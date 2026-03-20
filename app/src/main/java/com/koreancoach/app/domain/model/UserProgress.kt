package com.koreancoach.app.domain.model

data class UserProgress(
    val totalLessonsCompleted: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val totalWordsLearned: Int,
    val totalPhrasesLearned: Int,
    val weeklyGoalMinutes: Int,
    val weeklyMinutesCompleted: Int,
    val masteredCardCount: Int,
    val reviewDueCount: Int
)
