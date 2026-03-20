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

    val lessons: StateFlow<List<Lesson>> = lessonRepository.getAllLessons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lessonsByWeek: StateFlow<Map<Int, List<Lesson>>> = lessons.map { list ->
        list.groupBy { it.weekNumber }.toSortedMap()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
}
