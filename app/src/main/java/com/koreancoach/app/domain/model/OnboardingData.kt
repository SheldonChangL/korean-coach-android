package com.koreancoach.app.domain.model

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

enum class LearningReason(val label: String, val emoji: String) {
    TRAVEL("Travel to Korea", "✈️"),
    KPOP("K-pop & K-drama", "🎵"),
    HERITAGE("Heritage & family", "👨‍👩‍👧"),
    WORK("Work & business", "💼"),
    CHALLENGE("Personal challenge", "🏆")
}

enum class StudyTime(val label: String, val emoji: String) {
    MORNING("Morning", "🌅"),
    AFTERNOON("Afternoon", "☀️"),
    EVENING("Evening", "🌙"),
    FLEXIBLE("Flexible", "🕐")
}

enum class UiLanguage(val label: String) {
    ENGLISH("English"),
    TRADITIONAL_CHINESE("Traditional Chinese")
}

enum class HangulLevel(val label: String, val emoji: String) {
    COMPLETE_BEGINNER("I can't read Hangul yet", "🔰"),
    KNOW_SOME("I know a few letters", "🧩"),
    CAN_READ("I can already read Hangul", "✅")
}
