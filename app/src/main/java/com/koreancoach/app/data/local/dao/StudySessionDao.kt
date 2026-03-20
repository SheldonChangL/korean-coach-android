package com.koreancoach.app.data.local.dao

import androidx.room.*
import com.koreancoach.app.data.local.entity.StudySessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {

    @Insert
    suspend fun insertSession(session: StudySessionEntity)

    @Query("SELECT * FROM study_sessions ORDER BY completedAtMs DESC")
    fun getAllSessions(): Flow<List<StudySessionEntity>>

    /** Returns per-day study minutes for the last N days (epoch day as key). */
    @Query("""
        SELECT (completedAtMs / 86400000) AS dayEpoch, SUM(durationMinutes) AS totalMinutes
        FROM study_sessions
        WHERE completedAtMs >= :sinceMs
        GROUP BY dayEpoch
        ORDER BY dayEpoch ASC
    """)
    fun getDailyMinutesSince(sinceMs: Long): Flow<List<DailyMinutesRow>>

    @Query("SELECT SUM(durationMinutes) FROM study_sessions WHERE completedAtMs >= :sinceMs")
    fun getTotalMinutesSince(sinceMs: Long): Flow<Int?>
}

data class DailyMinutesRow(val dayEpoch: Long, val totalMinutes: Int)
