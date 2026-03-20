package com.koreancoach.app.ui.feature.writing

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.R
import com.koreancoach.app.domain.model.HangulCharacter
import com.koreancoach.app.domain.model.HangulCharacterType
import com.koreancoach.app.domain.model.StrokePoint
import com.koreancoach.app.ui.theme.LocalSpacing
import com.koreancoach.app.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HangulWritingScreen(
    onBack: () -> Unit,
    viewModel: HangulWritingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.hangul_writing_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.sm)
        ) {
            // Progress bar
            LinearProgressIndicator(
                progress = { state.progressFraction },
                modifier = Modifier.fillMaxWidth().height(6.dp),
                color = MaterialTheme.colorScheme.primary
            )

            // Character selector
            CharacterSelectorRow(
                characters = state.characters,
                currentIndex = state.currentCharacterIndex,
                onSelect = viewModel::selectCharacter
            )

            state.currentCharacter?.let { character ->
                // Character info card
                CharacterInfoCard(character = character)

                Spacer(modifier = Modifier.height(spacing.xs))

                // Stroke hint
                state.currentStroke?.let { stroke ->
                    AnimatedVisibility(!state.isCharacterComplete) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(spacing.sm),
                                horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("✏️", fontSize = 16.sp)
                                Column {
                                    Text(
                                        stringResource(R.string.stroke_progress, state.currentStrokeIndex + 1, character.strokeCount),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        stroke.hint,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    }
                }

                // Drawing canvas
                TracingCanvas(
                    character = character,
                    completedStrokes = state.completedStrokes,
                    userStrokePoints = state.userStrokePoints,
                    isComplete = state.isCharacterComplete,
                    onAddPoint = viewModel::addPoint,
                    onSubmitStroke = viewModel::submitStroke,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(20.dp))
                )

                // Result feedback
                AnimatedVisibility(
                    visible = state.strokeResult != null,
                    enter = fadeIn() + slideInVertically { it / 2 },
                    exit = fadeOut()
                ) {
                    state.strokeResult?.let { result ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when (result) {
                                StrokeResult.CORRECT -> SuccessGreen.copy(alpha = 0.12f)
                                StrokeResult.NEEDS_RETRY -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(spacing.sm),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(spacing.xs)
                            ) {
                                Text(
                                    if (result == StrokeResult.CORRECT) "✅" else "↩️",
                                    fontSize = 20.sp
                                )
                                Text(
                                    if (result == StrokeResult.CORRECT) stringResource(R.string.stroke_correct)
                                    else stringResource(R.string.stroke_retry),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                // Character complete celebration
                if (state.isCharacterComplete) {
                    CharacterCompleteCard(
                        character = character,
                        hasNext = state.currentCharacterIndex < state.characters.size - 1,
                        onNext = viewModel::nextCharacter,
                        onRetry = viewModel::retryCharacter
                    )
                } else {
                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                    ) {
                        OutlinedButton(
                            onClick = viewModel::clearCurrentStroke,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Refresh, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(stringResource(R.string.clear))
                        }
                        Button(
                            onClick = { /* submitStroke called from canvas */ },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            enabled = state.userStrokePoints.size >= 3
                        ) {
                            Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(stringResource(R.string.check))
                        }
                    }
                }

                // Score row
                if (state.totalAttempts > 0) {
                    Text(
                        stringResource(R.string.writing_session_score, state.totalCorrect, state.totalAttempts),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterSelectorRow(
    characters: List<HangulCharacter>,
    currentIndex: Int,
    onSelect: (Int) -> Unit
) {
    val consonants = characters.filter { it.type == HangulCharacterType.CONSONANT }
    val vowels = characters.filter { it.type == HangulCharacterType.VOWEL }
    val syllables = characters.filter { it.type == HangulCharacterType.SYLLABLE }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(stringResource(R.string.consonants), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(consonants) { _, char ->
                val globalIndex = characters.indexOf(char)
                CharacterChip(char = char, selected = globalIndex == currentIndex, onClick = { onSelect(globalIndex) })
            }
        }
        if (vowels.isNotEmpty()) {
            Text(stringResource(R.string.vowels), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(vowels) { _, char ->
                    val globalIndex = characters.indexOf(char)
                    CharacterChip(char = char, selected = globalIndex == currentIndex, onClick = { onSelect(globalIndex) })
                }
            }
        }
        if (syllables.isNotEmpty()) {
            Text(stringResource(R.string.example_blocks), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(syllables) { _, char ->
                    val globalIndex = characters.indexOf(char)
                    CharacterChip(char = char, selected = globalIndex == currentIndex, onClick = { onSelect(globalIndex) })
                }
            }
        }
    }
}

@Composable
private fun CharacterChip(char: HangulCharacter, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                char.character,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    )
}

@Composable
private fun CharacterInfoCard(character: HangulCharacter) {
    val spacing = LocalSpacing.current
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            Text(
                character.character,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(character.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("[${character.romanization}]", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(character.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun TracingCanvas(
    character: HangulCharacter,
    completedStrokes: List<List<Offset>>,
    userStrokePoints: List<Offset>,
    isComplete: Boolean,
    onAddPoint: (Offset) -> Unit,
    onSubmitStroke: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
    val guideColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
    val completedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
    val startDotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    val userColor = MaterialTheme.colorScheme.tertiary
    val successColor = SuccessGreen

    var canvasWidth by remember { mutableStateOf(1f) }
    var canvasHeight by remember { mutableStateOf(1f) }

    Canvas(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp))
            .pointerInput(isComplete) {
                if (!isComplete) {
                    detectDragGestures(
                        onDragStart = { offset -> onAddPoint(offset) },
                        onDrag = { change, _ -> onAddPoint(change.position) },
                        onDragEnd = { onSubmitStroke(canvasWidth, canvasHeight) }
                    )
                }
            }
    ) {
        canvasWidth = size.width
        canvasHeight = size.height

        // Grid lines
        drawGuideGrid(gridColor)

        // Reference stroke guides (ghost outlines)
        character.strokes.forEachIndexed { strokeIndex, refStroke ->
            if (strokeIndex >= completedStrokes.size) {
                drawReferenceStroke(refStroke.points, guideColor, size.width, size.height)
            }
        }

        // Start dot for current stroke
        val currentStrokeRef = character.strokes.getOrNull(completedStrokes.size)
        currentStrokeRef?.points?.firstOrNull()?.let { startPt ->
            drawCircle(
                color = startDotColor,
                radius = 14f,
                center = Offset(startPt.x * size.width, startPt.y * size.height)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.8f),
                radius = 6f,
                center = Offset(startPt.x * size.width, startPt.y * size.height)
            )
        }

        // Completed strokes
        completedStrokes.forEachIndexed { index, stroke ->
            drawUserStroke(stroke, if (isComplete) successColor else completedColor)
        }

        // Current user stroke in progress
        if (userStrokePoints.size >= 2) {
            drawUserStroke(userStrokePoints, userColor)
        }
    }
}

private fun DrawScope.drawGuideGrid(color: Color) {
    // Centre cross and border
    drawLine(color, Offset(size.width / 2f, 0f), Offset(size.width / 2f, size.height), 1.5f)
    drawLine(color, Offset(0f, size.height / 2f), Offset(size.width, size.height / 2f), 1.5f)
    // Diagonal guides (light)
    drawLine(color.copy(alpha = color.alpha * 0.5f), Offset(0f, 0f), Offset(size.width, size.height), 1f)
    drawLine(color.copy(alpha = color.alpha * 0.5f), Offset(size.width, 0f), Offset(0f, size.height), 1f)
}

private fun DrawScope.drawReferenceStroke(
    points: List<StrokePoint>,
    color: Color,
    width: Float,
    height: Float
) {
    if (points.size < 2) return
    val path = Path()
    path.moveTo(points.first().x * width, points.first().y * height)
    points.drop(1).forEach { pt -> path.lineTo(pt.x * width, pt.y * height) }
    drawPath(path, color, style = Stroke(width = 12f, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.drawUserStroke(points: List<Offset>, color: Color) {
    if (points.size < 2) return
    val path = Path()
    path.moveTo(points.first().x, points.first().y)
    points.drop(1).forEach { pt -> path.lineTo(pt.x, pt.y) }
    drawPath(path, color, style = Stroke(width = 18f, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

@Composable
private fun CharacterCompleteCard(
    character: HangulCharacter,
    hasNext: Boolean,
    onNext: () -> Unit,
    onRetry: () -> Unit
) {
    val spacing = LocalSpacing.current
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SuccessGreen.copy(alpha = 0.1f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.sm)
        ) {
            Text("🎉", fontSize = 40.sp)
            Text(
                stringResource(R.string.character_complete, character.character),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = SuccessGreen
            )
            Text(
                character.memoryHook,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                OutlinedButton(onClick = onRetry, shape = RoundedCornerShape(12.dp)) {
                    Text(stringResource(R.string.practice_again))
                }
                if (hasNext) {
                    Button(onClick = onNext, shape = RoundedCornerShape(12.dp)) {
                        Text(stringResource(R.string.next_character))
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
