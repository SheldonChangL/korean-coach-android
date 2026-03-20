package com.koreancoach.app.data.repository

import kotlinx.serialization.Serializable

@Serializable
data class LessonContent(
    val vocabulary: List<SerializableVocabItem> = emptyList(),
    val phrases: List<SerializablePhraseItem> = emptyList(),
    val pronunciationTips: List<SerializablePronunciationTip> = emptyList(),
    val memoryHooks: List<SerializableMemoryHook> = emptyList(),
    val scriptItems: List<SerializableScriptItem> = emptyList(),
    val writingTargets: List<SerializableWritingTarget> = emptyList(),
    val readingDrills: List<SerializableReadingDrill> = emptyList(),
    val dialogueItems: List<SerializableDialogueItem> = emptyList(),
    val checkpointItems: List<SerializableCheckpointItem> = emptyList(),
    val unlockTargetIds: List<String> = emptyList()
)

@Serializable
data class SerializableVocabItem(
    val id: String = "",
    val korean: String = "",
    val romanization: String = "",
    val english: String = "",
    val exampleSentence: String = "",
    val exampleTranslation: String = "",
    val memoryHook: String = "",
    val category: String = "GENERAL",
    val speech: SerializableSpeechSpec = SerializableSpeechSpec()
)

@Serializable
data class SerializablePhraseItem(
    val id: String = "",
    val korean: String = "",
    val romanization: String = "",
    val english: String = "",
    val context: String = "",
    val usageTip: String = "",
    val speech: SerializableSpeechSpec = SerializableSpeechSpec()
)

@Serializable
data class SerializablePronunciationTip(
    val sound: String = "",
    val description: String = "",
    val englishComparison: String = "",
    val commonMistake: String = ""
)

@Serializable
data class SerializableMemoryHook(
    val targetWord: String = "",
    val story: String = "",
    val visualDescription: String = ""
)

@Serializable
data class SerializableSpeechSpec(
    val speechText: String = "",
    val speechLocale: String = "ko-KR",
    val speechRatePreset: String = "NORMAL",
    val preferredVoiceKey: String = ""
)

@Serializable
data class SerializableScriptItem(
    val id: String = "",
    val text: String = "",
    val romanization: String = "",
    val translation: String = "",
    val emphasis: String = "",
    val speech: SerializableSpeechSpec = SerializableSpeechSpec()
)

@Serializable
data class SerializableWritingTarget(
    val characterId: String = "",
    val prompt: String = "",
    val speech: SerializableSpeechSpec = SerializableSpeechSpec()
)

@Serializable
data class SerializableReadingDrill(
    val id: String = "",
    val prompt: String = "",
    val displayText: String = "",
    val romanization: String = "",
    val translation: String = "",
    val speech: SerializableSpeechSpec = SerializableSpeechSpec()
)

@Serializable
data class SerializableDialogueItem(
    val id: String = "",
    val title: String = "",
    val lines: List<SerializableDialogueLine> = emptyList(),
    val comprehensionQuestion: String = "",
    val comprehensionAnswer: String = ""
)

@Serializable
data class SerializableDialogueLine(
    val id: String = "",
    val speaker: String = "",
    val text: String = "",
    val romanization: String = "",
    val translation: String = "",
    val speech: SerializableSpeechSpec = SerializableSpeechSpec()
)

@Serializable
data class SerializableCheckpointItem(
    val id: String = "",
    val prompt: String = "",
    val options: List<String> = emptyList(),
    val correctAnswer: String = "",
    val explanation: String = "",
    val speech: SerializableSpeechSpec = SerializableSpeechSpec()
)
