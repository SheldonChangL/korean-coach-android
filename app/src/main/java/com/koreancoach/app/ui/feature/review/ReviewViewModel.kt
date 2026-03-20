package com.koreancoach.app.ui.feature.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreancoach.app.data.repository.ReviewRepository
import com.koreancoach.app.domain.model.FlashCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReviewUiState())
    val state: StateFlow<ReviewUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            reviewRepository.getDueCards(System.currentTimeMillis()).collect { cards ->
                _state.update { it.copy(cards = cards, isLoading = false) }
            }
        }
    }

    fun flipCard() = _state.update { it.copy(isFlipped = !it.isFlipped) }

    fun recordAnswer(quality: Int) {
        val s = _state.value
        val currentCard = s.cards.getOrNull(s.currentIndex) ?: return
        viewModelScope.launch {
            reviewRepository.recordReview(currentCard, quality)
            val nextIndex = s.currentIndex + 1
            _state.update {
                it.copy(
                    currentIndex = nextIndex,
                    isFlipped = false,
                    isFinished = nextIndex >= s.cards.size,
                    correctCount = if (quality >= 3) it.correctCount + 1 else it.correctCount
                )
            }
        }
    }
}

data class ReviewUiState(
    val cards: List<FlashCard> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val isFinished: Boolean = false,
    val isLoading: Boolean = true,
    val correctCount: Int = 0
)
