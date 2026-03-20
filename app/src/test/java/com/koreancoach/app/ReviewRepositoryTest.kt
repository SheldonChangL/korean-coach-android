package com.koreancoach.app

import app.cash.turbine.test
import com.koreancoach.app.data.local.dao.FlashCardDao
import com.koreancoach.app.data.local.entity.FlashCardEntity
import com.koreancoach.app.data.repository.ReviewRepository
import com.koreancoach.app.domain.model.FlashCard
import com.koreancoach.app.domain.model.ReviewState
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ReviewRepositoryTest {

    private lateinit var flashCardDao: FlashCardDao
    private lateinit var repository: ReviewRepository

    @Before
    fun setup() {
        flashCardDao = mockk()
        repository = ReviewRepository(flashCardDao)
    }

    @Test
    fun `getDueCards maps entities to domain models`() = runTest {
        val entity = FlashCardEntity(
            id = "test1", lessonId = "w1d1",
            front = "안녕하세요", frontSubtext = "annyeonghaseyo",
            back = "Hello", backSubtext = "Think: On young ha-say-yo",
            memoryHook = "On young ha-say-yo",
            reviewState = "NEW",
            nextReviewTimestamp = 0L,
            easeFactor = 2.5f, intervalDays = 1
        )
        every { flashCardDao.getDueCards(any()) } returns flowOf(listOf(entity))

        repository.getDueCards(System.currentTimeMillis()).test {
            val cards = awaitItem()
            assertEquals(1, cards.size)
            assertEquals("안녕하세요", cards.first().front)
            assertEquals(ReviewState.NEW, cards.first().reviewState)
            awaitComplete()
        }
    }

    @Test
    fun `recordReview quality below 3 keeps card in LEARNING`() = runTest {
        val slot = slot<FlashCardEntity>()
        coEvery { flashCardDao.updateCard(capture(slot)) } just Runs
        every { flashCardDao.getDueCards(any()) } returns flowOf(emptyList())

        val card = FlashCard(
            id = "test2", lessonId = "w1d1",
            front = "감사합니다", frontSubtext = "gamsahamnida",
            back = "Thank you", backSubtext = "",
            memoryHook = "", reviewState = ReviewState.NEW
        )

        repository.recordReview(card, 2)

        val updated = slot.captured
        assertEquals("LEARNING", updated.reviewState)
    }
}
