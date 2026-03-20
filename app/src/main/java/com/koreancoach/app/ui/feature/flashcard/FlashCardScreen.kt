package com.koreancoach.app.ui.feature.flashcard

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.domain.model.FlashCard
import com.koreancoach.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashCardScreen(
    lessonId: String,
    onFinish: () -> Unit,
    viewModel: FlashCardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Practice Cards", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onFinish) { Icon(Icons.Default.Close, "Close") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { 
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) 
                }
                state.isFinished -> FinishedState(
                    correct = state.correctCount,
                    total = state.cards.size,
                    onFinish = onFinish
                )
                state.cards.isEmpty() -> EmptyCardsState(onFinish)
                else -> {
                    val card = state.cards[state.currentIndex]
                    Column(
                        modifier = Modifier.fillMaxSize().padding(horizontal = spacing.md),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Progress
                        Column(modifier = Modifier.fillMaxWidth().padding(vertical = spacing.md)) {
                            LinearProgressIndicator(
                                progress = { (state.currentIndex.toFloat()) / state.cards.size },
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "${state.currentIndex + 1} of ${state.cards.size} items",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                        
                        Spacer(Modifier.height(spacing.md))

                        // Flip card with more depth
                        FlipCard(
                            card = card,
                            isFlipped = state.isFlipped,
                            onClick = viewModel::flipCard,
                            onSwipeLeft = { viewModel.swipeCard(0) },
                            onSwipeRight = { viewModel.swipeCard(5) },
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )

                        Spacer(Modifier.height(spacing.xl))

                        // Interaction hint
                        Box(
                            modifier = Modifier.height(40.dp).fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedVisibility(
                                visible = !state.isFlipped,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.TouchApp, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                                    Text("Tap card to see the answer", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            
                            AnimatedVisibility(
                                visible = state.isFlipped,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                                    Text("Swipe or tap buttons to rate", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }

                        // Action buttons
                        AnimatedVisibility(
                            visible = state.isFlipped,
                            enter = slideInVertically { it } + fadeIn(),
                            exit = slideOutVertically { it } + fadeOut()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = spacing.xl),
                                horizontalArrangement = Arrangement.spacedBy(spacing.md)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.swipeCard(0) },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                                    shape = MaterialTheme.shapes.medium,
                                    border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.5f)),
                                    modifier = Modifier.weight(1f).height(56.dp)
                                ) {
                                    Icon(Icons.Default.Close, null, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Still learning", fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = { viewModel.swipeCard(5) },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.weight(1f).height(56.dp)
                                ) {
                                    Icon(Icons.Default.Check, null, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Mastered", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        
                        if (!state.isFlipped) {
                            Spacer(Modifier.height(56.dp + spacing.xl))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlipCard(
    card: FlashCard,
    isFlipped: Boolean,
    onClick: () -> Unit,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "card_flip"
    )
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffset by animateFloatAsState(targetValue = offsetX, label = "card_offset")

    Box(
        modifier = modifier
            .graphicsLayer { 
                translationX = animatedOffset
                rotationZ = (offsetX / 20f).coerceIn(-10f, 10f)
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        when {
                            offsetX < -250 -> { onSwipeLeft(); offsetX = 0f }
                            offsetX > 250 -> { onSwipeRight(); offsetX = 0f }
                            else -> offsetX = 0f
                        }
                    }
                ) { _, dragAmount ->
                    offsetX += dragAmount
                }
            }
    ) {
        // Front face
        CardFace(
            content = {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally, 
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = card.front, 
                        style = MaterialTheme.typography.displayLarge, 
                        fontWeight = FontWeight.Black, 
                        color = MaterialTheme.colorScheme.onPrimaryContainer, 
                        textAlign = TextAlign.Center
                    )
                    if (card.frontSubtext.isNotBlank()) {
                        Spacer(Modifier.height(16.dp))
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = card.frontSubtext, 
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.titleMedium, 
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            },
            color = MaterialTheme.colorScheme.primaryContainer,
            isVisible = rotation <= 90f,
            onClick = onClick,
            rotation = rotation
        )

        // Back face
        CardFace(
            content = {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally, 
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = card.back, 
                        style = MaterialTheme.typography.headlineLarge, 
                        fontWeight = FontWeight.Black, 
                        color = OnKoreanBlueContainer, 
                        textAlign = TextAlign.Center
                    )
                    if (card.backSubtext.isNotBlank()) {
                        Spacer(Modifier.height(24.dp))
                        Surface(
                            color = GoldAccentContainer.copy(alpha = 0.6f), 
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = OnGoldAccentContainer, modifier = Modifier.size(20.dp))
                                Text(
                                    text = card.backSubtext, 
                                    style = MaterialTheme.typography.bodyMedium, 
                                    color = OnGoldAccentContainer,
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                    }
                }
            },
            color = KoreanBlueContainer,
            isVisible = rotation > 90f,
            onClick = onClick,
            rotation = rotation - 180f
        )
        
        // Swipe overlays
        if (offsetX > 100) {
            Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.TopEnd) {
                Surface(shape = CircleShape, color = SuccessGreen, tonalElevation = 8.dp) {
                    Icon(Icons.Default.Check, null, modifier = Modifier.padding(12.dp).size(32.dp), tint = androidx.compose.ui.graphics.Color.White)
                }
            }
        } else if (offsetX < -100) {
            Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.TopStart) {
                Surface(shape = CircleShape, color = ErrorRed, tonalElevation = 8.dp) {
                    Icon(Icons.Default.Close, null, modifier = Modifier.padding(12.dp).size(32.dp), tint = androidx.compose.ui.graphics.Color.White)
                }
            }
        }
    }
}

@Composable
private fun CardFace(
    content: @Composable BoxScope.() -> Unit,
    color: androidx.compose.ui.graphics.Color,
    isVisible: Boolean,
    onClick: () -> Unit,
    rotation: Float
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        color = color,
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 15f * density
                alpha = if (isVisible) 1f else 0f
            }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center, content = content)
    }
}

@Composable
private fun FinishedState(correct: Int, total: Int, onFinish: () -> Unit) {
    val spacing = LocalSpacing.current
    val percentage = if (total > 0) (correct.ScorableFloat() / total * 100).toInt() else 0
    val isSuccess = percentage >= 70

    Column(
        modifier = Modifier.fillMaxSize().padding(spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(140.dp).clip(CircleShape).background(if (isSuccess) SuccessContainer else MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(text = if (isSuccess) "🎯" else "💪", fontSize = 72.sp)
        }
        
        Spacer(Modifier.height(spacing.xl))
        
        Text(
            text = "Session Complete!", 
            style = MaterialTheme.typography.headlineMedium, 
            fontWeight = FontWeight.Black
        )
        
        Spacer(Modifier.height(spacing.sm))
        
        Text(
            text = "You've practiced $total items and mastered $correct of them.", 
            style = MaterialTheme.typography.bodyLarge, 
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        Spacer(Modifier.height(spacing.xxl))
        
        Button(
            onClick = onFinish, 
            shape = MaterialTheme.shapes.medium, 
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Finish Session", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun EmptyCardsState(onFinish: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("No cards found for this lesson.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onFinish) { Text("Go Back") }
    }
}
