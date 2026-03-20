package com.koreancoach.app.domain.pronunciation

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Pure Kotlin evaluator that compares ASR-recognised text against the target
 * Korean phrase to produce a 0–100 score with actionable feedback.
 *
 * Scoring algorithm (no external dependencies):
 *  1. Exact match → 97
 *  2. Token-overlap score  (40 % weight) — full-word matches in recognised output
 *  3. Edit-distance score  (60 % weight) — Levenshtein similarity on Korean chars
 *  4. Romanization bonus   (secondary)  — if recognised text looks like romanization,
 *     compare against targetRomanization and take the better score
 *
 * The class is free of Android APIs so it can be tested on the JVM without
 * any instrumented test runner.
 */
@Singleton
class PronunciationEvaluator @Inject constructor() {

    /**
     * Evaluate how closely [recognizedText] matches the [targetKorean] phrase.
     *
     * @param targetKorean       The correct Korean string (e.g. "안녕하세요").
     * @param targetRomanization Romanized equivalent (e.g. "annyeonghaseyo").
     * @param recognizedText     What the ASR engine heard.
     */
    fun evaluate(
        targetKorean: String,
        targetRomanization: String,
        recognizedText: String
    ): PronunciationEvaluation {
        if (recognizedText.isBlank()) {
            return PronunciationEvaluation(
                scorePercent = 0,
                feedback = "다시 해봐요! Couldn't hear anything — try again in a quieter place.",
                phonemeIssues = listOf("no audio detected")
            )
        }

        val normTarget = targetKorean.trim().lowercase()
        val normRecognized = recognizedText.trim().lowercase()

        // Fast path: exact match
        if (normRecognized == normTarget) {
            return PronunciationEvaluation(
                scorePercent = 97,
                feedback = "완벽해요! Perfect match — excellent pronunciation!",
                phonemeIssues = emptyList()
            )
        }

        // Token overlap (favours partial word matches)
        val targetTokens = tokenize(normTarget)
        val recognizedTokens = tokenize(normRecognized)
        val tokenScore = tokenOverlapScore(targetTokens, recognizedTokens)

        // Character-level edit distance on Korean
        val editScore = editDistanceScore(normTarget, normRecognized)

        // Combined Korean score
        val koreanScore = (tokenScore * 0.4 + editScore * 0.6).toInt()

        // Romanization secondary check (in case ASR returned Latin characters)
        val romanScore = if (targetRomanization.isNotBlank()) {
            editDistanceScore(targetRomanization.trim().lowercase(), normRecognized)
        } else 0

        // Take the better of Korean or romanization match, then scale
        val baseScore = maxOf(koreanScore, (romanScore * 0.75).toInt()).coerceIn(5, 97)

        val issues = detectIssues(targetKorean, recognizedText, baseScore)
        val feedback = feedbackForScore(baseScore)

        return PronunciationEvaluation(
            scorePercent = baseScore,
            feedback = feedback,
            phonemeIssues = issues
        )
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private fun tokenize(text: String): List<String> =
        text.split(Regex("\\s+")).filter { it.isNotBlank() }

    private fun tokenOverlapScore(target: List<String>, recognized: List<String>): Int {
        if (target.isEmpty()) return 100
        val matched = target.count { it in recognized }
        return (matched.toFloat() / target.size * 100).toInt()
    }

    private fun editDistanceScore(a: String, b: String): Int {
        val dist = levenshtein(a, b)
        val maxLen = maxOf(a.length, b.length, 1)
        val similarity = 1.0 - (dist.toDouble() / maxLen)
        return (similarity * 100).toInt().coerceIn(0, 100)
    }

    /**
     * Standard Levenshtein distance between two strings.
     * O(m × n) time, O(min(m,n)) space (two-row rolling array).
     */
    internal fun levenshtein(a: String, b: String): Int {
        if (a == b) return 0
        if (a.isEmpty()) return b.length
        if (b.isEmpty()) return a.length

        val m = a.length
        val n = b.length

        // Keep only two rows to save memory
        var prev = IntArray(n + 1) { it }
        val curr = IntArray(n + 1)

        for (i in 1..m) {
            curr[0] = i
            for (j in 1..n) {
                curr[j] = if (a[i - 1] == b[j - 1]) {
                    prev[j - 1]
                } else {
                    1 + minOf(prev[j], curr[j - 1], prev[j - 1])
                }
            }
            prev = curr.copyOf()
        }
        return prev[n]
    }

    private fun detectIssues(target: String, recognized: String, score: Int): List<String> {
        val issues = mutableListOf<String>()
        if (score < 85) {
            // Final consonant (받침) present in target but not detected?
            if (hasReceivingConsonant(target) && score < 75) {
                issues.add("final consonant (받침)")
            }
            if (score < 65) {
                issues.add("vowel clarity")
            }
            if (score < 45) {
                issues.add("intonation pattern")
            }
        }
        return issues
    }

    /**
     * Returns true if any Hangul syllable in [text] carries a final consonant (받침).
     * Hangul syllables in Unicode are encoded as: code = 0xAC00 + (onset×21 + vowel)×28 + coda.
     * If (code - 0xAC00) % 28 != 0, a coda (받침) is present.
     */
    internal fun hasReceivingConsonant(text: String): Boolean =
        text.any { ch -> ch.code in 0xAC00..0xD7A3 && (ch.code - 0xAC00) % 28 != 0 }

    private fun feedbackForScore(score: Int): String = when {
        score >= 90 -> "훌륭해요! Excellent — very natural pronunciation!"
        score >= 75 -> "잘했어요! Good — keep refining the rhythm."
        score >= 60 -> "괜찮아요! Getting there — focus on vowel sounds."
        score >= 40 -> "다시 해봐요! Try again — listen to the target carefully."
        else        -> "천천히! Slow down and try once more."
    }
}

/** Return value from [PronunciationEvaluator.evaluate]. */
data class PronunciationEvaluation(
    val scorePercent: Int,
    val feedback: String,
    val phonemeIssues: List<String>
)
