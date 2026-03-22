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
                isBootstrapping,
                reviewRepository.getMasteredIds()
            ) { args ->
                @Suppress("UNCHECKED_CAST")
                val onboarding = args[0] as OnboardingData
                val completedCount = args[1] as Int
                val dueCount = args[2] as Int
                val totalMastered = args[3] as Int
                @Suppress("UNCHECKED_CAST")
                val lessons = args[4] as List<Lesson>
                val streak = args[5] as Int
                val weeklyMinutes = args[6] as Int
                val bootstrapping = args[7] as Boolean
                @Suppress("UNCHECKED_CAST")
                val masteredIds = args[8] as List<String>

                val hangulLessons = lessons.filter { it.trackId == "hangul_sprint" }
                val survivalLessons = lessons.filter { it.trackId == "survival_korean" }

                val stage1Vocab = hangulLessons.find { it.id == "hangul_stage_1" }?.vocabulary ?: emptyList()
                val stage2Vocab = hangulLessons.find { it.id == "hangul_stage_2" }?.vocabulary ?: emptyList()
                val stage4Vocab = hangulLessons.find { it.id == "hangul_stage_4" }?.vocabulary ?: emptyList()
                val stage5Vocab = hangulLessons.find { it.id == "hangul_stage_5" }?.vocabulary ?: emptyList()

                val stage1Mastered = stage1Vocab.count { masteredIds.contains("fc_hangul_stage_1_${it.id}") }
                val stage2Mastered = stage2Vocab.count { masteredIds.contains("fc_hangul_stage_2_${it.id}") }
                val stage4Mastered = stage4Vocab.count { masteredIds.contains("fc_hangul_stage_4_${it.id}") }
                val stage5Mastered = stage5Vocab.count { masteredIds.contains("fc_hangul_stage_5_${it.id}") }

                DashboardUiState(
                    onboarding = onboarding,
                    completedLessons = completedCount,
                    reviewDueCount = dueCount,
                    masteredCards = totalMastered,
                    recentLessons = survivalLessons.filter { it.isUnlocked }.take(3),
                    allLessons = lessons,
                    nextHangulLesson = hangulLessons.firstOrNull { it.isUnlocked && !it.isCompleted },
                    hangulCompletedStages = hangulLessons.count { it.isCompleted },
                    hangulTotalStages = hangulLessons.size,
                    masteredVowels = stage1Mastered,
                    totalVowels = stage1Vocab.size.coerceAtLeast(10),
                    masteredConsonants = stage2Mastered,
                    totalConsonants = stage2Vocab.size.coerceAtLeast(14),
                    masteredDoubleConsonants = stage4Mastered,
                    totalDoubleConsonants = stage4Vocab.size.coerceAtLeast(5),
                    masteredCompoundVowels = stage5Mastered,
                    totalCompoundVowels = stage5Vocab.size.coerceAtLeast(11),
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
    val hangulCompletedStages: Int = 0,
    val hangulTotalStages: Int = 0,
    val masteredVowels: Int = 0,
    val totalVowels: Int = 10,
    val masteredConsonants: Int = 0,
    val totalConsonants: Int = 14,
    val masteredDoubleConsonants: Int = 0,
    val totalDoubleConsonants: Int = 5,
    val masteredCompoundVowels: Int = 0,
    val totalCompoundVowels: Int = 11,
    val currentStreak: Int = 0,
    val weeklyMinutes: Int = 0,
    val isLoading: Boolean = true
)
