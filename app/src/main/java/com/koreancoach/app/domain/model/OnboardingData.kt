package com.koreancoach.app.domain.model

data class OnboardingData(
    val learnerName: String = "",
    val dailyGoalMinutes: Int = 10,
    val learningReason: LearningReason = LearningReason.TRAVEL,
    val preferredTime: StudyTime = StudyTime.MORNING,
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
