package com.koreancoach.app

import android.content.Context
import android.speech.SpeechRecognizer
import com.koreancoach.app.domain.pronunciation.FakePronunciationScorer
import com.koreancoach.app.domain.pronunciation.PronunciationEvaluator
import com.koreancoach.app.domain.pronunciation.RealPronunciationScorer
import com.koreancoach.app.domain.pronunciation.ScoringSource
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RealPronunciationScorerTest {

    private lateinit var context: Context
    private lateinit var evaluator: PronunciationEvaluator
    private lateinit var fakeFallback: FakePronunciationScorer

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        evaluator = PronunciationEvaluator()
        fakeFallback = FakePronunciationScorer()
    }

    @Test
    fun `falls back to FakePronunciationScorer when ASR is unavailable`() = runBlocking {
        mockkStatic(SpeechRecognizer::class)
        every { SpeechRecognizer.isRecognitionAvailable(any()) } returns false

        val scorer = RealPronunciationScorer(context, evaluator, fakeFallback)
        val result = scorer.score(
            targetText = "안녕하세요",
            targetRomanization = "annyeonghaseyo",
            audioBytes = ByteArray(0),
            durationMs = 1000L
        )

        assertNotNull("Result must not be null", result)
        assertEquals(ScoringSource.FAKE, result.scoringSource)
        unmockkStatic(SpeechRecognizer::class)
    }

    @Test
    fun `usesOwnRecordingPipeline is true for RealPronunciationScorer`() {
        val scorer = RealPronunciationScorer(context, evaluator, fakeFallback)
        assertTrue(scorer.usesOwnRecordingPipeline)
    }
}
