package com.koreancoach.app.domain.pronunciation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * Real on-device pronunciation scorer backed by Android's [SpeechRecognizer].
 *
 * Recognition flow:
 *  1. Checks that ASR is available on the device.
 *  2. Starts a Korean-language recognition session (no internet flag where possible).
 *  3. Passes the recognised text to [PronunciationEvaluator] to produce a score.
 *  4. Falls back to [FakePronunciationScorer] if ASR is unavailable or fails.
 *
 * Extensibility: To swap in a cloud ASR provider, replace the [RecognizerIntent]
 * action or point `EXTRA_LANGUAGE` at a different endpoint — the evaluator and
 * repository layers remain unchanged.
 */
@Singleton
class RealPronunciationScorer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val evaluator: PronunciationEvaluator,
    private val fallback: FakePronunciationScorer
) : PronunciationScorer {

    /** SpeechRecognizer manages its own audio capture, so we skip AudioRecord. */
    override val usesOwnRecordingPipeline: Boolean = true

    override suspend fun score(
        targetText: String,
        targetRomanization: String,
        audioBytes: ByteArray,
        durationMs: Long
    ): PronunciationResult {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            return fallback.score(targetText, targetRomanization, audioBytes, durationMs)
                .copy(scoringSource = ScoringSource.FAKE)
        }

        val recognized = runCatching { recognizeSpeech() }.getOrNull()
            ?: return fallback.score(targetText, targetRomanization, audioBytes, durationMs)
                .copy(scoringSource = ScoringSource.FAKE)

        val evaluation = evaluator.evaluate(
            targetKorean = targetText,
            targetRomanization = targetRomanization,
            recognizedText = recognized
        )

        return PronunciationResult(
            scorePercent = evaluation.scorePercent,
            feedback = evaluation.feedback,
            phonemeIssues = evaluation.phonemeIssues,
            recognizedText = recognized,
            scoringSource = ScoringSource.REAL
        )
    }

    /**
     * Launches SpeechRecognizer on the main thread (required by Android) and
     * suspends until a result or error is received.
     *
     * Returns the best recognised string, or null on any error.
     */
    private suspend fun recognizeSpeech(): String? = withContext(Dispatchers.Main) {
        suspendCancellableCoroutine { continuation ->
            val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                // Allow silence detection to end the session automatically
                putExtra(
                    RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                    1500L
                )
            }

            recognizer.setRecognitionListener(object : RecognitionListener {
                override fun onResults(results: Bundle?) {
                    val matches =
                        results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    recognizer.destroy()
                    if (continuation.isActive) continuation.resume(matches?.firstOrNull())
                }

                override fun onError(error: Int) {
                    recognizer.destroy()
                    if (continuation.isActive) continuation.resume(null)
                }

                // Unused callbacks — must be implemented per the interface
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })

            recognizer.startListening(intent)

            continuation.invokeOnCancellation {
                recognizer.cancel()
                recognizer.destroy()
            }
        }
    }
}
