package com.koreancoach.app.ui.feature.lesson

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.repository.LessonRepository
import com.koreancoach.app.domain.model.Lesson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonDetailViewModel @Inject constructor(
    private val lessonRepository: LessonRepository,
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
    }

    fun selectTab(tab: LessonTab) = _state.update { it.copy(selectedTab = tab) }

    fun markComplete() {
        viewModelScope.launch {
            lessonRepository.markLessonComplete(lessonId)
            _state.update { it.copy(lesson = it.lesson?.copy(isCompleted = true)) }
        }
    }
}

data class LessonDetailUiState(
    val lesson: Lesson? = null,
    val isLoading: Boolean = true,
    val selectedTab: LessonTab = LessonTab.VOCABULARY
)

enum class LessonTab(val label: String) {
    VOCABULARY("Vocabulary"),
    PHRASES("Phrases"),
    PRONUNCIATION("Pronunciation"),
    MEMORY("Memory Hooks")
}
