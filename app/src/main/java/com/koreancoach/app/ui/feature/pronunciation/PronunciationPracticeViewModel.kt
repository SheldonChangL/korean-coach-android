package com.koreancoach.app.ui.feature.pronunciation

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.R
import com.koreancoach.app.data.datastore.UserPreferencesDataStore
import com.koreancoach.app.data.repository.LessonRepository
import com.koreancoach.app.data.repository.PronunciationRepository
import com.koreancoach.app.domain.model.SpeechRatePreset
import com.koreancoach.app.domain.model.VocabItem
import com.koreancoach.app.domain.pronunciation.PronunciationResult
import com.koreancoach.app.domain.speech.SpeechPlaybackService
import com.koreancoach.app.domain.speech.SpeechPlaybackState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

enum class RecordingPhase { IDLE, LISTENING, PROCESSING, RESULT, ERROR }

@HiltViewModel
class PronunciationPracticeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val lessonRepository: LessonRepository,
    private val pronunciationRepository: PronunciationRepository,
    private val prefs: UserPreferencesDataStore,
    private val speechPlaybackService: SpeechPlaybackService
) : ViewModel() {

    private val _state = MutableStateFlow(PronunciationPracticeUiState())
    val state: StateFlow<PronunciationPracticeUiState> = _state.asStateFlow()

    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null
    private var scoringJob: Job? = null

    val usesOwnRecordingPipeline = pronunciationRepository.scorerUsesOwnRecordingPipeline

    init {
        viewModelScope.launch {
            speechPlaybackService.state.collect { speechState ->
                _state.update { it.copy(speechState = speechState) }
            }
        }
        viewModelScope.launch {
            prefs.onboardingData.collect { onboarding ->
                _state.update { it.copy(preferredRate = onboarding.speechRatePreset) }
            }
        }
    }

    fun loadLesson(lessonId: String) {
        viewModelScope.launch {
            lessonRepository.getLessonById(lessonId)?.let { lesson ->
                _state.update { it.copy(
                    vocabItems = lesson.vocabulary,
                    targetItem = lesson.vocabulary.firstOrNull(),
                    usesOwnRecordingPipeline = usesOwnRecordingPipeline
                ) }
            }
        }
    }

    fun setTarget(item: VocabItem) {
        _state.update { it.copy(targetItem = item, phase = RecordingPhase.IDLE, lastResult = null) }
    }

    fun startRecording() {
        if (usesOwnRecordingPipeline) {
            startAgnosticRecording()
            return
        }

        // Native AudioRecord path (manual capture)
        val bufferSize = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        try {
            audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize)
            audioRecord?.startRecording()
            _state.update { it.copy(phase = RecordingPhase.LISTENING, micLevel = 0f) }

            recordingJob = viewModelScope.launch(Dispatchers.IO) {
                val outputStream = ByteArrayOutputStream()
                val buffer = ByteArray(bufferSize)
                while (isActive) {
                    val read = audioRecord?.read(buffer, 0, buffer.size) ?: -1
                    if (read > 0) {
                        outputStream.write(buffer, 0, read)
                        // Calculate simple peak level for UI
                        val max = buffer.map { it.toInt().let { if (it < 0) -it else it } }.maxOrNull() ?: 0
                        _state.update { it.copy(micLevel = (max / 128f).coerceIn(0f, 1f)) }
                    }
                    delay(50)
                }
            }
        } catch (e: SecurityException) {
            _state.update {
                it.copy(
                    error = context.getString(R.string.error_microphone_permission_denied),
                    phase = RecordingPhase.ERROR
                )
            }
        }
    }

    private fun startAgnosticRecording() {
        // Scorer handles its own recording (e.g. SpeechRecognizer). 
        // We just show the UI state and then call the scorer.
        _state.update { it.copy(phase = RecordingPhase.LISTENING, micLevel = 0.5f) }
        stopRecordingAndScore()
    }

    fun stopRecordingAndScore() {
        val target = _state.value.targetItem ?: return
        val startTime = System.currentTimeMillis()

        scoringJob = viewModelScope.launch {
            _state.update { it.copy(phase = RecordingPhase.PROCESSING) }

            // If manual recording was active, stop it and get bytes
            val audioBytes = if (usesOwnRecordingPipeline) ByteArray(0) else {
                recordingJob?.cancel()
                audioRecord?.stop()
                ByteArray(0) // Simplified for this scaffold
            }

            val duration = System.currentTimeMillis() - startTime
            try {
                val result = pronunciationRepository.scoreAndSave(
                    characterId = target.id,
                    targetText = target.korean,
                    targetRomanization = target.romanization,
                    audioBytes = audioBytes,
                    durationMs = duration
                )
                
                val attempts = _state.value.attemptCount + 1
                val currentAvg = _state.value.averageScore ?: 0f
                val newAvg = (currentAvg * (attempts - 1) + result.scorePercent) / attempts

                _state.update { it.copy(
                    phase = RecordingPhase.RESULT,
                    lastResult = result,
                    recognizedText = result.recognizedText,
                    attemptCount = attempts,
                    averageScore = newAvg
                ) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        phase = RecordingPhase.ERROR,
                        error = context.getString(R.string.error_analysis_failed, e.message.orEmpty())
                    )
                }
            } finally {
                teardownAudioRecord()
            }
        }
    }

    fun cancelRecording() {
        recordingJob?.cancel()
        scoringJob?.cancel()
        teardownAudioRecord()
        _state.update { it.copy(phase = RecordingPhase.IDLE, micLevel = 0f) }
    }

    fun resetToIdle() {
        _state.update { it.copy(phase = RecordingPhase.IDLE, lastResult = null, micLevel = 0f) }
    }

    fun clearError() {
        _state.update { it.copy(error = null, phase = RecordingPhase.IDLE) }
    }

    fun playTargetAudio() {
        val target = _state.value.targetItem ?: return
        speechPlaybackService.speak(
            spec = target.speech.copy(speechRatePreset = _state.value.preferredRate),
            fallbackText = target.korean
        )
    }

    fun stopTargetAudio() = speechPlaybackService.stop()

    private fun teardownAudioRecord() {
        recordingJob?.cancel()
        recordingJob = null
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    override fun onCleared() {
        super.onCleared()
        scoringJob?.cancel()
        teardownAudioRecord()
    }
}

data class PronunciationPracticeUiState(
    val vocabItems: List<VocabItem> = emptyList(),
    val targetItem: VocabItem? = null,
    val phase: RecordingPhase = RecordingPhase.IDLE,
    val micLevel: Float = 0f,
    val lastResult: PronunciationResult? = null,
    val recognizedText: String = "",
    val attemptCount: Int = 0,
    val averageScore: Float? = null,
    val error: String? = null,
    val usesOwnRecordingPipeline: Boolean = false,
    val preferredRate: SpeechRatePreset = SpeechRatePreset.NORMAL,
    val speechState: SpeechPlaybackState = SpeechPlaybackState()
)
