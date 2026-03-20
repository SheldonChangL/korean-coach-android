package com.koreancoach.app.data.speech

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.koreancoach.app.domain.model.SpeechRatePreset
import com.koreancoach.app.domain.model.SpeechVoiceOption
import com.koreancoach.app.domain.speech.SpeechPlaybackService
import com.koreancoach.app.domain.speech.SpeechPlaybackState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidSpeechPlaybackService @Inject constructor(
    @ApplicationContext context: Context
) : SpeechPlaybackService {

    private val _state = MutableStateFlow(SpeechPlaybackState())
    override val state: StateFlow<SpeechPlaybackState> = _state.asStateFlow()
    private lateinit var tts: TextToSpeech

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _state.value = _state.value.copy(isSpeaking = true, lastError = null)
                    }

                    override fun onDone(utteranceId: String?) {
                        _state.value = _state.value.copy(isSpeaking = false)
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        _state.value = _state.value.copy(
                            isSpeaking = false,
                            lastError = "Speech playback failed."
                        )
                    }

                    override fun onError(utteranceId: String?, errorCode: Int) {
                        _state.value = _state.value.copy(
                            isSpeaking = false,
                            lastError = "Speech playback failed ($errorCode)."
                        )
                    }
                })
                _state.value = SpeechPlaybackState(
                    isReady = true,
                    voices = getAvailableVoices("ko-KR")
                )
            } else {
                _state.value = SpeechPlaybackState(
                    isReady = false,
                    lastError = "Text-to-speech is unavailable on this device."
                )
            }
        }
    }

    override fun speak(
        text: String,
        localeTag: String,
        ratePreset: SpeechRatePreset,
        preferredVoiceKey: String
    ) {
        if (!::tts.isInitialized || !_state.value.isReady) {
            _state.value = _state.value.copy(lastError = "Text-to-speech is still loading.")
            return
        }

        val locale = Locale.forLanguageTag(localeTag)
        if (!isLanguageReady(localeTag)) {
            _state.value = _state.value.copy(lastError = "No supported voice found for $localeTag.")
            return
        }

        val voice = getAvailableVoices(localeTag).firstOrNull { it.key == preferredVoiceKey }
        if (voice != null) {
            tts.voices.firstOrNull { it.name == voice.key }?.let { tts.voice = it }
        } else {
            tts.setLanguage(locale)
        }

        tts.setSpeechRate(ratePreset.ttsRate)
        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UUID.randomUUID().toString())
        }
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, UUID.randomUUID().toString())
    }

    override fun stop() {
        if (!::tts.isInitialized) return
        tts.stop()
        _state.value = _state.value.copy(isSpeaking = false)
    }

    override fun isLanguageReady(localeTag: String): Boolean {
        if (!::tts.isInitialized) return false
        val locale = Locale.forLanguageTag(localeTag)
        return tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE
    }

    override fun getAvailableVoices(localeTag: String): List<SpeechVoiceOption> {
        if (!::tts.isInitialized) return emptyList()
        val requested = Locale.forLanguageTag(localeTag)
        return tts.voices
            ?.filter { voice ->
                val locale = voice.locale ?: return@filter false
                locale.language == requested.language
            }
            ?.sortedBy { it.name }
            ?.map { voice ->
                SpeechVoiceOption(
                    key = voice.name,
                    label = voice.name,
                    localeTag = voice.locale?.toLanguageTag().orEmpty()
                )
            }
            .orEmpty()
    }

    override fun shutdown() {
        if (!::tts.isInitialized) return
        tts.stop()
        tts.shutdown()
        _state.value = _state.value.copy(isReady = false, isSpeaking = false)
    }
}
