package com.koreancoach.app.data.repository

import com.koreancoach.app.data.local.dao.FlashCardDao
import com.koreancoach.app.data.local.entity.FlashCardEntity
import com.koreancoach.app.domain.model.FlashCard
import com.koreancoach.app.domain.model.ReviewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max
import kotlin.math.roundToInt

@Singleton
class ReviewRepository @Inject constructor(
    private val flashCardDao: FlashCardDao
) {
    fun getDueCards(nowMs: Long): Flow<List<FlashCard>> =
        flashCardDao.getDueCards(nowMs).map { it.map { e -> e.toDomain() } }

    fun getDueCount(nowMs: Long): Flow<Int> = flashCardDao.getDueCount(nowMs)

    fun getMasteredCount(): Flow<Int> = flashCardDao.getMasteredCount()

    fun getCardsByLesson(lessonId: String): Flow<List<FlashCard>> =
        flashCardDao.getCardsByLesson(lessonId).map { it.map { e -> e.toDomain() } }

    /** SM-2 simplified: quality 0-5. 0-2 = wrong, 3-5 = correct */
    suspend fun recordReview(card: FlashCard, quality: Int) {
        val nowMs = System.currentTimeMillis()
        val (newInterval, newEase, newState) = computeNextReview(
            intervalDays = 1, easeFactor = 2.5f,
            reviewState = card.reviewState.name, quality = quality
        )
        flashCardDao.updateCard(
            FlashCardEntity(
                id = card.id, lessonId = card.lessonId,
                front = card.front, frontSubtext = card.frontSubtext,
                back = card.back, backSubtext = card.backSubtext,
                memoryHook = card.memoryHook, reviewState = newState,
                nextReviewTimestamp = nowMs + newInterval * 24 * 60 * 60 * 1000L,
                easeFactor = newEase, intervalDays = newInterval
            )
        )
    }

    private fun computeNextReview(
        intervalDays: Int, easeFactor: Float,
        reviewState: String, quality: Int
    ): Triple<Int, Float, String> {
        return if (quality < 3) {
            Triple(1, max(1.3f, easeFactor - 0.2f), "LEARNING")
        } else {
            val newEase = max(1.3f, easeFactor + 0.1f - (5 - quality) * (0.08f + (5 - quality) * 0.02f))
            val newInterval = when {
                intervalDays <= 1 -> 4
                intervalDays <= 4 -> 8
                else -> (intervalDays * newEase).roundToInt()
            }
            val newState = if (newInterval >= 21) "MASTERED" else "REVIEW"
            Triple(newInterval, newEase, newState)
        }
    }

    private fun FlashCardEntity.toDomain() = FlashCard(
        id = id, lessonId = lessonId,
        front = front, frontSubtext = frontSubtext,
        back = back, backSubtext = backSubtext,
        memoryHook = memoryHook,
        reviewState = runCatching { ReviewState.valueOf(reviewState) }.getOrElse { ReviewState.NEW }
    )
}
