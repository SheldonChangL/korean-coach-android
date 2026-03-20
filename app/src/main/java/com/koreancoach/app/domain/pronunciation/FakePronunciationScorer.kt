package com.koreancoach.app.domain.pronunciation

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Deterministic-ish fake scorer used while full ASR is not yet integrated.
 * Score is derived from recording duration (rewarding longer attempts) with
 * light randomness to keep the UX realistic. Replace with a real
 * implementation by binding a different class in the DI module.
 */
@Singleton
class FakePronunciationScorer @Inject constructor() : PronunciationScorer {

    override suspend fun score(
        targetText: String,
        targetRomanization: String,
        audioBytes: ByteArray,
        durationMs: Long
    ): PronunciationResult {
        // Seed with target length so same word always gives similar base score
        val base = 55 + (targetText.length % 20)
        val durationBonus = (durationMs / 500L).toInt().coerceAtMost(15)
        val noise = Random.nextInt(-5, 10)
        val score = (base + durationBonus + noise).coerceIn(40, 98)

        val (feedback, issues) = feedbackForScore(score)
        return PronunciationResult(
            scorePercent = score,
            feedback = feedback,
            phonemeIssues = issues,
            recognizedText = "",
            scoringSource = ScoringSource.FAKE
        )
    }

    private fun feedbackForScore(score: Int): Pair<String, List<String>> = when {
        score >= 85 -> "훌륭해요! Excellent — very natural!" to emptyList()
        score >= 70 -> "잘했어요! Good job — keep practising the rhythm." to emptyList()
        score >= 55 -> "괜찮아요! Decent — focus on vowel length." to listOf("vowel length")
        else        -> "다시 해봐요! Try again — speak a bit slower." to listOf("vowel length", "consonant clarity")
    }
}
