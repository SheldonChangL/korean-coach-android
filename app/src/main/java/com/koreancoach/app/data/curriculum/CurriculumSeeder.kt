package com.koreancoach.app.data.curriculum

import android.content.Context
import com.koreancoach.app.R
import com.koreancoach.app.domain.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurriculumSeeder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val hangulCurriculumLoader: HangulCurriculumLoader
) {
    fun loadAllLessons(): List<Lesson> {
        val hangulLessons = hangulCurriculumLoader.loadLessons()
        val survivalLessons = CurriculumData.getAllLessons().mapIndexed { index, lesson ->
            lesson.copy(
                isUnlocked = false,
                trackId = "survival_korean",
                lessonKind = LessonKind.SURVIVAL,
                stageOrder = 100 + index,
                learningObjective = lesson.subtitle,
                contentVersion = "survival-v3",
                scriptItems = lesson.toScriptItems(),
                readingDrills = lesson.toReadingDrills(),
                dialogueItems = lesson.toDialogueItems(),
                checkpointItems = lesson.toCheckpointItems()
            )
        }
        return hangulLessons + survivalLessons
    }

    fun loadAllFlashCards(): List<FlashCard> =
        loadAllLessons().flatMap { CurriculumData.getFlashCardsForLesson(it) }

    private fun Lesson.toScriptItems(): List<ScriptItem> =
        vocabulary.take(2).map { vocab ->
            ScriptItem(
                id = "script_${id}_${vocab.id}",
                text = vocab.korean,
                romanization = vocab.romanization,
                translation = vocab.english,
                emphasis = vocab.memoryHook,
                speech = vocab.speech
            )
        }

    private fun Lesson.toReadingDrills(): List<ReadingDrill> =
        vocabulary.take(2).map { vocab ->
            ReadingDrill(
                id = "reading_${id}_${vocab.id}",
                prompt = context.getString(R.string.survival_read_and_listen),
                displayText = vocab.korean,
                romanization = vocab.romanization,
                translation = vocab.english,
                speech = vocab.speech
            )
        }

    private fun Lesson.toDialogueItems(): List<DialogueItem> {
        val primary = phrases.firstOrNull()
        if (primary == null) return emptyList()

        val secondLine = phrases.drop(1).firstOrNull()?.let { phrase ->
            DialogueLine(
                id = "dialogue_${id}_line2",
                speaker = context.getString(R.string.survival_speaker_local),
                text = phrase.korean,
                romanization = phrase.romanization,
                translation = phrase.english,
                speech = phrase.speech
            )
        } ?: DialogueLine(
            id = "dialogue_${id}_line2",
            speaker = context.getString(R.string.survival_speaker_local),
            text = "네, 좋아요.",
            romanization = "ne, joayo",
            translation = context.getString(R.string.survival_default_reply_translation),
            speech = SpeechSpec(speechText = "네, 좋아요.")
        )

        return listOf(
            DialogueItem(
                id = "dialogue_$id",
                title = context.getString(R.string.survival_dialogue_title, title),
                lines = listOf(
                    DialogueLine(
                        id = "dialogue_${id}_line1",
                        speaker = context.getString(R.string.survival_speaker_you),
                        text = primary.korean,
                        romanization = primary.romanization,
                        translation = primary.english,
                        speech = primary.speech
                    ),
                    secondLine
                ),
                comprehensionQuestion = context.getString(R.string.survival_dialogue_question),
                comprehensionAnswer = primary.korean
            )
        )
    }

    private fun Lesson.toCheckpointItems(): List<CheckpointItem> {
        val target = vocabulary.firstOrNull() ?: return emptyList()
        return listOf(
            CheckpointItem(
                id = "checkpoint_$id",
                prompt = context.getString(R.string.survival_checkpoint_prompt, target.english),
                options = (vocabulary.take(4).map { it.korean } + target.korean).distinct().take(4),
                correctAnswer = target.korean,
                explanation = context.getString(R.string.survival_checkpoint_explanation, target.korean, target.english),
                speech = target.speech
            )
        )
    }
}
