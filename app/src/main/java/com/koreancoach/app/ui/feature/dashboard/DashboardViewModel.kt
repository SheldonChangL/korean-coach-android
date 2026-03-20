package com.koreancoach.app.ui.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.curriculum.CurriculumData
import com.koreancoach.app.data.datastore.UserPreferencesDataStore
import com.koreancoach.app.data.repository.AnalyticsRepository
import com.koreancoach.app.data.repository.LessonRepository
import com.koreancoach.app.data.repository.ReviewRepository
import com.koreancoach.app.domain.model.Lesson
import com.koreancoach.app.domain.model.OnboardingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val reviewRepository: ReviewRepository,
    private val analyticsRepository: AnalyticsRepository,
    private val prefs: UserPreferencesDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch { seedIfNeeded() }
        viewModelScope.launch {
            combine(
                prefs.onboardingData,
                lessonRepository.getCompletedLessonCount(),
                reviewRepository.getDueCount(System.currentTimeMillis()),
                reviewRepository.getMasteredCount(),
                lessonRepository.getAllLessons(),
                prefs.currentStreak,
                analyticsRepository.getWeeklyTotalMinutes()
            ) { args ->
                @Suppress("UNCHECKED_CAST")
                val onboarding = args[0] as OnboardingData
                val completed = args[1] as Int
                val dueCount = args[2] as Int
                val mastered = args[3] as Int
                @Suppress("UNCHECKED_CAST")
                val lessons = args[4] as List<Lesson>
                val streak = args[5] as Int
                val weeklyMinutes = args[6] as Int
                DashboardUiState(
                    onboarding = onboarding,
                    completedLessons = completed,
                    reviewDueCount = dueCount,
                    masteredCards = mastered,
                    recentLessons = lessons.filter { it.isUnlocked }.take(3),
                    allLessons = lessons,
                    currentStreak = streak,
                    weeklyMinutes = weeklyMinutes,
                    isLoading = false
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    private suspend fun seedIfNeeded() {
        val lessons = lessonRepository.getAllLessons().first()
        if (lessons.isEmpty()) {
            val curriculumLessons = CurriculumData.getAllLessons()
            val flashCards = CurriculumData.getAllFlashCards()
            lessonRepository.seedCurriculum(curriculumLessons, flashCards)
        }
    }
}

data class DashboardUiState(
    val onboarding: OnboardingData = OnboardingData(),
    val completedLessons: Int = 0,
    val reviewDueCount: Int = 0,
    val masteredCards: Int = 0,
    val recentLessons: List<Lesson> = emptyList(),
    val allLessons: List<Lesson> = emptyList(),
    val currentStreak: Int = 0,
    val weeklyMinutes: Int = 0,
    val isLoading: Boolean = true
)
