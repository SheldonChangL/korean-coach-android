package com.koreancoach.app.domain.model

data class FlashCard(
    val id: String,
    val lessonId: String,
    val front: String,
    val frontSubtext: String,
    val back: String,
    val backSubtext: String,
    val memoryHook: String,
    val reviewState: ReviewState
)

enum class ReviewState {
    NEW, LEARNING, REVIEW, MASTERED
}
