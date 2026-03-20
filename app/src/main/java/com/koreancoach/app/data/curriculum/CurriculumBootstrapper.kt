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
            if (existingLessons.isNotEmpty()) return

            val lessons = curriculumSeeder.loadAllLessons()
            val flashCards = curriculumSeeder.loadAllFlashCards()
            lessonRepository.seedCurriculum(lessons, flashCards)
        }
    }
}
