package com.koreancoach.app.ui.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.datastore.UserPreferencesDataStore
import com.koreancoach.app.domain.model.LearningReason
import com.koreancoach.app.domain.model.OnboardingData
import com.koreancoach.app.domain.model.StudyTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val prefs: UserPreferencesDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingUiState())
    val state: StateFlow<OnboardingUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            prefs.onboardingData.first().let { data ->
                if (data.onboardingComplete) {
                    _state.update { it.copy(isComplete = true) }
                }
            }
        }
    }

    fun setName(name: String) = _state.update { it.copy(name = name) }
    fun setReason(reason: LearningReason) = _state.update { it.copy(selectedReason = reason) }
    fun setStudyTime(time: StudyTime) = _state.update { it.copy(selectedTime = time) }
    fun setDailyGoal(minutes: Int) = _state.update { it.copy(dailyGoalMinutes = minutes) }
    fun nextPage() = _state.update { it.copy(currentPage = (it.currentPage + 1).coerceAtMost(OnboardingPage.entries.size - 1)) }
    fun prevPage() = _state.update { it.copy(currentPage = (it.currentPage - 1).coerceAtLeast(0)) }

    fun completeOnboarding() {
        viewModelScope.launch {
            val s = _state.value
            prefs.saveOnboarding(
                OnboardingData(
                    learnerName = s.name,
                    dailyGoalMinutes = s.dailyGoalMinutes,
                    learningReason = s.selectedReason,
                    preferredTime = s.selectedTime,
                    onboardingComplete = true
                )
            )
            _state.update { it.copy(isComplete = true) }
        }
    }
}

data class OnboardingUiState(
    val currentPage: Int = 0,
    val name: String = "",
    val selectedReason: LearningReason = LearningReason.TRAVEL,
    val selectedTime: StudyTime = StudyTime.MORNING,
    val dailyGoalMinutes: Int = 10,
    val isComplete: Boolean = false
)

enum class OnboardingPage { WELCOME, NAME, REASON, TIME, GOAL, READY }
