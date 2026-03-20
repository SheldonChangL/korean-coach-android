package com.koreancoach.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.koreancoach.app.data.local.dao.FlashCardDao
import com.koreancoach.app.data.local.dao.LessonDao
import com.koreancoach.app.data.local.dao.PronunciationAttemptDao
import com.koreancoach.app.data.local.dao.QuizResultDao
import com.koreancoach.app.data.local.dao.StudySessionDao
import com.koreancoach.app.data.local.entity.FlashCardEntity
import com.koreancoach.app.data.local.entity.LessonEntity
import com.koreancoach.app.data.local.entity.PronunciationAttemptEntity
import com.koreancoach.app.data.local.entity.QuizResultEntity
import com.koreancoach.app.data.local.entity.StudySessionEntity

@Database(
    entities = [
        LessonEntity::class,
        FlashCardEntity::class,
        QuizResultEntity::class,
        StudySessionEntity::class,
        PronunciationAttemptEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lessonDao(): LessonDao
    abstract fun flashCardDao(): FlashCardDao
    abstract fun quizResultDao(): QuizResultDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun pronunciationAttemptDao(): PronunciationAttemptDao
}
