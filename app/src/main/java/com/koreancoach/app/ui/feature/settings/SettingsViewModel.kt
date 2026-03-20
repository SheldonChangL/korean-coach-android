package com.koreancoach.app.ui.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.datastore.UserPreferencesDataStore
import com.koreancoach.app.domain.model.SpeechRatePreset
import com.koreancoach.app.domain.model.SpeechVoiceOption
import com.koreancoach.app.domain.model.ThemeMode
import com.koreancoach.app.domain.speech.SpeechPlaybackService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: UserPreferencesDataStore,
    private val speechPlaybackService: SpeechPlaybackService
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
                prefs.themeMode,
                speechPlaybackService.state
            ) { args ->
                @Suppress("UNCHECKED_CAST")
                val onboarding = args[0] as com.koreancoach.app.domain.model.OnboardingData
                val enabled = args[1] as Boolean
                val hour = args[2] as Int
                val min = args[3] as Int
                val theme = args[4] as ThemeMode
                val speechState = args[5] as com.koreancoach.app.domain.speech.SpeechPlaybackState
                SettingsUiState(
                    learnerName = onboarding.learnerName,
                    dailyGoalMinutes = onboarding.dailyGoalMinutes,
                    reminderEnabled = enabled,
                    reminderHour = hour,
                    reminderMinute = min,
                    themeMode = theme,
                    autoPlayHangul = onboarding.autoPlayHangul,
                    speechRatePreset = onboarding.speechRatePreset,
                    preferredVoiceKey = onboarding.preferredVoiceKey,
                    availableVoices = speechState.voices,
                    speechReady = speechState.isReady
                )
            }.collect { _state.value = it }
        }
    }

    fun updateDailyGoal(minutes: Int) = viewModelScope.launch { prefs.updateDailyGoal(minutes) }
    fun toggleReminders(enabled: Boolean) = viewModelScope.launch { prefs.updateReminder(enabled, _state.value.reminderHour, _state.value.reminderMinute) }
    fun updateTheme(mode: ThemeMode) = viewModelScope.launch { prefs.updateThemeMode(mode) }
    fun updateAutoPlayHangul(enabled: Boolean) = viewModelScope.launch { prefs.updateAutoPlayHangul(enabled) }
    fun updateSpeechRate(rate: SpeechRatePreset) = viewModelScope.launch { prefs.updateSpeechRate(rate) }
    fun updatePreferredVoiceKey(voiceKey: String) = viewModelScope.launch { prefs.updatePreferredVoiceKey(voiceKey) }
}

data class SettingsUiState(
    val learnerName: String = "",
    val dailyGoalMinutes: Int = 10,
    val reminderEnabled: Boolean = false,
    val reminderHour: Int = 20,
    val reminderMinute: Int = 0,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val autoPlayHangul: Boolean = true,
    val speechRatePreset: SpeechRatePreset = SpeechRatePreset.NORMAL,
    val preferredVoiceKey: String = "",
    val availableVoices: List<SpeechVoiceOption> = emptyList(),
    val speechReady: Boolean = false
)
