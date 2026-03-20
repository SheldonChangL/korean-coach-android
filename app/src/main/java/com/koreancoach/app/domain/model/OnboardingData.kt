package com.koreancoach.app.domain.model

import androidx.annotation.StringRes
import com.koreancoach.app.R

data class OnboardingData(
    val learnerName: String = "",
    val dailyGoalMinutes: Int = 10,
    val learningReason: LearningReason = LearningReason.TRAVEL,
    val preferredTime: StudyTime = StudyTime.MORNING,
    val uiLanguage: UiLanguage = UiLanguage.ENGLISH,
    val hangulLevel: HangulLevel = HangulLevel.COMPLETE_BEGINNER,
    val autoPlayHangul: Boolean = true,
    val speechRatePreset: SpeechRatePreset = SpeechRatePreset.NORMAL,
    val preferredVoiceKey: String = "",
    val onboardingComplete: Boolean = false
)

enum class LearningReason(@StringRes val labelRes: Int, val emoji: String) {
    TRAVEL(R.string.reason_travel, "✈️"),
    KPOP(R.string.reason_kpop, "🎵"),
    HERITAGE(R.string.reason_heritage, "👨‍👩‍👧"),
    WORK(R.string.reason_work, "💼"),
    CHALLENGE(R.string.reason_challenge, "🏆")
}

enum class StudyTime(@StringRes val labelRes: Int, val emoji: String) {
    MORNING(R.string.time_morning, "🌅"),
    AFTERNOON(R.string.time_afternoon, "☀️"),
    EVENING(R.string.time_evening, "🌙"),
    FLEXIBLE(R.string.time_flexible, "🕐")
}

enum class UiLanguage(@StringRes val labelRes: Int) {
    ENGLISH(R.string.lang_english),
    TRADITIONAL_CHINESE(R.string.lang_chinese)
}

enum class HangulLevel(@StringRes val labelRes: Int, val emoji: String) {
    COMPLETE_BEGINNER(R.string.hangul_beginner, "🔰"),
    KNOW_SOME(R.string.hangul_some, "🧩"),
    CAN_READ(R.string.hangul_can_read, "✅")
}
