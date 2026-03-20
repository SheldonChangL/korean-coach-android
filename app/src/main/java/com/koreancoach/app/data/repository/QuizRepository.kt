package com.koreancoach.app.data.repository

import com.koreancoach.app.data.local.dao.QuizResultDao
import com.koreancoach.app.data.local.entity.QuizResultEntity
import com.koreancoach.app.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val quizResultDao: QuizResultDao,
    private val lessonRepository: LessonRepository
) {
    suspend fun saveResult(lessonId: String, score: Int, total: Int) {
        quizResultDao.insertResult(
            QuizResultEntity(
                lessonId = lessonId, score = score,
                totalQuestions = total, completedAtMs = System.currentTimeMillis()
            )
        )
    }

    fun getAverageScore(): Flow<Float?> = quizResultDao.getAverageScore()

    suspend fun generateQuiz(lessonId: String): List<QuizQuestion> {
        val lesson = lessonRepository.getLessonById(lessonId) ?: return emptyList()
        val questions = mutableListOf<QuizQuestion>()
        val allVocab = lesson.vocabulary

        lesson.vocabulary.forEachIndexed { idx, vocab ->
            val wrongOptions = allVocab.filter { it.id != vocab.id }.shuffled().take(3).map { it.english }
            val options = (wrongOptions + vocab.english).shuffled()
            questions.add(
                QuizQuestion(
                    id = "q_${lessonId}_vocab_$idx",
                    lessonId = lessonId,
                    questionText = "What does \"${vocab.korean}\" (${vocab.romanization}) mean?",
                    questionType = QuestionType.KOREAN_TO_ENGLISH,
                    options = options,
                    correctOptionIndex = options.indexOf(vocab.english),
                    explanation = "${vocab.korean} = ${vocab.english}. ${vocab.memoryHook}"
                )
            )
        }

        lesson.phrases.take(3).forEachIndexed { idx, phrase ->
            val wrongOptions = lesson.phrases.filter { it.id != phrase.id }.shuffled().take(3).map { it.english }
            val options = (wrongOptions + phrase.english).shuffled()
            questions.add(
                QuizQuestion(
                    id = "q_${lessonId}_phrase_$idx",
                    lessonId = lessonId,
                    questionText = "\"${phrase.korean}\" means:",
                    questionType = QuestionType.KOREAN_TO_ENGLISH,
                    options = options,
                    correctOptionIndex = options.indexOf(phrase.english),
                    explanation = "${phrase.context}. ${phrase.usageTip}"
                )
            )
        }

        return questions.shuffled().take(8)
    }
}
