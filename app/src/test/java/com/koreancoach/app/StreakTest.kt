package com.koreancoach.app

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class StreakTest {

    private fun computeStreak(currentStreak: Int, lastStudyMs: Long, nowMs: Long): Int {
        if (lastStudyMs == 0L) return 1
        val dayMs = 24 * 60 * 60 * 1000L
        val todayStart = (nowMs / dayMs) * dayMs
        val yesterdayStart = todayStart - dayMs
        val lastDayStart = (lastStudyMs / dayMs) * dayMs
        return when {
            lastDayStart == todayStart -> currentStreak       
            lastDayStart == yesterdayStart -> currentStreak + 1 
            else -> 1                                          
        }
    }

    @Test
    fun `first ever study gives streak of 1`() {
        val result = computeStreak(currentStreak = 0, lastStudyMs = 0L, nowMs = System.currentTimeMillis())
        assertEquals(1, result)
    }

    @Test
    fun `studying consecutive days increments streak`() {
        val dayMs = 24 * 60 * 60 * 1000L
        val nowMs = System.currentTimeMillis()
        val todayStart = (nowMs / dayMs) * dayMs
        val yesterdayMs = todayStart - dayMs + 3600_000L 
        val result = computeStreak(currentStreak = 3, lastStudyMs = yesterdayMs, nowMs = nowMs)
        assertEquals(4, result)
    }
}
