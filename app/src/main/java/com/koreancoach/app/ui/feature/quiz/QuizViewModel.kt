package com.koreancoach.app.ui.feature.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.repository.QuizRepository
import com.koreancoach.app.domain.model.QuizQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerRevealed: Boolean = false,
    val score: Int = 0,
    val isFinished: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QuizUiState())
    val state: StateFlow<QuizUiState> = _state.asStateFlow()

    fun loadQuiz(lessonId: String) {
        viewModelScope.launch {
            val questions = quizRepository.generateQuiz(lessonId)
            _state.update { it.copy(questions = questions, isLoading = false) }
        }
    }

    fun selectOption(index: Int) {
        if (_state.value.isAnswerRevealed) return
        _state.update { it.copy(selectedOptionIndex = index, isAnswerRevealed = true) }
    }

    fun nextQuestion() {
        val state = _state.value
        val isCorrect = state.selectedOptionIndex == state.questions.getOrNull(state.currentIndex)?.correctOptionIndex
        val newScore = if (isCorrect) state.score + 1 else state.score
        val nextIndex = state.currentIndex + 1

        if (nextIndex >= state.questions.size) {
            _state.update { it.copy(score = newScore, isFinished = true) }
        } else {
            _state.update { it.copy(
                currentIndex = nextIndex,
                selectedOptionIndex = null,
                isAnswerRevealed = false,
                score = newScore
            ) }
        }
    }
}
