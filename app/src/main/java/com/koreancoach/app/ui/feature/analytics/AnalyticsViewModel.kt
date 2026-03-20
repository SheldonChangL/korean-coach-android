package com.koreancoach.app.ui.feature.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.datastore.UserPreferencesDataStore
import com.koreancoach.app.data.local.dao.DailyPronunciationRow
import com.koreancoach.app.data.repository.AnalyticsRepository
import com.koreancoach.app.domain.model.DailyStudyMinutes
import com.koreancoach.app.domain.model.QuizAccuracyPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val analyticsRepository: AnalyticsRepository,
    private val prefs: UserPreferencesDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(AnalyticsUiState())
    val state: StateFlow<AnalyticsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // Chain two combines to stay within the 5-flow overload limit.
            val studyAndQuizFlow = combine(
                analyticsRepository.getWeeklyStudyMinutes(),
                analyticsRepository.getWeeklyTotalMinutes(),
                analyticsRepository.getQuizAccuracyTrend(),
                analyticsRepository.getAverageAccuracy(),
                analyticsRepository.getWeeklyPronunciationTrend()
            ) { dailyMinutes, weeklyTotal, accuracyTrend, avgAccuracy, pronTrend ->
                PartialAnalytics(dailyMinutes, weeklyTotal, accuracyTrend, avgAccuracy, pronTrend)
            }

            combine(
                studyAndQuizFlow,
                analyticsRepository.getOverallAveragePronunciationScore(),
                prefs.currentStreak
            ) { partial, overallPron, streak ->
                AnalyticsUiState(
                    weeklyStudyMinutes = partial.weeklyStudyMinutes,
                    weeklyTotalMinutes = partial.weeklyTotalMinutes,
                    quizAccuracyTrend = partial.quizAccuracyTrend,
                    averageAccuracy = partial.averageAccuracy,
                    weeklyPronunciationTrend = partial.weeklyPronunciationTrend,
                    overallPronunciationScore = overallPron,
                    currentStreak = streak,
                    isLoading = false
                )
            }.collect { _state.value = it }
        }
    }

    private data class PartialAnalytics(
        val weeklyStudyMinutes: List<DailyStudyMinutes>,
        val weeklyTotalMinutes: Int,
        val quizAccuracyTrend: List<QuizAccuracyPoint>,
        val averageAccuracy: Float?,
        val weeklyPronunciationTrend: List<DailyPronunciationRow>
    )
}

data class AnalyticsUiState(
    val weeklyStudyMinutes: List<DailyStudyMinutes> = emptyList(),
    val weeklyTotalMinutes: Int = 0,
    val quizAccuracyTrend: List<QuizAccuracyPoint> = emptyList(),
    val averageAccuracy: Float? = null,
    val weeklyPronunciationTrend: List<DailyPronunciationRow> = emptyList(),
    val overallPronunciationScore: Float? = null,
    val currentStreak: Int = 0,
    val isLoading: Boolean = true
)
