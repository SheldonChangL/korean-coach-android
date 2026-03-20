package com.koreancoach.app.data.local.dao

import androidx.room.*
import com.koreancoach.app.data.local.entity.LessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons ORDER BY stageOrder")
    fun getAllLessons(): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE weekNumber = :week ORDER BY stageOrder")
    fun getLessonsByWeek(week: Int): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE trackId = :trackId ORDER BY stageOrder")
    fun getLessonsByTrack(trackId: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE id = :id")
    suspend fun getLessonById(id: String): LessonEntity?

    @Query("SELECT * FROM lessons WHERE id = :id")
    fun observeLessonById(id: String): Flow<LessonEntity?>

    @Query("SELECT * FROM lessons WHERE isCompleted = 0 AND isUnlocked = 1 ORDER BY stageOrder LIMIT 1")
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
