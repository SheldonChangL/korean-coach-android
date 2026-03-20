package com.koreancoach.app.di

import com.koreancoach.app.data.speech.AndroidSpeechPlaybackService
import com.koreancoach.app.domain.speech.SpeechPlaybackService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SpeechModule {
    @Binds
    @Singleton
    abstract fun bindSpeechPlaybackService(
        impl: AndroidSpeechPlaybackService
    ): SpeechPlaybackService
}
