package com.koreancoach.app.ui.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.datastore.UserPreferencesDataStore
import com.koreancoach.app.domain.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: UserPreferencesDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                prefs.onboardingData,
                prefs.reminderEnabled,
                prefs.reminderHour,
                prefs.reminderMinute,
                prefs.themeMode
            ) { onboarding, enabled, hour, min, theme ->
                SettingsUiState(
                    learnerName = onboarding.learnerName,
                    dailyGoalMinutes = onboarding.dailyGoalMinutes,
                    reminderEnabled = enabled,
                    reminderHour = hour,
                    reminderMinute = min,
                    themeMode = theme
                )
            }.collect { _state.value = it }
        }
    }

    fun updateDailyGoal(minutes: Int) = viewModelScope.launch { prefs.updateDailyGoal(minutes) }
    fun toggleReminders(enabled: Boolean) = viewModelScope.launch { prefs.updateReminder(enabled, _state.value.reminderHour, _state.value.reminderMinute) }
    fun updateTheme(mode: ThemeMode) = viewModelScope.launch { prefs.updateThemeMode(mode) }
}

data class SettingsUiState(
    val learnerName: String = "",
    val dailyGoalMinutes: Int = 10,
    val reminderEnabled: Boolean = false,
    val reminderHour: Int = 20,
    val reminderMinute: Int = 0,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)
