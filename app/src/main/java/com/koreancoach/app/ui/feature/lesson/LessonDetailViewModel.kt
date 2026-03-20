package com.koreancoach.app.ui.feature.lesson

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.datastore.UserPreferencesDataStore
import com.koreancoach.app.data.repository.LessonRepository
import com.koreancoach.app.domain.model.Lesson
import com.koreancoach.app.domain.model.SpeechRatePreset
import com.koreancoach.app.domain.model.SpeechSpec
import com.koreancoach.app.domain.speech.SpeechPlaybackService
import com.koreancoach.app.domain.speech.SpeechPlaybackState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonDetailViewModel @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val prefs: UserPreferencesDataStore,
    private val speechPlaybackService: SpeechPlaybackService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"])

    private val _state = MutableStateFlow(LessonDetailUiState())
    val state: StateFlow<LessonDetailUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val lesson = lessonRepository.getLessonById(lessonId)
            _state.update { it.copy(lesson = lesson, isLoading = false) }
        }
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

    fun selectTab(tab: LessonTab) = _state.update { it.copy(selectedTab = tab) }

    fun markComplete() {
        viewModelScope.launch {
            lessonRepository.completeAndUnlockNext(lessonId)
            prefs.recordStudyActivity()
            _state.update { it.copy(lesson = it.lesson?.copy(isCompleted = true)) }
        }
    }

    fun playSpeech(spec: SpeechSpec, fallbackText: String) {
        val rate = if (spec.speechRatePreset == SpeechRatePreset.NORMAL) _state.value.preferredRate else spec.speechRatePreset
        speechPlaybackService.speak(
            spec.copy(speechRatePreset = rate),
            fallbackText
        )
    }

    fun stopSpeech() = speechPlaybackService.stop()
}

data class LessonDetailUiState(
    val lesson: Lesson? = null,
    val isLoading: Boolean = true,
    val selectedTab: LessonTab = LessonTab.VOCABULARY,
    val preferredRate: SpeechRatePreset = SpeechRatePreset.NORMAL,
    val speechState: SpeechPlaybackState = SpeechPlaybackState()
)

enum class LessonTab(val label: String) {
    VOCABULARY("Vocabulary"),
    PHRASES("Phrases"),
    PRONUNCIATION("Pronunciation"),
    DIALOGUE("Dialogue"),
    MEMORY("Memory Hooks")
}
