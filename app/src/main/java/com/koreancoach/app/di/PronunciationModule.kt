package com.koreancoach.app.di

import com.koreancoach.app.domain.pronunciation.PronunciationScorer
import com.koreancoach.app.domain.pronunciation.RealPronunciationScorer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds [RealPronunciationScorer] as the production [PronunciationScorer].
 *
 * RealPronunciationScorer automatically falls back to FakePronunciationScorer
 * when Android SpeechRecognizer is unavailable (e.g. emulators without Google
 * Play Services or CI environments).
 *
 * To switch to a cloud ASR provider: create a new PronunciationScorer
 * implementation and change the binding here — no other code needs to change.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class PronunciationModule {

    @Binds
    @Singleton
    abstract fun bindPronunciationScorer(
        real: RealPronunciationScorer
    ): PronunciationScorer
}
