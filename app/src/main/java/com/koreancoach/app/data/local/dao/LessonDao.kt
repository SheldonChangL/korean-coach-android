package com.koreancoach.app.data.local.dao

import androidx.room.*
import com.koreancoach.app.data.local.entity.LessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons ORDER BY weekNumber, dayNumber")
    fun getAllLessons(): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE weekNumber = :week ORDER BY dayNumber")
    fun getLessonsByWeek(week: Int): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE id = :id")
    suspend fun getLessonById(id: String): LessonEntity?

    @Query("SELECT * FROM lessons WHERE isCompleted = 0 AND isUnlocked = 1 LIMIT 1")
    suspend fun getNextLesson(): LessonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Query("UPDATE lessons SET isCompleted = 1 WHERE id = :id")
    suspend fun markLessonComplete(id: String)

    @Query("UPDATE lessons SET isUnlocked = 1 WHERE id = :id")
    suspend fun unlockLesson(id: String)

    @Query("SELECT COUNT(*) FROM lessons WHERE isCompleted = 1")
    fun getCompletedLessonCount(): Flow<Int>
}
