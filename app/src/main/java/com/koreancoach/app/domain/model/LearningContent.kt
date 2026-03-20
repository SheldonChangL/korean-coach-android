package com.koreancoach.app.domain.model

import androidx.annotation.StringRes
import com.koreancoach.app.R

const val DEFAULT_SPEECH_LOCALE = "ko-KR"

enum class LessonKind {
    HANGUL_STAGE,
    SURVIVAL
}

enum class SpeechRatePreset(val ttsRate: Float, @StringRes val labelRes: Int) {
    SLOW(0.72f, R.string.speed_slow),
    NORMAL(1.0f, R.string.speed_normal)
}

data class SpeechSpec(
    val speechText: String = "",
    val speechLocale: String = DEFAULT_SPEECH_LOCALE,
    val speechRatePreset: SpeechRatePreset = SpeechRatePreset.NORMAL,
    val preferredVoiceKey: String = ""
) {
    fun resolvedText(fallback: String): String = speechText.ifBlank { fallback }
}

data class ScriptItem(
    val id: String,
    val text: String,
    val romanization: String,
    val translation: String,
    val emphasis: String = "",
    val speech: SpeechSpec = SpeechSpec()
)

data class WritingTarget(
    val characterId: String,
    val prompt: String,
    val speech: SpeechSpec = SpeechSpec()
)

data class ReadingDrill(
    val id: String,
    val prompt: String,
    val displayText: String,
    val romanization: String,
    val translation: String,
    val speech: SpeechSpec = SpeechSpec()
)

data class DialogueItem(
    val id: String,
    val title: String,
    val lines: List<DialogueLine>,
    val comprehensionQuestion: String = "",
    val comprehensionAnswer: String = ""
)

data class DialogueLine(
    val id: String,
    val speaker: String,
    val text: String,
    val romanization: String,
    val translation: String,
    val speech: SpeechSpec = SpeechSpec()
)

data class CheckpointItem(
    val id: String,
    val prompt: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String,
    val speech: SpeechSpec = SpeechSpec()
)

data class SpeechVoiceOption(
    val key: String,
    val label: String,
    val localeTag: String
)
