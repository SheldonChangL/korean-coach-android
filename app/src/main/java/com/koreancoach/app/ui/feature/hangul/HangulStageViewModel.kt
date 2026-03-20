package com.koreancoach.app.ui.feature.hangul

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.curriculum.HangulCharacterData
import com.koreancoach.app.data.curriculum.CurriculumBootstrapper
import com.koreancoach.app.data.datastore.UserPreferencesDataStore
import com.koreancoach.app.data.repository.LessonRepository
import com.koreancoach.app.domain.model.CheckpointItem
import com.koreancoach.app.domain.model.Lesson
import com.koreancoach.app.domain.model.OnboardingData
import com.koreancoach.app.domain.model.SpeechRatePreset
import com.koreancoach.app.domain.model.SpeechSpec
import com.koreancoach.app.domain.speech.SpeechPlaybackService
import com.koreancoach.app.domain.speech.SpeechPlaybackState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HangulStageUiState(
    val lesson: Lesson? = null,
    val isLoading: Boolean = true,
    val autoPlayHangul: Boolean = true,
    val preferredRate: SpeechRatePreset = SpeechRatePreset.NORMAL,
    val speechState: SpeechPlaybackState = SpeechPlaybackState(),
    val selectedAnswers: Map<String, String> = emptyMap(),
    val completedCheckpoints: Set<String> = emptySet()
)

@HiltViewModel
class HangulStageViewModel @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val prefs: UserPreferencesDataStore,
    private val speechPlaybackService: SpeechPlaybackService,
    curriculumBootstrapper: CurriculumBootstrapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"])
    private val autoPlayedLessonIds = mutableSetOf<String>()
    private val selectedAnswers = MutableStateFlow<Map<String, String>>(emptyMap())
    private val completedCheckpoints = MutableStateFlow<Set<String>>(emptySet())
    private val isBootstrapping = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            curriculumBootstrapper.ensureSeeded()
            isBootstrapping.value = false
        }
    }

    val state: StateFlow<HangulStageUiState> = combine(
        lessonRepository.observeLessonById(lessonId),
        prefs.onboardingData,
        speechPlaybackService.state,
        selectedAnswers,
        completedCheckpoints,
        isBootstrapping
    ) { values ->
        val lesson = values[0] as Lesson?
        val onboarding = values[1] as OnboardingData
        val speechState = values[2] as SpeechPlaybackState
        @Suppress("UNCHECKED_CAST")
        val answers = values[3] as Map<String, String>
        @Suppress("UNCHECKED_CAST")
        val completed = values[4] as Set<String>
        val bootstrapping = values[5] as Boolean
        HangulStageUiState(
            lesson = lesson,
            isLoading = bootstrapping && lesson == null,
            autoPlayHangul = onboarding.autoPlayHangul,
            preferredRate = onboarding.speechRatePreset,
            speechState = speechState,
            selectedAnswers = answers,
            completedCheckpoints = completed
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HangulStageUiState())

    fun play(spec: SpeechSpec, fallback: String) {
        val current = state.value
        val effectiveSpec = if (spec.speechRatePreset == SpeechRatePreset.NORMAL) {
            spec.copy(speechRatePreset = current.preferredRate)
        } else spec
        speechPlaybackService.speak(effectiveSpec, fallback)
    }

    fun stopSpeaking() = speechPlaybackService.stop()

    fun maybeAutoPlay() {
        val current = state.value
        val lesson = current.lesson ?: return
        if (!current.autoPlayHangul) return
        if (!autoPlayedLessonIds.add(lesson.id)) return

        val firstReadingDrill = lesson.readingDrills.firstOrNull()
        if (firstReadingDrill != null) {
            play(firstReadingDrill.speech, firstReadingDrill.displayText)
            return
        }

        val firstWritingTarget = lesson.writingTargets.firstOrNull() ?: return
        val fallback = HangulCharacterData.findById(firstWritingTarget.characterId)?.character
            ?: firstWritingTarget.characterId
        play(firstWritingTarget.speech, fallback)
    }

    fun answerCheckpoint(item: CheckpointItem, answer: String) {
        selectedAnswers.update { it + (item.id to answer) }
        if (answer == item.correctAnswer) {
            completedCheckpoints.update { it + item.id }
        }
    }

    fun markComplete(onDone: () -> Unit) {
        viewModelScope.launch {
            lessonRepository.completeAndUnlockNext(lessonId)
            prefs.recordStudyActivity()
            onDone()
        }
    }
}
