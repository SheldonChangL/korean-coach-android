package com.koreancoach.app.ui.feature.review

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.ui.feature.flashcard.FlipCard
import com.koreancoach.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    onBack: () -> Unit,
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Review", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = spacing.md)) {
            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { 
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) 
                }
                state.isFinished || state.cards.isEmpty() -> ReviewDoneState(
                    correct = state.correctCount,
                    total = state.cards.size,
                    onBack = onBack
                )
                else -> {
                    val card = state.cards[state.currentIndex]
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Progress
                        Column(modifier = Modifier.fillMaxWidth().padding(vertical = spacing.md)) {
                            LinearProgressIndicator(
                                progress = { state.currentIndex.toFloat() / state.cards.size },
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(androidx.compose.foundation.shape.CircleShape),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Item ${state.currentIndex + 1} of ${state.cards.size}", 
                                style = MaterialTheme.typography.labelMedium, 
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }

                        Spacer(Modifier.height(spacing.md))

                        FlipCard(
                            card = card,
                            isFlipped = state.isFlipped,
                            onClick = viewModel::flipCard,
                            onSwipeLeft = { viewModel.recordAnswer(0) },
                            onSwipeRight = { viewModel.recordAnswer(5) },
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )

                        Spacer(Modifier.height(spacing.xl))

                        // Review specific actions
                        AnimatedVisibility(
                            visible = state.isFlipped,
                            enter = fadeIn() + slideInVertically { it },
                            exit = fadeOut() + slideOutVertically { it }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = spacing.xl),
                                horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                            ) {
                                ReviewButton(
                                    label = "Again",
                                    color = ErrorRed,
                                    modifier = Modifier.weight(1f),
                                    onClick = { viewModel.recordAnswer(0) }
                                )
                                ReviewButton(
                                    label = "Hard",
                                    color = GoldAccent,
                                    modifier = Modifier.weight(1f),
                                    onClick = { viewModel.recordAnswer(3) }
                                )
                                ReviewButton(
                                    label = "Easy",
                                    color = SuccessGreen,
                                    modifier = Modifier.weight(1f),
                                    onClick = { viewModel.recordAnswer(5) }
                                )
                            }
                        }
                        
                        if (!state.isFlipped) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = spacing.xxl),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.TouchApp, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Tap to see the answer", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewButton(
    label: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ReviewDoneState(correct: Int, total: Int, onBack: () -> Unit) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier.fillMaxSize().padding(spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(140.dp).clip(androidx.compose.foundation.shape.CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(text = if (total == 0) "📭" else "✅", fontSize = 72.sp)
        }
        
        Spacer(Modifier.height(spacing.xl))
        
        Text(
            text = if (total == 0) "All caught up!" else "Review Complete!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        
        if (total > 0) {
            Spacer(Modifier.height(spacing.sm))
            Text(
                text = "You've reviewed $total items today.", 
                style = MaterialTheme.typography.bodyLarge, 
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(Modifier.height(spacing.lg))
        
        Text(
            text = if (total == 0) "No more cards due for review right now. Come back later!" else "Great job! Keep showing up every day to build a lasting memory.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(Modifier.height(spacing.xxl))
        
        Button(
            onClick = onBack, 
            modifier = Modifier.fillMaxWidth().height(56.dp), 
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Back to Dashboard", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        }
    }
}
