package com.koreancoach.app.domain.speech

import com.koreancoach.app.domain.model.SpeechRatePreset
import com.koreancoach.app.domain.model.SpeechSpec
import com.koreancoach.app.domain.model.SpeechVoiceOption
import kotlinx.coroutines.flow.StateFlow

data class SpeechPlaybackState(
    val isReady: Boolean = false,
    val isSpeaking: Boolean = false,
    val lastError: String? = null,
    val voices: List<SpeechVoiceOption> = emptyList()
)

interface SpeechPlaybackService {
    val state: StateFlow<SpeechPlaybackState>

    fun speak(
        text: String,
        localeTag: String,
        ratePreset: SpeechRatePreset = SpeechRatePreset.NORMAL,
        preferredVoiceKey: String = ""
    )

    fun speak(spec: SpeechSpec, fallbackText: String) =
        speak(
            text = spec.resolvedText(fallbackText),
            localeTag = spec.speechLocale,
            ratePreset = spec.speechRatePreset,
            preferredVoiceKey = spec.preferredVoiceKey
        )

    fun stop()

    fun isLanguageReady(localeTag: String): Boolean

    fun getAvailableVoices(localeTag: String): List<SpeechVoiceOption>

    fun shutdown()
}
