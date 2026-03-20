package com.koreancoach.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey val id: String,
    val weekNumber: Int,
    val dayNumber: Int,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val estimatedMinutes: Int,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val contentJson: String
)
