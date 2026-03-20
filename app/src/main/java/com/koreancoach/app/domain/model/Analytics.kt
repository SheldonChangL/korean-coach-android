package com.koreancoach.app.domain.model

data class DailyStudyMinutes(
    val dayEpoch: Long,   // days since Unix epoch
    val minutes: Int
)

data class QuizAccuracyPoint(
    val index: Int,       // position in trend (0 = oldest shown)
    val accuracy: Int,    // 0–100 %
    val completedAtMs: Long
)
