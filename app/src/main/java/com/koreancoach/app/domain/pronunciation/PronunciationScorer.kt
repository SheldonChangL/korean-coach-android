package com.koreancoach.app.domain.pronunciation

/** Identifies whether a result came from real ASR or the deterministic fake. */
enum class ScoringSource { FAKE, REAL }

/**
 * Interface for pronunciation scoring. Implementations may use on-device ASR
 * (e.g. Android SpeechRecognizer), a cloud speech-to-text API, or a fake
 * implementation for offline development and CI.
 *
 * To add a new ASR back-end: implement this interface and rebind it in
 * [com.koreancoach.app.di.PronunciationModule] — nothing else needs to change.
 */
interface PronunciationScorer {
    /**
     * Score a pronunciation attempt.
     * @param targetText        The Korean text the learner should have said.
     * @param targetRomanization Romanized equivalent (used as secondary match signal).
     * @param audioBytes        Raw PCM audio captured from the microphone.
     * @param durationMs        Duration of the recording in milliseconds.
     * @return A [PronunciationResult] with a 0–100 score, feedback, and recognised text.
     */
    suspend fun score(
        targetText: String,
        targetRomanization: String = "",
        audioBytes: ByteArray,
        durationMs: Long
    ): PronunciationResult

    /**
     * True when this scorer handles its own audio capture (e.g. SpeechRecognizer).
     * The ViewModel uses this to skip the AudioRecord recording loop and instead
     * show a simple "listening…" animation while the scorer runs.
     */
    val usesOwnRecordingPipeline: Boolean get() = false
}

data class PronunciationResult(
    /** 0–100 where 100 is perfect. */
    val scorePercent: Int,
    /** Short human-readable feedback sentence. */
    val feedback: String,
    /** Detected phoneme issues, if any (empty list = none found). */
    val phonemeIssues: List<String> = emptyList(),
    /** The text the ASR engine actually heard (empty for fake scorer). */
    val recognizedText: String = "",
    /** Whether the score came from real ASR or the deterministic fake. */
    val scoringSource: ScoringSource = ScoringSource.FAKE
)
