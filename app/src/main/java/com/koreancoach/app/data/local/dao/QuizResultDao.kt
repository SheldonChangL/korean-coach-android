package com.koreancoach.app.data.local.dao

import androidx.room.*
import com.koreancoach.app.data.local.entity.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {
    @Insert
    suspend fun insertResult(result: QuizResultEntity)

    @Query("SELECT * FROM quiz_results ORDER BY completedAtMs DESC")
    fun getAllResults(): Flow<List<QuizResultEntity>>

    @Query("SELECT AVG(CAST(score AS FLOAT)/totalQuestions) FROM quiz_results")
    fun getAverageScore(): Flow<Float?>
}
