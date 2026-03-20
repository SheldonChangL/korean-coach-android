package com.koreancoach.app.domain.model

data class QuizQuestion(
    val id: String,
    val lessonId: String,
    val questionText: String,
    val questionType: QuestionType,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String
)

enum class QuestionType {
    KOREAN_TO_ENGLISH, ENGLISH_TO_KOREAN, ROMANIZATION_MATCH, FILL_IN_BLANK
}
