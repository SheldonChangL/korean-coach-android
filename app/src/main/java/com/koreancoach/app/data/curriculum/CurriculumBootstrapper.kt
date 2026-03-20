package com.koreancoach.app.data.curriculum

import com.koreancoach.app.data.repository.LessonRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurriculumBootstrapper @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val curriculumSeeder: CurriculumSeeder
) {
    private val seedMutex = Mutex()

    suspend fun ensureSeeded() {
        seedMutex.withLock {
            val existingLessons = lessonRepository.getAllLessons().first()
            val lessons = curriculumSeeder.loadAllLessons()
            val shouldReseed = existingLessons.isEmpty() || shouldReseed(existingLessons, lessons)
            if (!shouldReseed) return

            val existingById = existingLessons.associateBy { it.id }
            val mergedLessons = lessons.map { lesson ->
                existingById[lesson.id]?.let { existing ->
                    lesson.copy(
                        isUnlocked = existing.isUnlocked,
                        isCompleted = existing.isCompleted
                    )
                } ?: lesson
            }
            val flashCards = curriculumSeeder.loadAllFlashCards()
            lessonRepository.seedCurriculum(mergedLessons, flashCards)
        }
    }

    private fun shouldReseed(existingLessons: List<com.koreancoach.app.domain.model.Lesson>, targetLessons: List<com.koreancoach.app.domain.model.Lesson>): Boolean {
        if (existingLessons.size != targetLessons.size) return true

        val existingById = existingLessons.associateBy { it.id }
        return targetLessons.any { lesson ->
            val existing = existingById[lesson.id] ?: return@any true
            existing.contentVersion != lesson.contentVersion ||
                existing.title != lesson.title ||
                existing.subtitle != lesson.subtitle ||
                existing.learningObjective != lesson.learningObjective
        }
    }
}
