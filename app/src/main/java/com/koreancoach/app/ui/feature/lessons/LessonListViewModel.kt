package com.koreancoach.app.ui.feature.lessons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.repository.LessonRepository
import com.koreancoach.app.domain.model.Lesson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LessonListViewModel @Inject constructor(
    private val lessonRepository: LessonRepository
) : ViewModel() {

    val lessonsByWeek: StateFlow<Map<Int, List<Lesson>>> = lessonRepository.getAllLessons()
        .map { lessons -> lessons.groupBy { it.weekNumber } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
}
