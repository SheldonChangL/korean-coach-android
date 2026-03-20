package com.koreancoach.app.di

import android.content.Context
import androidx.room.Room
import com.koreancoach.app.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "korean_coach.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideLessonDao(db: AppDatabase) = db.lessonDao()

    @Provides
    fun provideFlashCardDao(db: AppDatabase) = db.flashCardDao()

    @Provides
    fun provideQuizResultDao(db: AppDatabase) = db.quizResultDao()

    @Provides
    fun provideStudySessionDao(db: AppDatabase) = db.studySessionDao()

    @Provides
    fun providePronunciationAttemptDao(db: AppDatabase) = db.pronunciationAttemptDao()
}
