package com.koreancoach.app

import com.koreancoach.app.data.curriculum.CurriculumData
import com.koreancoach.app.domain.model.VocabCategory
import org.junit.Assert.*
import org.junit.Test

class CurriculumDataTest {

    @Test
    fun `week 1 has 5 lessons`() {
        val lessons = CurriculumData.getWeek1Lessons()
        assertEquals(5, lessons.size)
    }

    @Test
    fun `first lesson is unlocked, rest are locked`() {
        val lessons = CurriculumData.getWeek1Lessons()
        assertTrue("First lesson must be unlocked", lessons.first().isUnlocked)
        lessons.drop(1).forEach { lesson ->
            assertFalse("Lesson ${lesson.id} should be locked", lesson.isUnlocked)
        }
    }

    @Test
    fun `all lessons have estimated minutes above zero`() {
        CurriculumData.getAllLessons().forEach { lesson ->
            assertTrue("Lesson ${lesson.id} estimated minutes should be > 0", lesson.estimatedMinutes > 0)
        }
    }

    @Test
    fun `all vocab items have Korean text`() {
        CurriculumData.getAllLessons().forEach { lesson ->
            lesson.vocabulary.forEach { vocab ->
                assertTrue("Vocab ${vocab.id} must have Korean text", vocab.korean.isNotBlank())
                assertTrue("Vocab ${vocab.id} must have English text", vocab.english.isNotBlank())
                assertTrue("Vocab ${vocab.id} must have romanization", vocab.romanization.isNotBlank())
            }
        }
    }

    @Test
    fun `getAllFlashCards returns cards for all lessons`() {
        val cards = CurriculumData.getAllFlashCards()
        assertTrue("Should have at least 20 flash cards", cards.size >= 20)
    }

    @Test
    fun `flash card IDs are unique`() {
        val cards = CurriculumData.getAllFlashCards()
        val ids = cards.map { it.id }
        assertEquals("All flash card IDs must be unique", ids.size, ids.toSet().size)
    }

    @Test
    fun `lesson 1 contains greeting vocabulary`() {
        val lesson = CurriculumData.getWeek1Lessons().first()
        val greetings = lesson.vocabulary.filter { it.category == VocabCategory.GREETING }
        assertTrue("Week 1 Day 1 must have greeting vocab", greetings.isNotEmpty())
    }

    @Test
    fun `all phrases have context and usage tips`() {
        CurriculumData.getAllLessons().forEach { lesson ->
            lesson.phrases.forEach { phrase ->
                assertTrue("Phrase ${phrase.id} must have context", phrase.context.isNotBlank())
                assertTrue("Phrase ${phrase.id} must have usage tip", phrase.usageTip.isNotBlank())
            }
        }
    }
}
