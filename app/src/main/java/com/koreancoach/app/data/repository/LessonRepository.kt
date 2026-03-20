package com.koreancoach.app.data.repository

import com.koreancoach.app.data.local.dao.FlashCardDao
import com.koreancoach.app.data.local.dao.LessonDao
import com.koreancoach.app.data.local.entity.FlashCardEntity
import com.koreancoach.app.data.local.entity.LessonEntity
import com.koreancoach.app.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRepository @Inject constructor(
    private val lessonDao: LessonDao,
    private val flashCardDao: FlashCardDao
) {
    fun getAllLessons(): Flow<List<Lesson>> =
        lessonDao.getAllLessons().map { it.map { e -> e.toDomain() } }

    fun getLessonsByWeek(week: Int): Flow<List<Lesson>> =
        lessonDao.getLessonsByWeek(week).map { it.map { e -> e.toDomain() } }

    suspend fun getLessonById(id: String): Lesson? =
        lessonDao.getLessonById(id)?.toDomain()

    suspend fun markLessonComplete(id: String) = lessonDao.markLessonComplete(id)

    suspend fun unlockLesson(id: String) = lessonDao.unlockLesson(id)

    /**
     * Marks [lessonId] complete and unlocks the next sequential lesson.
     * Lessons are ordered by weekNumber then dayNumber; the next one in that sequence is unlocked.
     */
    suspend fun completeAndUnlockNext(lessonId: String) {
        lessonDao.markLessonComplete(lessonId)
        val allLessons = lessonDao.getAllLessons().first()
            .sortedWith(compareBy({ it.weekNumber }, { it.dayNumber }))
        val idx = allLessons.indexOfFirst { it.id == lessonId }
        if (idx >= 0 && idx + 1 < allLessons.size) {
            lessonDao.unlockLesson(allLessons[idx + 1].id)
        }
    }

    fun getCompletedLessonCount(): Flow<Int> = lessonDao.getCompletedLessonCount()

    suspend fun seedCurriculum(lessons: List<Lesson>, cards: List<FlashCard>) {
        lessonDao.insertLessons(lessons.map { it.toEntity() })
        flashCardDao.insertCards(cards.map { it.toFlashCardEntity() })
    }

    private fun LessonEntity.toDomain(): Lesson {
        val content = runCatching { Json.decodeFromString<LessonContent>(contentJson) }.getOrElse { LessonContent() }
        return Lesson(
            id = id, weekNumber = weekNumber, dayNumber = dayNumber,
            title = title, subtitle = subtitle, emoji = emoji,
            estimatedMinutes = estimatedMinutes,
            isUnlocked = isUnlocked, isCompleted = isCompleted,
            vocabulary = content.vocabulary.map { it.toDomain() },
            phrases = content.phrases.map { it.toDomain() },
            pronunciationTips = content.pronunciationTips.map { it.toDomain() },
            memoryHooks = content.memoryHooks.map { it.toDomain() }
        )
    }

    private fun Lesson.toEntity(): LessonEntity {
        val content = LessonContent(
            vocabulary = vocabulary.map { it.toSerializable() },
            phrases = phrases.map { it.toSerializable() },
            pronunciationTips = pronunciationTips.map { it.toSerializable() },
            memoryHooks = memoryHooks.map { it.toSerializable() }
        )
        return LessonEntity(
            id = id, weekNumber = weekNumber, dayNumber = dayNumber,
            title = title, subtitle = subtitle, emoji = emoji,
            estimatedMinutes = estimatedMinutes,
            isUnlocked = isUnlocked, isCompleted = isCompleted,
            contentJson = Json.encodeToString(content)
        )
    }

    private fun FlashCard.toFlashCardEntity() = FlashCardEntity(
        id = id, lessonId = lessonId,
        front = front, frontSubtext = frontSubtext,
        back = back, backSubtext = backSubtext,
        memoryHook = memoryHook, reviewState = reviewState.name,
        nextReviewTimestamp = 0L, easeFactor = 2.5f, intervalDays = 1
    )

    private fun SerializableVocabItem.toDomain() = VocabItem(
        id = id, korean = korean, romanization = romanization, english = english,
        exampleSentence = exampleSentence, exampleTranslation = exampleTranslation,
        memoryHook = memoryHook, category = runCatching { VocabCategory.valueOf(category) }.getOrElse { VocabCategory.GENERAL }
    )
    private fun VocabItem.toSerializable() = SerializableVocabItem(
        id = id, korean = korean, romanization = romanization, english = english,
        exampleSentence = exampleSentence, exampleTranslation = exampleTranslation,
        memoryHook = memoryHook, category = category.name
    )
    private fun SerializablePhraseItem.toDomain() = PhraseItem(id, korean, romanization, english, context, usageTip)
    private fun PhraseItem.toSerializable() = SerializablePhraseItem(id, korean, romanization, english, context, usageTip)
    private fun SerializablePronunciationTip.toDomain() = PronunciationTip(sound, description, englishComparison, commonMistake)
    private fun PronunciationTip.toSerializable() = SerializablePronunciationTip(sound, description, englishComparison, commonMistake)
    private fun SerializableMemoryHook.toDomain() = MemoryHook(targetWord, story, visualDescription)
    private fun MemoryHook.toSerializable() = SerializableMemoryHook(targetWord, story, visualDescription)
}
