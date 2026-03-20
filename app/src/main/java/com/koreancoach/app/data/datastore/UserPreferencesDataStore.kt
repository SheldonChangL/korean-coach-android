package com.koreancoach.app.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.koreancoach.app.domain.model.LearningReason
import com.koreancoach.app.domain.model.OnboardingData
import com.koreancoach.app.domain.model.StudyTime
import com.koreancoach.app.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>
) {
    companion object {
        val KEY_NAME = stringPreferencesKey("learner_name")
        val KEY_DAILY_GOAL = intPreferencesKey("daily_goal_minutes")
        val KEY_REASON = stringPreferencesKey("learning_reason")
        val KEY_STUDY_TIME = stringPreferencesKey("study_time")
        val KEY_ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
        val KEY_STREAK = intPreferencesKey("current_streak")
        val KEY_LAST_STUDY_DATE = longPreferencesKey("last_study_date_ms")
        val KEY_REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
        val KEY_REMINDER_HOUR = intPreferencesKey("reminder_hour")
        val KEY_REMINDER_MINUTE = intPreferencesKey("reminder_minute")
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
    }

    val onboardingData: Flow<OnboardingData> = dataStore.data.map { prefs ->
        OnboardingData(
            learnerName = prefs[KEY_NAME] ?: "",
            dailyGoalMinutes = prefs[KEY_DAILY_GOAL] ?: 10,
            learningReason = prefs[KEY_REASON]?.let { runCatching { LearningReason.valueOf(it) }.getOrNull() } ?: LearningReason.TRAVEL,
            preferredTime = prefs[KEY_STUDY_TIME]?.let { runCatching { StudyTime.valueOf(it) }.getOrNull() } ?: StudyTime.MORNING,
            onboardingComplete = prefs[KEY_ONBOARDING_COMPLETE] ?: false
        )
    }

    val currentStreak: Flow<Int> = dataStore.data.map { it[KEY_STREAK] ?: 0 }

    val reminderEnabled: Flow<Boolean> = dataStore.data.map { it[KEY_REMINDER_ENABLED] ?: false }

    val reminderHour: Flow<Int> = dataStore.data.map { it[KEY_REMINDER_HOUR] ?: 20 }

    val reminderMinute: Flow<Int> = dataStore.data.map { it[KEY_REMINDER_MINUTE] ?: 0 }

    val themeMode: Flow<ThemeMode> = dataStore.data.map { prefs ->
        prefs[KEY_THEME_MODE]?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() } ?: ThemeMode.SYSTEM
    }

    val dailyGoalMinutes: Flow<Int> = dataStore.data.map { it[KEY_DAILY_GOAL] ?: 10 }

    suspend fun saveOnboarding(data: OnboardingData) {
        dataStore.edit { prefs ->
            prefs[KEY_NAME] = data.learnerName
            prefs[KEY_DAILY_GOAL] = data.dailyGoalMinutes
            prefs[KEY_REASON] = data.learningReason.name
            prefs[KEY_STUDY_TIME] = data.preferredTime.name
            prefs[KEY_ONBOARDING_COMPLETE] = data.onboardingComplete
        }
    }

    suspend fun updateStreak(streak: Int) {
        dataStore.edit { it[KEY_STREAK] = streak }
    }

    suspend fun updateLastStudyDate(timestampMs: Long) {
        dataStore.edit { it[KEY_LAST_STUDY_DATE] = timestampMs }
    }

    suspend fun updateReminder(enabled: Boolean, hour: Int, minute: Int) {
        dataStore.edit { prefs ->
            prefs[KEY_REMINDER_ENABLED] = enabled
            prefs[KEY_REMINDER_HOUR] = hour
            prefs[KEY_REMINDER_MINUTE] = minute
        }
    }

    suspend fun updateThemeMode(mode: ThemeMode) {
        dataStore.edit { it[KEY_THEME_MODE] = mode.name }
    }

    suspend fun updateDailyGoal(minutes: Int) {
        dataStore.edit { it[KEY_DAILY_GOAL] = minutes }
    }

    /** Computes and persists the streak based on the last study date. Call after any study activity. */
    suspend fun recordStudyActivity() {
        dataStore.edit { prefs ->
            val nowMs = System.currentTimeMillis()
            val lastMs = prefs[KEY_LAST_STUDY_DATE] ?: 0L
            val currentStreak = prefs[KEY_STREAK] ?: 0
            val dayMs = 24 * 60 * 60 * 1000L
            val todayStart = (nowMs / dayMs) * dayMs
            val yesterdayStart = todayStart - dayMs
            val lastDayStart = (lastMs / dayMs) * dayMs

            val newStreak = when {
                lastMs == 0L -> 1                            // first ever study
                lastDayStart == todayStart -> currentStreak  // already studied today
                lastDayStart == yesterdayStart -> currentStreak + 1 // studied yesterday
                else -> 1                                    // streak broken
            }
            prefs[KEY_STREAK] = newStreak
            prefs[KEY_LAST_STUDY_DATE] = nowMs
        }
    }
}
