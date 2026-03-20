package com.koreancoach.app.ui.feature.hangul

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.curriculum.CurriculumBootstrapper
import com.koreancoach.app.data.curriculum.HangulCharacterData
import com.koreancoach.app.data.repository.LessonRepository
import com.koreancoach.app.domain.model.HangulCharacter
import com.koreancoach.app.domain.model.SpeechSpec
import com.koreancoach.app.domain.speech.SpeechPlaybackService
import com.koreancoach.app.domain.speech.SpeechPlaybackState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HangulExploreUiState(
    val characters: List<HangulCharacter> = HangulCharacterData.allCharacters,
    val stageLookup: Map<String, String> = emptyMap(),
    val unlockedStageIds: Set<String> = emptySet(),
    val completedStageIds: Set<String> = emptySet(),
    val isLoading: Boolean = true,
    val speechState: SpeechPlaybackState = SpeechPlaybackState()
)

@HiltViewModel
class HangulExploreViewModel @Inject constructor(
    lessonRepository: LessonRepository,
    curriculumBootstrapper: CurriculumBootstrapper,
    private val speechPlaybackService: SpeechPlaybackService
) : ViewModel() {

    private val isBootstrapping = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            curriculumBootstrapper.ensureSeeded()
            isBootstrapping.value = false
        }
    }

    val state: StateFlow<HangulExploreUiState> = combine(
        lessonRepository.getLessonsByTrack("hangul_sprint"),
        speechPlaybackService.state,
        isBootstrapping
    ) { lessons, speechState, bootstrapping ->
        val stageLookup = buildMap {
            lessons.forEach { lesson ->
                lesson.writingTargets.forEach { put(it.characterId, lesson.id) }
                lesson.vocabulary.forEach { vocab ->
                    HangulCharacterData.allCharacters.firstOrNull { it.character == vocab.korean }?.let { put(it.id, lesson.id) }
                }
            }
        }
        HangulExploreUiState(
            stageLookup = stageLookup,
            unlockedStageIds = lessons.filter { it.isUnlocked }.map { it.id }.toSet(),
            completedStageIds = lessons.filter { it.isCompleted }.map { it.id }.toSet(),
            isLoading = bootstrapping && lessons.isEmpty(),
            speechState = speechState
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HangulExploreUiState())

    fun playCharacter(character: HangulCharacter) {
        speechPlaybackService.speak(
            spec = SpeechSpec(speechText = character.name),
            fallbackText = character.name
        )
    }

    fun stopSpeaking() = speechPlaybackService.stop()
}
