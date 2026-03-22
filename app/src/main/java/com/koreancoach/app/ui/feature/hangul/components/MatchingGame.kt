package com.koreancoach.app.ui.feature.hangul.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.koreancoach.app.R
import com.koreancoach.app.ui.theme.LocalSpacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class MatchCard(
    val id: Int,
    val pairId: Int,
    val text: String,
    val isCharCard: Boolean
)

/**
 * Memory card flip game that matches Korean characters to their romanization.
 *
 * @param koreanToRomanization List of (Korean character, romanization) pairs. Up to 4 are used.
 * @param onSpeak Called with the Korean text when a pair is matched (for TTS replay).
 * @param onComplete Called when all pairs have been matched.
 */
@Composable
fun MatchingGame(
    koreanToRomanization: List<Pair<String, String>>,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier,
    onComplete: () -> Unit = {}
) {
    val spacing = LocalSpacing.current
    val pairs = remember(koreanToRomanization) { koreanToRomanization.take(4) }

    // Build and shuffle cards once per pairs list
    val cards = remember(pairs) {
        buildList {
            pairs.forEachIndexed { i, (korean, roman) ->
                add(MatchCard(id = i * 2, pairId = i, text = korean, isCharCard = true))
                add(MatchCard(id = i * 2 + 1, pairId = i, text = roman, isCharCard = false))
            }
        }.shuffled()
    }

    var flippedIds by remember { mutableStateOf(emptyList<Int>()) }
    var matchedPairIds by remember { mutableStateOf(emptySet<Int>()) }
    var isChecking by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val isComplete = pairs.isNotEmpty() && matchedPairIds.size == pairs.size

    LaunchedEffect(isComplete) {
        if (isComplete) onComplete()
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
        // Header: title + score
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.matching_game_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                stringResource(R.string.matching_game_score, matchedPairIds.size, pairs.size),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (isComplete) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.md),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(R.string.matching_game_complete),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        } else {
            // Card grid: 4 columns, up to 2 rows
            cards.chunked(4).forEach { rowCards ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.xs)
                ) {
                    rowCards.forEach { card ->
                        val isFlipped = card.id in flippedIds || card.pairId in matchedPairIds
                        val isMatched = card.pairId in matchedPairIds
                        FlipCard(
                            card = card,
                            isFlipped = isFlipped,
                            isMatched = isMatched,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (isChecking || isFlipped) return@FlipCard
                                val newFlipped = flippedIds + card.id
                                flippedIds = newFlipped
                                if (newFlipped.size == 2) {
                                    isChecking = true
                                    scope.launch {
                                        val a = cards.first { it.id == newFlipped[0] }
                                        val b = cards.first { it.id == newFlipped[1] }
                                        if (a.pairId == b.pairId) {
                                            matchedPairIds = matchedPairIds + a.pairId
                                            onSpeak(pairs[a.pairId].first)
                                        } else {
                                            delay(800)
                                        }
                                        flippedIds = emptyList()
                                        isChecking = false
                                    }
                                }
                            }
                        )
                    }
                    // Fill empty slots in the last row
                    repeat(4 - rowCards.size) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }

        Text(
            stringResource(R.string.matching_game_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FlipCard(
    card: MatchCard,
    isFlipped: Boolean,
    isMatched: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "flip_${card.id}"
    )
    val showContent = rotation >= 90f

    Box(
        modifier = modifier
            .aspectRatio(0.75f)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .background(
                color = when {
                    !showContent -> MaterialTheme.colorScheme.surfaceVariant
                    isMatched -> MaterialTheme.colorScheme.primaryContainer
                    card.isCharCard -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.tertiaryContainer
                },
                shape = MaterialTheme.shapes.medium
            )
            .border(
                width = if (isMatched) 2.dp else 0.5.dp,
                color = if (isMatched) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .clickable(enabled = !isFlipped) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (showContent) {
            Text(
                text = card.text,
                modifier = Modifier
                    .graphicsLayer { rotationY = 180f }
                    .padding(4.dp),
                style = if (card.isCharCard) MaterialTheme.typography.headlineMedium
                        else MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
