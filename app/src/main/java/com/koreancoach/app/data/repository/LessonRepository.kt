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

    fun getLessonsByTrack(trackId: String): Flow<List<Lesson>> =
        lessonDao.getLessonsByTrack(trackId).map { it.map { entity -> entity.toDomain() } }

    fun observeLessonById(id: String): Flow<Lesson?> =
        lessonDao.observeLessonById(id).map { it?.toDomain() }

    suspend fun getLessonById(id: String): Lesson? =
        lessonDao.getLessonById(id)?.toDomain()

    suspend fun markLessonComplete(id: String) = lessonDao.markLessonComplete(id)

    suspend fun unlockLesson(id: String) = lessonDao.unlockLesson(id)

    /**
     * Marks [lessonId] complete and unlocks the next sequential lesson.
     * Lessons are ordered by weekNumber then dayNumber; the next one in that sequence is unlocked.
     */
    suspend fun completeAndUnlockNext(lessonId: String) {
        val lesson = lessonDao.getLessonById(lessonId)?.toDomain() ?: return
        lessonDao.markLessonComplete(lessonId)
        if (lesson.unlockTargetIds.isNotEmpty()) {
            lesson.unlockTargetIds.forEach { lessonDao.unlockLesson(it) }
            return
        }

        val allLessons = lessonDao.getAllLessons().first().sortedBy { it.stageOrder }
        val idx = allLessons.indexOfFirst { it.id == lessonId }
        if (idx >= 0 && idx + 1 < allLessons.size) {
            lessonDao.unlockLesson(allLessons[idx + 1].id)
        }
    }

    fun getCompletedLessonCount(): Flow<Int> = lessonDao.getCompletedLessonCount()

    suspend fun seedCurriculum(lessons: List<Lesson>, cards: List<FlashCard>) {
        lessonDao.insertLessons(lessons.map { it.toEntity() })
        val existingCards = flashCardDao.getAllCards().associateBy { it.id }
        flashCardDao.insertCards(
            cards.map { card ->
                val entity = card.toFlashCardEntity()
                existingCards[entity.id]?.let { existing ->
                    entity.copy(
                        reviewState = existing.reviewState,
                        nextReviewTimestamp = existing.nextReviewTimestamp,
                        easeFactor = existing.easeFactor,
                        intervalDays = existing.intervalDays
                    )
                } ?: entity
            }
        )
    }

    private fun LessonEntity.toDomain(): Lesson {
        val content = runCatching { Json.decodeFromString<LessonContent>(contentJson) }.getOrElse { LessonContent() }
        return Lesson(
            id = id, weekNumber = weekNumber, dayNumber = dayNumber,
            title = title, subtitle = subtitle, emoji = emoji,
            estimatedMinutes = estimatedMinutes,
            isUnlocked = isUnlocked, isCompleted = isCompleted,
            trackId = trackId,
            lessonKind = runCatching { LessonKind.valueOf(lessonKind) }.getOrElse { LessonKind.SURVIVAL },
            stageOrder = stageOrder,
            learningObjective = learningObjective,
            contentVersion = contentVersion,
            vocabulary = content.vocabulary.map { it.toDomain() },
            phrases = content.phrases.map { it.toDomain() },
            pronunciationTips = content.pronunciationTips.map { it.toDomain() },
            memoryHooks = content.memoryHooks.map { it.toDomain() },
            scriptItems = content.scriptItems.map { it.toDomain() },
            writingTargets = content.writingTargets.map { it.toDomain() },
            readingDrills = content.readingDrills.map { it.toDomain() },
            dialogueItems = content.dialogueItems.map { it.toDomain() },
            checkpointItems = content.checkpointItems.map { it.toDomain() },
            unlockTargetIds = content.unlockTargetIds
        )
    }

    private fun Lesson.toEntity(): LessonEntity {
        val content = LessonContent(
            vocabulary = vocabulary.map { it.toSerializable() },
            phrases = phrases.map { it.toSerializable() },
            pronunciationTips = pronunciationTips.map { it.toSerializable() },
            memoryHooks = memoryHooks.map { it.toSerializable() },
            scriptItems = scriptItems.map { it.toSerializable() },
            writingTargets = writingTargets.map { it.toSerializable() },
            readingDrills = readingDrills.map { it.toSerializable() },
            dialogueItems = dialogueItems.map { it.toSerializable() },
            checkpointItems = checkpointItems.map { it.toSerializable() },
            unlockTargetIds = unlockTargetIds
        )
        return LessonEntity(
            id = id, weekNumber = weekNumber, dayNumber = dayNumber,
            trackId = trackId,
            lessonKind = lessonKind.name,
            stageOrder = stageOrder,
            title = title, subtitle = subtitle, emoji = emoji,
            estimatedMinutes = estimatedMinutes,
            isUnlocked = isUnlocked, isCompleted = isCompleted,
            learningObjective = learningObjective,
            contentVersion = contentVersion,
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
        memoryHook = memoryHook,
        category = runCatching { VocabCategory.valueOf(category) }.getOrElse { VocabCategory.GENERAL },
        speech = speech.toDomain()
    )
    private fun VocabItem.toSerializable() = SerializableVocabItem(
        id = id, korean = korean, romanization = romanization, english = english,
        exampleSentence = exampleSentence, exampleTranslation = exampleTranslation,
        memoryHook = memoryHook, category = category.name, speech = speech.toSerializable()
    )
    private fun SerializablePhraseItem.toDomain() = PhraseItem(id, korean, romanization, english, context, usageTip, speech.toDomain())
    private fun PhraseItem.toSerializable() = SerializablePhraseItem(id, korean, romanization, english, context, usageTip, speech.toSerializable())
    private fun SerializablePronunciationTip.toDomain() = PronunciationTip(sound, description, englishComparison, commonMistake)
    private fun PronunciationTip.toSerializable() = SerializablePronunciationTip(sound, description, englishComparison, commonMistake)
    private fun SerializableMemoryHook.toDomain() = MemoryHook(targetWord, story, visualDescription)
    private fun MemoryHook.toSerializable() = SerializableMemoryHook(targetWord, story, visualDescription)

    private fun SerializableScriptItem.toDomain() = ScriptItem(
        id = id,
        text = text,
        romanization = romanization,
        translation = translation,
        emphasis = emphasis,
        speech = speech.toDomain()
    )

    private fun ScriptItem.toSerializable() = SerializableScriptItem(
        id = id,
        text = text,
        romanization = romanization,
        translation = translation,
        emphasis = emphasis,
        speech = speech.toSerializable()
    )

    private fun SerializableWritingTarget.toDomain() = WritingTarget(
        characterId = characterId,
        prompt = prompt,
        speech = speech.toDomain()
    )

    private fun WritingTarget.toSerializable() = SerializableWritingTarget(
        characterId = characterId,
        prompt = prompt,
        speech = speech.toSerializable()
    )

    private fun SerializableReadingDrill.toDomain() = ReadingDrill(
        id = id,
        prompt = prompt,
        displayText = displayText,
        romanization = romanization,
        translation = translation,
        speech = speech.toDomain()
    )

    private fun ReadingDrill.toSerializable() = SerializableReadingDrill(
        id = id,
        prompt = prompt,
        displayText = displayText,
        romanization = romanization,
        translation = translation,
        speech = speech.toSerializable()
    )

    private fun SerializableDialogueItem.toDomain() = DialogueItem(
        id = id,
        title = title,
        lines = lines.map { it.toDomain() },
        comprehensionQuestion = comprehensionQuestion,
        comprehensionAnswer = comprehensionAnswer
    )

    private fun DialogueItem.toSerializable() = SerializableDialogueItem(
        id = id,
        title = title,
        lines = lines.map { it.toSerializable() },
        comprehensionQuestion = comprehensionQuestion,
        comprehensionAnswer = comprehensionAnswer
    )

    private fun SerializableDialogueLine.toDomain() = DialogueLine(
        id = id,
        speaker = speaker,
        text = text,
        romanization = romanization,
        translation = translation,
        speech = speech.toDomain()
    )

    private fun DialogueLine.toSerializable() = SerializableDialogueLine(
        id = id,
        speaker = speaker,
        text = text,
        romanization = romanization,
        translation = translation,
        speech = speech.toSerializable()
    )

    private fun SerializableCheckpointItem.toDomain() = CheckpointItem(
        id = id,
        prompt = prompt,
        options = options,
        correctAnswer = correctAnswer,
        explanation = explanation,
        speech = speech.toDomain()
    )

    private fun CheckpointItem.toSerializable() = SerializableCheckpointItem(
        id = id,
        prompt = prompt,
        options = options,
        correctAnswer = correctAnswer,
        explanation = explanation,
        speech = speech.toSerializable()
    )

    private fun SerializableSpeechSpec.toDomain() = SpeechSpec(
        speechText = speechText,
        speechLocale = speechLocale,
        speechRatePreset = runCatching { SpeechRatePreset.valueOf(speechRatePreset) }.getOrElse { SpeechRatePreset.NORMAL },
        preferredVoiceKey = preferredVoiceKey
    )

    private fun SpeechSpec.toSerializable() = SerializableSpeechSpec(
        speechText = speechText,
        speechLocale = speechLocale,
        speechRatePreset = speechRatePreset.name,
        preferredVoiceKey = preferredVoiceKey
    )
}
