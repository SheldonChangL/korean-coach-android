package com.koreancoach.app.data.curriculum

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import com.koreancoach.app.data.repository.*
import com.koreancoach.app.domain.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HangulCurriculumLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun loadLessons(): List<Lesson> {
        val raw = runCatching {
            context.assets.open(resolveAssetPath()).bufferedReader().use { it.readText() }
        }.getOrElse {
            context.assets.open(HANGUL_ASSET_PATH_ZH).bufferedReader().use { it.readText() }
        }
        val pack = json.decodeFromString(SerializableHangulLessonPack.serializer(), raw)
        return pack.lessons.map { lesson -> lesson.toDomain(pack.version) }
    }

    private fun resolveAssetPath(): String {
        val appLocale = AppCompatDelegate.getApplicationLocales()[0]
        val configurationLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]
        val languageTag = (appLocale ?: configurationLocale)?.toLanguageTag().orEmpty()
        return if (languageTag.startsWith("zh")) {
            HANGUL_ASSET_PATH_ZH
        } else {
            HANGUL_ASSET_PATH_EN
        }
    }

    private fun SerializableHangulLesson.toDomain(version: String): Lesson = Lesson(
        id = lessonId,
        weekNumber = 0,
        dayNumber = stageOrder,
        title = title,
        subtitle = subtitle,
        emoji = emoji,
        estimatedMinutes = estimatedMinutes,
        isUnlocked = initiallyUnlocked,
        isCompleted = false,
        trackId = trackId,
        lessonKind = LessonKind.HANGUL_STAGE,
        stageOrder = stageOrder,
        learningObjective = learningObjective,
        contentVersion = version,
        vocabulary = vocabulary.map { it.toDomain() },
        phrases = emptyList(),
        pronunciationTips = pronunciationTips.map { it.toDomain() },
        memoryHooks = memoryHooks.map { it.toDomain() },
        scriptItems = scriptItems.map { it.toDomain() },
        writingTargets = writingTargets.map { it.toDomain() },
        readingDrills = readingDrills.map { it.toDomain() },
        dialogueItems = dialogueItems.map { it.toDomain() },
        checkpointItems = checkpointItems.map { it.toDomain() },
        unlockTargetIds = unlockTargetIds
    )

    private fun SerializableVocabItem.toDomain() = VocabItem(
        id = id,
        korean = korean,
        romanization = romanization,
        english = english,
        exampleSentence = exampleSentence,
        exampleTranslation = exampleTranslation,
        memoryHook = memoryHook,
        category = runCatching { VocabCategory.valueOf(category) }.getOrElse { VocabCategory.HANGUL },
        speech = speech.toDomain()
    )

    private fun SerializablePronunciationTip.toDomain() =
        PronunciationTip(sound, description, englishComparison, commonMistake)

    private fun SerializableMemoryHook.toDomain() =
        MemoryHook(targetWord, story, visualDescription)

    private fun SerializableScriptItem.toDomain() = ScriptItem(
        id = id,
        text = text,
        romanization = romanization,
        translation = translation,
        emphasis = emphasis,
        speech = speech.toDomain()
    )

    private fun SerializableWritingTarget.toDomain() = WritingTarget(
        characterId = characterId,
        prompt = prompt,
        speech = speech.toDomain(),
        practiceGroupId = practiceGroupId
    )

    private fun SerializableReadingDrill.toDomain() = ReadingDrill(
        id = id,
        prompt = prompt,
        displayText = displayText,
        romanization = romanization,
        translation = translation,
        speech = speech.toDomain(),
        practiceGroupId = practiceGroupId
    )

    private fun SerializableDialogueItem.toDomain() = DialogueItem(
        id = id,
        title = title,
        lines = lines.map { it.toDomain() },
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

    private fun SerializableCheckpointItem.toDomain() = CheckpointItem(
        id = id,
        prompt = prompt,
        options = options,
        correctAnswer = correctAnswer,
        explanation = explanation,
        speech = speech.toDomain()
    )

    private fun SerializableSpeechSpec.toDomain() = SpeechSpec(
        speechText = speechText,
        speechLocale = speechLocale,
        speechRatePreset = runCatching { SpeechRatePreset.valueOf(speechRatePreset) }.getOrElse { SpeechRatePreset.NORMAL },
        preferredVoiceKey = preferredVoiceKey
    )

    companion object {
        private const val HANGUL_ASSET_PATH_ZH = "curriculum/hangul_sprint_zh_tw.json"
        private const val HANGUL_ASSET_PATH_EN = "curriculum/hangul_sprint_en.json"
    }
}

@Serializable
data class SerializableHangulLessonPack(
    val version: String,
    val lessons: List<SerializableHangulLesson>
)

@Serializable
data class SerializableHangulLesson(
    val lessonId: String,
    val trackId: String,
    val stageOrder: Int,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val estimatedMinutes: Int,
    val learningObjective: String,
    val initiallyUnlocked: Boolean = false,
    val unlockTargetIds: List<String> = emptyList(),
    val vocabulary: List<SerializableVocabItem> = emptyList(),
    val pronunciationTips: List<SerializablePronunciationTip> = emptyList(),
    val memoryHooks: List<SerializableMemoryHook> = emptyList(),
    val scriptItems: List<SerializableScriptItem> = emptyList(),
    val writingTargets: List<SerializableWritingTarget> = emptyList(),
    val readingDrills: List<SerializableReadingDrill> = emptyList(),
    val dialogueItems: List<SerializableDialogueItem> = emptyList(),
    val checkpointItems: List<SerializableCheckpointItem> = emptyList()
)
