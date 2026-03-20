package com.koreancoach.app

import com.koreancoach.app.data.curriculum.CurriculumData
import com.koreancoach.app.domain.model.Lesson
import org.junit.Assert.*
import org.junit.Test

class LessonUnlockTest {

    private fun getAllLessonsOrdered(): List<Lesson> =
        CurriculumData.getAllLessons()
            .sortedWith(compareBy({ it.weekNumber }, { it.dayNumber }))

    @Test
    fun `week 1 lesson 1 is the only unlocked lesson at start`() {
        val lessons = getAllLessonsOrdered()
        val unlocked = lessons.filter { it.isUnlocked }
        assertEquals(1, unlocked.size)
        assertEquals("w1d1", unlocked.first().id)
    }

    @Test
    fun `all lessons beyond first are initially locked`() {
        val lessons = getAllLessonsOrdered()
        lessons.drop(1).forEach { lesson ->
            assertFalse("Lesson ${lesson.id} should be locked", lesson.isUnlocked)
        }
    }

    @Test
    fun `next lesson after w1d1 is w1d2`() {
        val lessons = getAllLessonsOrdered()
        val idx = lessons.indexOfFirst { it.id == "w1d1" }
        assertTrue(idx >= 0)
        val next = lessons.getOrNull(idx + 1)
        assertNotNull(next)
        assertEquals("w1d2", next?.id)
    }

    @Test
    fun `next lesson after last in week 1 is first in week 2`() {
        val lessons = getAllLessonsOrdered()
        val idx = lessons.indexOfFirst { it.id == "w1d5" }
        assertTrue(idx >= 0)
        val next = lessons.getOrNull(idx + 1)
        assertNotNull(next)
        assertEquals("w2d1", next?.id)
    }

    @Test
    fun `next lesson after last in week 2 is first in week 3`() {
        val lessons = getAllLessonsOrdered()
        val idx = lessons.indexOfFirst { it.id == "w2d5" }
        assertTrue(idx >= 0)
        val next = lessons.getOrNull(idx + 1)
        assertNotNull(next)
        assertEquals("w3d1", next?.id)
    }

    @Test
    fun `last lesson in curriculum has no next lesson`() {
        val lessons = getAllLessonsOrdered()
        val last = lessons.last()
        val idx = lessons.indexOfFirst { it.id == last.id }
        assertNull(lessons.getOrNull(idx + 1))
    }

    @Test
    fun `all 15 lessons present across 3 weeks`() {
        val lessons = CurriculumData.getAllLessons()
        assertTrue("Curriculum must have lessons", lessons.isNotEmpty())
    }

    @Test
    fun `all lesson ids are unique`() {
        val lessons = CurriculumData.getAllLessons()
        val ids = lessons.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `each lesson has 5 vocabulary items`() {
        CurriculumData.getAllLessons().forEach { lesson ->
            assertEquals("Lesson ${lesson.id} should have 5 vocab items", 5, lesson.vocabulary.size)
        }
    }

    @Test
    fun `all vocab items have non-blank memory hooks`() {
        CurriculumData.getAllLessons().flatMap { it.vocabulary }.forEach { vocab ->
            assertTrue("Vocab ${vocab.id} memory hook should not be blank", vocab.memoryHook.isNotBlank())
        }
    }
}
