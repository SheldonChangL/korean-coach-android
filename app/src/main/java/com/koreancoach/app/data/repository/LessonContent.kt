package com.koreancoach.app.data.repository

import kotlinx.serialization.Serializable

@Serializable
data class LessonContent(
    val vocabulary: List<SerializableVocabItem> = emptyList(),
    val phrases: List<SerializablePhraseItem> = emptyList(),
    val pronunciationTips: List<SerializablePronunciationTip> = emptyList(),
    val memoryHooks: List<SerializableMemoryHook> = emptyList()
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
    val category: String = "GENERAL"
)

@Serializable
data class SerializablePhraseItem(
    val id: String = "",
    val korean: String = "",
    val romanization: String = "",
    val english: String = "",
    val context: String = "",
    val usageTip: String = ""
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
