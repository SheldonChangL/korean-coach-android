package com.koreancoach.app.ui.feature.quiz

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.ui.common.ConfettiOverlay
import com.koreancoach.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    lessonId: String,
    onFinish: () -> Unit,
    onBack: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(lessonId) { viewModel.loadQuiz(lessonId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.isFinished -> {
                    QuizResultScreen(
                        score = state.score,
                        total = state.questions.size,
                        onFinish = onFinish
                    )
                }
                state.questions.isNotEmpty() -> {
                    val question = state.questions[state.currentIndex]
                    QuizQuestionContent(
                        question = question,
                        questionNumber = state.currentIndex + 1,
                        totalQuestions = state.questions.size,
                        selectedOptionIndex = state.selectedOptionIndex,
                        isAnswerRevealed = state.isAnswerRevealed,
                        onSelectOption = viewModel::selectOption,
                        onNext = viewModel::nextQuestion,
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    )
                }
                else -> {
                    Text(
                        text = "No questions available for this lesson.",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizQuestionContent(
    question: com.koreancoach.app.domain.model.QuizQuestion,
    questionNumber: Int,
    totalQuestions: Int,
    selectedOptionIndex: Int?,
    isAnswerRevealed: Boolean,
    onSelectOption: (Int) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        LinearProgressIndicator(
            progress = { questionNumber.toFloat() / totalQuestions },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Question $questionNumber / $totalQuestions",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                text = question.questionText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(20.dp),
                textAlign = TextAlign.Center
            )
        }

        question.options.forEachIndexed { index, option ->
            val isSelected = selectedOptionIndex == index
            val isCorrect = index == question.correctOptionIndex
            val containerColor = when {
                !isAnswerRevealed -> if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                isCorrect -> SuccessContainer
                isSelected -> ErrorContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
            val contentColor = when {
                !isAnswerRevealed -> MaterialTheme.colorScheme.onSurface
                isCorrect -> SuccessGreen
                isSelected -> ErrorRed
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }

            OutlinedButton(
                onClick = { onSelectOption(index) },
                enabled = !isAnswerRevealed,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = containerColor,
                    contentColor = contentColor,
                    disabledContainerColor = containerColor,
                    disabledContentColor = contentColor
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(option, modifier = Modifier.weight(1f))
                    if (isAnswerRevealed && isCorrect) Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen)
                    if (isAnswerRevealed && isSelected && !isCorrect) Icon(Icons.Default.Close, contentDescription = null, tint = ErrorRed)
                }
            }
        }

        AnimatedVisibility(visible = isAnswerRevealed) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Text(
                        text = question.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Button(
                    onClick = onNext,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
private fun QuizResultScreen(
    score: Int,
    total: Int,
    onFinish: () -> Unit
) {
    val percent = if (total > 0) (score * 100f / total).toInt() else 0
    val passed = percent >= 80

    Box(modifier = Modifier.fillMaxSize()) {
        ConfettiOverlay(active = passed, modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (passed) "🎉" else "📚",
                fontSize = 64.sp
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "$score / $total",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = if (passed) SuccessGreen else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (passed) "Excellent work!" else "Keep practicing!",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onFinish,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back to Dashboard")
            }
        }
    }
}
