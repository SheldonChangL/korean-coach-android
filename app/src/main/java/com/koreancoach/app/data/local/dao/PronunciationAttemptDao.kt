package com.koreancoach.app.data.local.dao

import androidx.room.*
import com.koreancoach.app.data.local.entity.PronunciationAttemptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PronunciationAttemptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attempt: PronunciationAttemptEntity)

    @Query("SELECT * FROM pronunciation_attempts WHERE characterId = :characterId ORDER BY completedAtMs DESC")
    fun getAttemptsForCharacter(characterId: String): Flow<List<PronunciationAttemptEntity>>

    @Query("SELECT * FROM pronunciation_attempts ORDER BY completedAtMs DESC LIMIT :limit")
    fun getRecentAttempts(limit: Int = 20): Flow<List<PronunciationAttemptEntity>>

    @Query("SELECT AVG(scorePercent) FROM pronunciation_attempts WHERE characterId = :characterId")
    fun getAverageScore(characterId: String): Flow<Float?>

    @Query("SELECT COUNT(*) FROM pronunciation_attempts")
    fun getTotalAttemptCount(): Flow<Int>

    /**
     * Returns daily average pronunciation scores for the last 7 days.
     * Each row is (dayEpoch = completedAtMs / 86400000, avgScore).
     * Used to power the pronunciation trend snippet in the analytics screen.
     */
    @Query(
        """
        SELECT completedAtMs / 86400000 AS dayEpoch, AVG(scorePercent) AS avgScore
        FROM pronunciation_attempts
        WHERE completedAtMs >= :sinceMs
        GROUP BY dayEpoch
        ORDER BY dayEpoch ASC
        """
    )
    fun getDailyAverageScoresSince(sinceMs: Long): Flow<List<DailyPronunciationRow>>

    /** Overall average across all attempts (for the analytics headline stat). */
    @Query("SELECT AVG(scorePercent) FROM pronunciation_attempts")
    fun getOverallAverageScore(): Flow<Float?>
}

/** Projection returned by [PronunciationAttemptDao.getDailyAverageScoresSince]. */
data class DailyPronunciationRow(
    val dayEpoch: Long,
    val avgScore: Float
)
