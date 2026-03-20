package com.koreancoach.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class FlashCardEntity(
    @PrimaryKey val id: String,
    val lessonId: String,
    val front: String,
    val frontSubtext: String,
    val back: String,
    val backSubtext: String,
    val memoryHook: String,
    val reviewState: String,
    val nextReviewTimestamp: Long,
    val easeFactor: Float,
    val intervalDays: Int
)
