package com.koreancoach.app.ui.feature.hangul

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.curriculum.CurriculumBootstrapper
import com.koreancoach.app.data.repository.LessonRepository
import com.koreancoach.app.domain.model.Lesson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HangulPathUiState(
    val lessons: List<Lesson> = emptyList(),
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val isLoading: Boolean = true
) {
    val progressFraction: Float
        get() = if (totalCount == 0) 0f else completedCount.toFloat() / totalCount

    val nextLessonId: String?
        get() = lessons.firstOrNull { it.isUnlocked && !it.isCompleted }?.id

    val shouldShowEmptyState: Boolean
        get() = !isLoading && lessons.isEmpty()
}

@HiltViewModel
class HangulPathViewModel @Inject constructor(
    lessonRepository: LessonRepository,
    curriculumBootstrapper: CurriculumBootstrapper
) : ViewModel() {

    private val isBootstrapping = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            curriculumBootstrapper.ensureSeeded()
            isBootstrapping.value = false
        }
    }

    val state: StateFlow<HangulPathUiState> = combine(
        lessonRepository.getLessonsByTrack("hangul_sprint"),
        isBootstrapping
    ) { lessons, bootstrapping ->
            HangulPathUiState(
                lessons = lessons,
                completedCount = lessons.count { it.isCompleted },
                totalCount = lessons.size,
                isLoading = bootstrapping && lessons.isEmpty()
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HangulPathUiState())
}
