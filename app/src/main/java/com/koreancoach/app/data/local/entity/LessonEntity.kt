package com.koreancoach.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey val id: String,
    val weekNumber: Int,
    val dayNumber: Int,
    val trackId: String,
    val lessonKind: String,
    val stageOrder: Int,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val estimatedMinutes: Int,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val learningObjective: String,
    val contentVersion: String,
    val contentJson: String
)
