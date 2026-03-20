package com.koreancoach.app.data.repository

import com.koreancoach.app.data.local.dao.DailyPronunciationRow
import com.koreancoach.app.data.local.dao.PronunciationAttemptDao
import com.koreancoach.app.data.local.entity.PronunciationAttemptEntity
import com.koreancoach.app.domain.pronunciation.PronunciationResult
import com.koreancoach.app.domain.pronunciation.PronunciationScorer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PronunciationRepository @Inject constructor(
    private val dao: PronunciationAttemptDao,
    private val scorer: PronunciationScorer      // bound to RealPronunciationScorer via DI
) {

    /** Whether the active scorer uses its own recording pipeline (e.g. SpeechRecognizer). */
    val scorerUsesOwnRecordingPipeline: Boolean
        get() = scorer.usesOwnRecordingPipeline

    /** Score a pronunciation attempt and persist the result. */
    suspend fun scoreAndSave(
        characterId: String,
        targetText: String,
        targetRomanization: String = "",
        audioBytes: ByteArray,
        durationMs: Long
    ): PronunciationResult {
        val result = scorer.score(targetText, targetRomanization, audioBytes, durationMs)
        dao.insert(
            PronunciationAttemptEntity(
                characterId = characterId,
                targetText = targetText,
                recognizedText = result.recognizedText,
                scorePercent = result.scorePercent,
                durationMs = durationMs,
                scoringSource = result.scoringSource.name
            )
        )
        return result
    }

    fun getAverageScore(characterId: String): Flow<Float?> =
        dao.getAverageScore(characterId)

    fun getTotalAttemptCount(): Flow<Int> =
        dao.getTotalAttemptCount()

    fun getRecentAttempts(limit: Int = 20) =
        dao.getRecentAttempts(limit)

    /** Daily average pronunciation scores for the last 7 days (for analytics trend). */
    fun getWeeklyPronunciationTrend(): Flow<List<DailyPronunciationRow>> {
        val sevenDaysAgoMs = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L
        return dao.getDailyAverageScoresSince(sevenDaysAgoMs)
    }

    /** Overall average pronunciation score across all attempts. */
    fun getOverallAverageScore(): Flow<Float?> = dao.getOverallAverageScore()
}
