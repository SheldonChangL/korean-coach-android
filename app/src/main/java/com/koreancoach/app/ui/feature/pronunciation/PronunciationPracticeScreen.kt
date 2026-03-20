package com.koreancoach.app.ui.feature.pronunciation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.ui.common.SpeechIconButton
import com.koreancoach.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PronunciationPracticeScreen(
    lessonId: String,
    onBack: () -> Unit,
    viewModel: PronunciationPracticeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(lessonId) { viewModel.loadLesson(lessonId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pronunciation Practice", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Vocab item selector
            if (state.vocabItems.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(state.vocabItems) { _, item ->
                        val isTarget = item == state.targetItem
                        FilterChip(
                            selected = isTarget,
                            onClick = { viewModel.setTarget(item) },
                            label = { Text(item.korean) }
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Target word display
            state.targetItem?.let { target ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SpeechIconButton(
                            isPlaying = state.speechState.isSpeaking,
                            onClick = {
                                if (state.speechState.isSpeaking) viewModel.stopTargetAudio()
                                else viewModel.playTargetAudio()
                            }
                        )
                        Text(
                            text = target.korean,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "[${target.romanization}]",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            text = target.english,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Phase-aware result display
            AnimatedVisibility(visible = state.phase == RecordingPhase.RESULT && state.lastResult != null) {
                state.lastResult?.let { result ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (result.scorePercent >= 70f) SuccessContainer else ErrorContainer
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${result.scorePercent.toInt()}%",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (result.scorePercent >= 70f) SuccessGreen else ErrorRed
                            )
                            if (state.recognizedText.isNotEmpty()) {
                                Text(
                                    text = "Heard: \"${state.recognizedText}\"",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                            result.feedback?.let { fb ->
                                Text(
                                    text = fb,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = state.phase == RecordingPhase.ERROR) {
                state.error?.let { error ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = ErrorContainer)
                    ) {
                        Text(
                            text = error,
                            modifier = Modifier.padding(16.dp),
                            color = ErrorRed
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Mic button
            val isListening = state.phase == RecordingPhase.LISTENING
            val isProcessing = state.phase == RecordingPhase.PROCESSING

            val micScale by animateFloatAsState(
                targetValue = if (isListening) 1.15f else 1f,
                animationSpec = if (isListening) {
                    infiniteRepeatable(
                        animation = tween(600, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                } else {
                    spring()
                },
                label = "micScale"
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                when {
                    isProcessing -> {
                        CircularProgressIndicator()
                        Text("Analyzing...", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    else -> {
                        FloatingActionButton(
                            onClick = {
                                if (isListening) viewModel.stopRecordingAndScore()
                                else { viewModel.resetToIdle(); viewModel.startRecording() }
                            },
                            modifier = Modifier.size(72.dp).scale(micScale).clip(CircleShape),
                            containerColor = if (isListening) ErrorRed else KoreanRed
                        ) {
                            Icon(
                                imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                                contentDescription = if (isListening) "Stop" else "Record",
                                modifier = Modifier.size(32.dp),
                                tint = OnKoreanRed
                            )
                        }
                        Text(
                            text = when (state.phase) {
                                RecordingPhase.IDLE -> "Tap to speak"
                                RecordingPhase.LISTENING -> "Listening… tap to stop"
                                RecordingPhase.RESULT -> "Tap to try again"
                                RecordingPhase.ERROR -> "Tap to retry"
                                else -> ""
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                state.averageScore?.let { avg ->
                    Text(
                        text = "Session avg: ${avg.toInt()}% (${state.attemptCount} attempts)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
