package com.koreancoach.app

import com.koreancoach.app.data.curriculum.CurriculumData
import org.junit.Assert.*
import org.junit.Test

class Sprint3CurriculumTest {

    @Test
    fun `week 4 has 5 lessons`() {
        assertEquals(5, CurriculumData.getWeek4Lessons().size)
    }

    @Test
    fun `week 5 has 5 lessons`() {
        assertEquals(5, CurriculumData.getWeek5Lessons().size)
    }

    @Test
    fun `all lessons now total 25`() {
        assertEquals(25, CurriculumData.getAllLessons().size)
    }

    @Test
    fun `week 4 lessons all belong to week 4`() {
        CurriculumData.getWeek4Lessons().forEach { lesson ->
            assertEquals("${lesson.id} must be week 4", 4, lesson.weekNumber)
        }
    }

    @Test
    fun `week 5 lessons all belong to week 5`() {
        CurriculumData.getWeek5Lessons().forEach { lesson ->
            assertEquals("${lesson.id} must be week 5", 5, lesson.weekNumber)
        }
    }
}
