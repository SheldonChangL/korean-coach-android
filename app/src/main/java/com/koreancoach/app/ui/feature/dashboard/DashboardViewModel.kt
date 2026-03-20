package com.koreancoach.app.ui.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.curriculum.CurriculumBootstrapper
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
    private val prefs: UserPreferencesDataStore,
    private val curriculumBootstrapper: CurriculumBootstrapper
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()
    private val isBootstrapping = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            curriculumBootstrapper.ensureSeeded()
            isBootstrapping.value = false
        }
        viewModelScope.launch {
            combine(
                prefs.onboardingData,
                lessonRepository.getCompletedLessonCount(),
                reviewRepository.getDueCount(System.currentTimeMillis()),
                reviewRepository.getMasteredCount(),
                lessonRepository.getAllLessons(),
                prefs.currentStreak,
                analyticsRepository.getWeeklyTotalMinutes(),
                isBootstrapping
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
                val bootstrapping = args[7] as Boolean
                val hangulLessons = lessons.filter { it.trackId == "hangul_sprint" }
                val survivalLessons = lessons.filter { it.trackId == "survival_korean" }
                DashboardUiState(
                    onboarding = onboarding,
                    completedLessons = completed,
                    reviewDueCount = dueCount,
                    masteredCards = mastered,
                    recentLessons = survivalLessons.filter { it.isUnlocked }.take(3),
                    allLessons = lessons,
                    nextHangulLesson = hangulLessons.firstOrNull { it.isUnlocked && !it.isCompleted },
                    hangulCompletedCount = hangulLessons.count { it.isCompleted },
                    hangulTotalCount = hangulLessons.size,
                    nextSurvivalLesson = survivalLessons.firstOrNull { it.isUnlocked && !it.isCompleted },
                    currentStreak = streak,
                    weeklyMinutes = weeklyMinutes,
                    isLoading = bootstrapping && lessons.isEmpty()
                )
            }.collect { newState ->
                _state.value = newState
            }
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
    val nextHangulLesson: Lesson? = null,
    val nextSurvivalLesson: Lesson? = null,
    val hangulCompletedCount: Int = 0,
    val hangulTotalCount: Int = 0,
    val currentStreak: Int = 0,
    val weeklyMinutes: Int = 0,
    val isLoading: Boolean = true
)
