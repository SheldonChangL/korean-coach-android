package com.koreancoach.app.ui.feature.flashcard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.repository.ReviewRepository
import com.koreancoach.app.domain.model.FlashCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlashCardViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"])

    private val _state = MutableStateFlow(FlashCardUiState())
    val state: StateFlow<FlashCardUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            reviewRepository.getCardsByLesson(lessonId).first().let { cards ->
                _state.update { it.copy(cards = cards, isLoading = false) }
            }
        }
    }

    fun flipCard() = _state.update { it.copy(isFlipped = !it.isFlipped) }

    fun swipeCard(quality: Int) {
        val currentState = _state.value
        val currentCard = currentState.cards.getOrNull(currentState.currentIndex) ?: return
        viewModelScope.launch {
            reviewRepository.recordReview(currentCard, quality)
            val nextIndex = currentState.currentIndex + 1
            _state.update {
                it.copy(
                    currentIndex = nextIndex,
                    isFlipped = false,
                    isFinished = nextIndex >= currentState.cards.size,
                    correctCount = if (quality >= 3) it.correctCount + 1 else it.correctCount
                )
            }
        }
    }
}

data class FlashCardUiState(
    val cards: List<FlashCard> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val isFinished: Boolean = false,
    val isLoading: Boolean = true,
    val correctCount: Int = 0
)
