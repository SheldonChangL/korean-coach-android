package com.koreancoach.app

import com.koreancoach.app.domain.pronunciation.FakePronunciationScorer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class FakePronunciationScorerTest {

    private val scorer = FakePronunciationScorer()

    @Test
    fun `score returns value in 0-100 range`() = runBlocking {
        val result = scorer.score("안녕하세요", audioBytes = ByteArray(0), durationMs = 1000L)
        assertTrue("Score must be ≥ 0", result.scorePercent >= 0)
        assertTrue("Score must be ≤ 100", result.scorePercent <= 100)
    }

    @Test
    fun `score returns non-blank feedback`() = runBlocking {
        val result = scorer.score("감사합니다", audioBytes = ByteArray(0), durationMs = 2000L)
        assertTrue("Feedback must not be blank", result.feedback.isNotBlank())
    }

    @Test
    fun `longer recording earns a higher base score than very short`() = runBlocking {
        var longerWon = 0
        repeat(20) {
            val short = scorer.score("네", audioBytes = ByteArray(0), durationMs = 100L)
            val long = scorer.score("네", audioBytes = ByteArray(0), durationMs = 5000L)
            if (long.scorePercent > short.scorePercent) longerWon++
        }
        assertTrue("Longer recording should tend to score higher ($longerWon/20)", longerWon >= 10)
    }

    @Test
    fun `high score produces no phoneme issues`() = runBlocking {
        repeat(30) {
            val result = scorer.score("가나다라마", audioBytes = ByteArray(0), durationMs = 8000L)
            if (result.scorePercent >= 85) {
                assertTrue("High score should have no phoneme issues", result.phonemeIssues.isEmpty())
            }
        }
    }

    @Test
    fun `score is deterministic-ish for same inputs`() = runBlocking {
        val scores = (1..5).map {
            scorer.score("사랑해", audioBytes = ByteArray(0), durationMs = 2000L).scorePercent
        }
        val range = scores.max() - scores.min()
        assertTrue("Score variance for same input should be <30 (got range $range)", range < 30)
    }
}
