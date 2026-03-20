package com.koreancoach.app.data.local.dao

import androidx.room.*
import com.koreancoach.app.data.local.entity.FlashCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashCardDao {
    @Query("SELECT * FROM flashcards")
    suspend fun getAllCards(): List<FlashCardEntity>

    @Query("SELECT * FROM flashcards WHERE lessonId = :lessonId")
    fun getCardsByLesson(lessonId: String): Flow<List<FlashCardEntity>>

    @Query("SELECT * FROM flashcards WHERE nextReviewTimestamp <= :nowMs ORDER BY nextReviewTimestamp LIMIT 20")
    fun getDueCards(nowMs: Long): Flow<List<FlashCardEntity>>

    @Query("SELECT COUNT(*) FROM flashcards WHERE nextReviewTimestamp <= :nowMs")
    fun getDueCount(nowMs: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM flashcards WHERE reviewState = 'MASTERED'")
    fun getMasteredCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<FlashCardEntity>)

    @Update
    suspend fun updateCard(card: FlashCardEntity)
}
