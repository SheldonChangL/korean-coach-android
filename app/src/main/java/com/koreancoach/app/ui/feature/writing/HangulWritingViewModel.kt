package com.koreancoach.app.ui.feature.writing

import androidx.lifecycle.ViewModel
import com.koreancoach.app.data.curriculum.HangulCharacterData
import com.koreancoach.app.domain.model.HangulCharacter
import com.koreancoach.app.domain.model.Stroke
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

data class WritingUiState(
    val characters: List<HangulCharacter> = HangulCharacterData.allCharacters,
    val currentCharacterIndex: Int = 0,
    val currentStrokeIndex: Int = 0,
    val completedStrokes: List<List<Offset>> = emptyList(),
    val userStrokePoints: List<Offset> = emptyList(),
    val strokeResult: StrokeResult? = null,
    val isCharacterComplete: Boolean = false,
    val totalCorrect: Int = 0,
    val totalAttempts: Int = 0
) {
    val currentCharacter: HangulCharacter?
        get() = characters.getOrNull(currentCharacterIndex)
    val currentStroke: Stroke?
        get() = currentCharacter?.strokes?.getOrNull(currentStrokeIndex)
    val progressFraction: Float
        get() = if (characters.isEmpty()) 0f else currentCharacterIndex.toFloat() / characters.size
}

enum class StrokeResult { CORRECT, NEEDS_RETRY }

@HiltViewModel
class HangulWritingViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(WritingUiState())
    val state: StateFlow<WritingUiState> = _state.asStateFlow()

    fun addPoint(point: Offset) {
        _state.update { it.copy(userStrokePoints = it.userStrokePoints + point) }
    }

    fun clearCurrentStroke() {
        _state.update { it.copy(userStrokePoints = emptyList(), strokeResult = null) }
    }

    fun submitStroke(canvasWidth: Float, canvasHeight: Float) {
        val state = _state.value
        val stroke = state.currentStroke ?: return
        val points = state.userStrokePoints
        if (points.size < 3) return

        val isCorrect = validateStroke(
            userPoints = points,
            referenceStroke = stroke,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight
        )

        val result = if (isCorrect) StrokeResult.CORRECT else StrokeResult.NEEDS_RETRY

        if (isCorrect) {
            val completedStrokes = state.completedStrokes + listOf(points)
            val nextStrokeIndex = state.currentStrokeIndex + 1
            val charComplete = nextStrokeIndex >= (state.currentCharacter?.strokeCount ?: 0)

            _state.update {
                it.copy(
                    completedStrokes = completedStrokes,
                    userStrokePoints = emptyList(),
                    currentStrokeIndex = if (charComplete) it.currentStrokeIndex else nextStrokeIndex,
                    isCharacterComplete = charComplete,
                    strokeResult = result,
                    totalCorrect = it.totalCorrect + 1,
                    totalAttempts = it.totalAttempts + 1
                )
            }
        } else {
            _state.update {
                it.copy(
                    userStrokePoints = emptyList(),
                    strokeResult = result,
                    totalAttempts = it.totalAttempts + 1
                )
            }
        }
    }

    fun nextCharacter() {
        val current = _state.value
        val nextIndex = (current.currentCharacterIndex + 1).coerceAtMost(current.characters.size - 1)
        _state.update {
            it.copy(
                currentCharacterIndex = nextIndex,
                currentStrokeIndex = 0,
                completedStrokes = emptyList(),
                userStrokePoints = emptyList(),
                strokeResult = null,
                isCharacterComplete = false
            )
        }
    }

    fun retryCharacter() {
        _state.update {
            it.copy(
                currentStrokeIndex = 0,
                completedStrokes = emptyList(),
                userStrokePoints = emptyList(),
                strokeResult = null,
                isCharacterComplete = false
            )
        }
    }

    fun selectCharacter(index: Int) {
        _state.update {
            it.copy(
                currentCharacterIndex = index,
                currentStrokeIndex = 0,
                completedStrokes = emptyList(),
                userStrokePoints = emptyList(),
                strokeResult = null,
                isCharacterComplete = false
            )
        }
    }

    /**
     * Fuzzy stroke validation: checks that the user's stroke starts and ends
     * near the reference stroke's start and end points.
     *
     * Tolerance is 25% of canvas dimension — generous for beginners.
     */
    private fun validateStroke(
        userPoints: List<Offset>,
        referenceStroke: Stroke,
        canvasWidth: Float,
        canvasHeight: Float
    ): Boolean {
        if (userPoints.isEmpty() || referenceStroke.points.isEmpty()) return false

        val refStart = referenceStroke.points.first()
        val refEnd = referenceStroke.points.last()

        val refStartPx = Offset(refStart.x * canvasWidth, refStart.y * canvasHeight)
        val refEndPx = Offset(refEnd.x * canvasWidth, refEnd.y * canvasHeight)

        val userStart = userPoints.first()
        val userEnd = userPoints.last()

        val tolerance = canvasWidth * 0.30f // 30% tolerance — beginner-friendly

        val startOk = distance(userStart, refStartPx) < tolerance
        val endOk = distance(userEnd, refEndPx) < tolerance

        return startOk && endOk
    }

    private fun distance(a: Offset, b: Offset): Float {
        val dx = a.x - b.x
        val dy = a.y - b.y
        return sqrt(dx * dx + dy * dy).toFloat()
    }
}
