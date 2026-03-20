package com.koreancoach.app.ui.feature.hangul

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.R
import com.koreancoach.app.data.curriculum.HangulCharacterData
import com.koreancoach.app.domain.model.HangulCharacter
import com.koreancoach.app.ui.common.SpeechIconButton
import com.koreancoach.app.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HangulExploreScreen(
    showBackButton: Boolean,
    onBack: () -> Unit,
    onOpenStage: (String) -> Unit,
    viewModel: HangulExploreViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    DisposableEffect(viewModel) {
        onDispose { viewModel.stopSpeaking() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.hangul_explore_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = {
                            viewModel.stopSpeaking()
                            onBack()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
            contentPadding = PaddingValues(bottom = spacing.xxl)
        ) {
            item {
                Text(
                    text = stringResource(R.string.hangul_explore_instruction),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item {
                ExploreSection(
                    title = stringResource(R.string.consonants),
                    characters = HangulCharacterData.consonants,
                    state = state,
                    onOpenStage = { stageId ->
                        viewModel.stopSpeaking()
                        onOpenStage(stageId)
                    },
                    onPlay = viewModel::playCharacter
                )
            }
            item {
                ExploreSection(
                    title = stringResource(R.string.vowels),
                    characters = HangulCharacterData.vowels,
                    state = state,
                    onOpenStage = { stageId ->
                        viewModel.stopSpeaking()
                        onOpenStage(stageId)
                    },
                    onPlay = viewModel::playCharacter
                )
            }
        }
    }
}

@Composable
private fun ExploreSection(
    title: String,
    characters: List<HangulCharacter>,
    state: HangulExploreUiState,
    onOpenStage: (String) -> Unit,
    onPlay: (HangulCharacter) -> Unit
) {
    val rowCount = ((characters.size + 3) / 4).coerceAtLeast(1)
    val gridHeight = (rowCount * 112 + (rowCount - 1) * 8).dp

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .height(gridHeight),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(characters, key = { it.id }) { character ->
                val stageId = state.stageLookup[character.id]
                val unlocked = stageId != null && stageId in state.unlockedStageIds
                val completed = stageId != null && stageId in state.completedStageIds
                val containerColor = when {
                    completed -> MaterialTheme.colorScheme.tertiaryContainer
                    unlocked -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
                val contentColor = when {
                    completed -> MaterialTheme.colorScheme.onTertiaryContainer
                    unlocked -> MaterialTheme.colorScheme.onSecondaryContainer
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(112.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = containerColor,
                        contentColor = contentColor
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = stageId != null) { stageId?.let(onOpenStage) }
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(character.character, style = MaterialTheme.typography.headlineMedium, color = contentColor)
                        Text(
                            character.romanization,
                            style = MaterialTheme.typography.labelSmall,
                            color = contentColor,
                            textAlign = TextAlign.Center
                        )
                        if (!unlocked && !completed) {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp), tint = contentColor)
                        }
                        SpeechIconButton(
                            isPlaying = state.speechState.isSpeaking,
                            onClick = { onPlay(character) },
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}
