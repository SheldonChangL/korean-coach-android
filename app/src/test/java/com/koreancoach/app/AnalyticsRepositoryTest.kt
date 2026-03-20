package com.koreancoach.app

import app.cash.turbine.test
import com.koreancoach.app.data.local.dao.DailyMinutesRow
import com.koreancoach.app.data.local.dao.DailyPronunciationRow
import com.koreancoach.app.data.local.dao.QuizResultDao
import com.koreancoach.app.data.local.dao.StudySessionDao
import com.koreancoach.app.data.local.entity.QuizResultEntity
import com.koreancoach.app.data.local.entity.StudySessionEntity
import com.koreancoach.app.data.repository.AnalyticsRepository
import com.koreancoach.app.data.repository.PronunciationRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AnalyticsRepositoryTest {

    private lateinit var studySessionDao: StudySessionDao
    private lateinit var quizResultDao: QuizResultDao
    private lateinit var pronunciationRepository: PronunciationRepository
    private lateinit var repository: AnalyticsRepository

    @Before
    fun setUp() {
        studySessionDao = mockk(relaxed = true)
        quizResultDao = mockk(relaxed = true)
        pronunciationRepository = mockk(relaxed = true)
        every { pronunciationRepository.getWeeklyPronunciationTrend() } returns flowOf(emptyList())
        every { pronunciationRepository.getOverallAverageScore() } returns flowOf(null)
        repository = AnalyticsRepository(studySessionDao, quizResultDao, pronunciationRepository)
    }

    @Test
    fun `recordSession inserts study session entity`() = runTest {
        repository.recordSession("w1d1", durationMinutes = 10)
        coVerify { studySessionDao.insertSession(match { it.lessonId == "w1d1" && it.durationMinutes == 10 }) }
    }

    @Test
    fun `getWeeklyStudyMinutes maps dao rows to domain model`() = runTest {
        val rows = listOf(
            DailyMinutesRow(dayEpoch = 19800L, totalMinutes = 15),
            DailyMinutesRow(dayEpoch = 19801L, totalMinutes = 20)
        )
        every { studySessionDao.getDailyMinutesSince(any()) } returns flowOf(rows)

        repository.getWeeklyStudyMinutes().test {
            val result = awaitItem()
            assertEquals(2, result.size)
            assertEquals(15, result[0].minutes)
            assertEquals(20, result[1].minutes)
            awaitComplete()
        }
    }

    @Test
    fun `getWeeklyTotalMinutes returns summed minutes`() = runTest {
        every { studySessionDao.getTotalMinutesSince(any()) } returns flowOf(45)

        repository.getWeeklyTotalMinutes().test {
            assertEquals(45, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getWeeklyTotalMinutes returns 0 when no sessions`() = runTest {
        every { studySessionDao.getTotalMinutesSince(any()) } returns flowOf(null)

        repository.getWeeklyTotalMinutes().test {
            assertEquals(0, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getQuizAccuracyTrend computes correct percentage`() = runTest {
        val results = listOf(
            QuizResultEntity(id = 1, lessonId = "w1d1", score = 8, totalQuestions = 10, completedAtMs = 1000L),
            QuizResultEntity(id = 2, lessonId = "w1d2", score = 5, totalQuestions = 10, completedAtMs = 2000L)
        )
        every { quizResultDao.getAllResults() } returns flowOf(results)

        repository.getQuizAccuracyTrend().test {
            val trend = awaitItem()
            assertEquals(2, trend.size)
            // Results are reversed so oldest first: result with id=2 at index 0, id=1 at index 1
            assertEquals(50, trend[0].accuracy) // 5/10 = 50%
            assertEquals(80, trend[1].accuracy) // 8/10 = 80%
            awaitComplete()
        }
    }

    @Test
    fun `getAverageAccuracy returns percentage scaled to 0-100`() = runTest {
        every { quizResultDao.getAverageScore() } returns flowOf(0.75f)

        repository.getAverageAccuracy().test {
            val avg = awaitItem()
            assertNotNull(avg)
            assertEquals(75f, avg!!, 0.01f)
            awaitComplete()
        }
    }

    @Test
    fun `getAverageAccuracy returns null when no results`() = runTest {
        every { quizResultDao.getAverageScore() } returns flowOf(null)

        repository.getAverageAccuracy().test {
            assertNull(awaitItem())
            awaitComplete()
        }
    }
}
