package com.koreancoach.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pronunciation_attempts")
data class PronunciationAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val characterId: String,       // e.g. "annyeong", "w1d1_v1"
    val targetText: String,        // The Korean text to pronounce
    val recognizedText: String = "",   // What ASR actually heard (empty for fake scorer)
    val scorePercent: Int,         // 0–100 from scorer
    val durationMs: Long,          // Recording length
    val scoringSource: String = "FAKE",  // "FAKE" or "REAL"
    val completedAtMs: Long = System.currentTimeMillis()
)
