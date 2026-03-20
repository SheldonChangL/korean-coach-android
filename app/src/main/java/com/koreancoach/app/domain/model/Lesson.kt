package com.koreancoach.app.domain.model

data class Lesson(
    val id: String,
    val weekNumber: Int,
    val dayNumber: Int,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val estimatedMinutes: Int,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val trackId: String = "survival_korean",
    val lessonKind: LessonKind = LessonKind.SURVIVAL,
    val stageOrder: Int = weekNumber * 10 + dayNumber,
    val learningObjective: String = "",
    val contentVersion: String = "legacy-v2",
    val vocabulary: List<VocabItem>,
    val phrases: List<PhraseItem>,
    val pronunciationTips: List<PronunciationTip>,
    val memoryHooks: List<MemoryHook>,
    val scriptItems: List<ScriptItem> = emptyList(),
    val writingTargets: List<WritingTarget> = emptyList(),
    val readingDrills: List<ReadingDrill> = emptyList(),
    val dialogueItems: List<DialogueItem> = emptyList(),
    val checkpointItems: List<CheckpointItem> = emptyList(),
    val unlockTargetIds: List<String> = emptyList()
)

data class VocabItem(
    val id: String,
    val korean: String,
    val romanization: String,
    val english: String,
    val exampleSentence: String,
    val exampleTranslation: String,
    val memoryHook: String,
    val category: VocabCategory,
    val speech: SpeechSpec = SpeechSpec()
)

data class PhraseItem(
    val id: String,
    val korean: String,
    val romanization: String,
    val english: String,
    val context: String,
    val usageTip: String,
    val speech: SpeechSpec = SpeechSpec()
)

data class PronunciationTip(
    val sound: String,
    val description: String,
    val englishComparison: String,
    val commonMistake: String
)

data class MemoryHook(
    val targetWord: String,
    val story: String,
    val visualDescription: String
)

enum class VocabCategory {
    HANGUL, GREETING, FOOD, TRANSPORT, SHOPPING, NUMBER, TIME, GENERAL,
    WEATHER, FAMILY, FEELINGS
}
