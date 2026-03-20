package com.koreancoach.app

import com.koreancoach.app.domain.pronunciation.PronunciationEvaluator
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PronunciationEvaluatorTest {

    private lateinit var evaluator: PronunciationEvaluator

    @Before
    fun setUp() {
        evaluator = PronunciationEvaluator()
    }

    @Test
    fun `score is within 0-100 for any input`() {
        val cases = listOf(
            Triple("안녕하세요", "annyeonghaseyo", "안녕하세요"),
            Triple("감사합니다", "gamsahamnida", ""),
            Triple("네", "ne", "아니요"),
            Triple("사랑해", "saranghae", "completely wrong text here"),
        )
        for ((korean, roman, recognized) in cases) {
            val result = evaluator.evaluate(korean, roman, recognized)
            assertTrue(
                "Score out of range for ($korean, $recognized): ${result.scorePercent}",
                result.scorePercent in 0..100
            )
        }
    }

    @Test
    fun `exact match returns score of 97`() {
        val result = evaluator.evaluate("안녕하세요", "annyeonghaseyo", "안녕하세요")
        assertEquals(97, result.scorePercent)
        assertTrue("Feedback must not be blank", result.feedback.isNotBlank())
        assertTrue("No phoneme issues for perfect match", result.phonemeIssues.isEmpty())
    }

    @Test
    fun `blank recognised text returns score 0 with feedback`() {
        val result = evaluator.evaluate("안녕하세요", "annyeonghaseyo", "")
        assertEquals(0, result.scorePercent)
        assertTrue("Feedback must not be blank", result.feedback.isNotBlank())
        assertTrue("Should report no audio detected", result.phonemeIssues.isNotEmpty())
    }

    @Test
    fun `similar text scores higher than completely different text`() {
        val similar = evaluator.evaluate("안녕하세요", "annyeonghaseyo", "안녕")
        val different = evaluator.evaluate("안녕하세요", "annyeonghaseyo", "완전히 다른 텍스트")
        assertTrue(
            "Similar text should score higher: similar=${similar.scorePercent}, different=${different.scorePercent}",
            similar.scorePercent > different.scorePercent
        )
    }

    @Test
    fun `levenshtein returns 0 for identical strings`() {
        assertEquals(0, evaluator.levenshtein("hello", "hello"))
    }

    @Test
    fun `hasReceivingConsonant returns true for syllables with batchim`() {
        assertTrue(evaluator.hasReceivingConsonant("닭"))
        assertTrue(evaluator.hasReceivingConsonant("밥"))
    }

    @Test
    fun `hasReceivingConsonant returns false for open syllables`() {
        assertFalse(evaluator.hasReceivingConsonant("가나다"))
    }
}
