package com.koreancoach.app.data.repository

import com.koreancoach.app.data.local.dao.DailyPronunciationRow
import com.koreancoach.app.data.local.dao.QuizResultDao
import com.koreancoach.app.data.local.dao.StudySessionDao
import com.koreancoach.app.data.local.entity.StudySessionEntity
import com.koreancoach.app.domain.model.DailyStudyMinutes
import com.koreancoach.app.domain.model.QuizAccuracyPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepository @Inject constructor(
    private val studySessionDao: StudySessionDao,
    private val quizResultDao: QuizResultDao,
    private val pronunciationRepository: PronunciationRepository
) {
    /** Records a completed study session for analytics aggregation. */
    suspend fun recordSession(lessonId: String, durationMinutes: Int) {
        studySessionDao.insertSession(
            StudySessionEntity(
                lessonId = lessonId,
                durationMinutes = durationMinutes,
                completedAtMs = System.currentTimeMillis()
            )
        )
    }

    /** Weekly study minutes — sums sessions for the last 7 days, grouped by day. */
    fun getWeeklyStudyMinutes(): Flow<List<DailyStudyMinutes>> {
        val sevenDaysAgoMs = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L
        return studySessionDao.getDailyMinutesSince(sevenDaysAgoMs).map { rows ->
            rows.map { DailyStudyMinutes(dayEpoch = it.dayEpoch, minutes = it.totalMinutes) }
        }
    }

    /** Total study minutes in the current week (Mon–Sun). */
    fun getWeeklyTotalMinutes(): Flow<Int> {
        val sevenDaysAgoMs = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L
        return studySessionDao.getTotalMinutesSince(sevenDaysAgoMs).map { it ?: 0 }
    }

    /** Last N quiz results as accuracy % points for a trend chart. */
    fun getQuizAccuracyTrend(limit: Int = 10): Flow<List<QuizAccuracyPoint>> =
        quizResultDao.getAllResults().map { results ->
            results.take(limit).reversed().mapIndexed { index, result ->
                QuizAccuracyPoint(
                    index = index,
                    accuracy = if (result.totalQuestions > 0)
                        (result.score.toFloat() / result.totalQuestions * 100).toInt()
                    else 0,
                    completedAtMs = result.completedAtMs
                )
            }
        }

    fun getAverageAccuracy(): Flow<Float?> = quizResultDao.getAverageScore().map { avg ->
        avg?.times(100f)
    }

    /** Daily average pronunciation scores for the last 7 days. */
    fun getWeeklyPronunciationTrend(): Flow<List<DailyPronunciationRow>> =
        pronunciationRepository.getWeeklyPronunciationTrend()

    /** Overall average pronunciation score across all recorded attempts. */
    fun getOverallAveragePronunciationScore(): Flow<Float?> =
        pronunciationRepository.getOverallAverageScore()
}
