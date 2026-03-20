package com.koreancoach.app.ui.feature.hangul

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.data.curriculum.HangulCharacterData
import com.koreancoach.app.domain.model.HangulCharacter
import com.koreancoach.app.ui.common.SpeechIconButton
import com.koreancoach.app.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HangulExploreScreen(
    onBack: () -> Unit,
    onOpenStage: (String) -> Unit,
    viewModel: HangulExploreViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore Hangul", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            Text(
                text = "Tap any character to hear its Korean name. Open its stage to practice it in context.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            ExploreSection(
                title = "Consonants",
                characters = HangulCharacterData.consonants,
                state = state,
                onOpenStage = onOpenStage,
                onPlay = viewModel::playCharacter
            )
            ExploreSection(
                title = "Vowels",
                characters = HangulCharacterData.vowels,
                state = state,
                onOpenStage = onOpenStage,
                onPlay = viewModel::playCharacter
            )
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
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.heightIn(max = 280.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(characters, key = { it.id }) { character ->
                val stageId = state.stageLookup[character.id]
                val unlocked = stageId != null && stageId in state.unlockedStageIds
                val completed = stageId != null && stageId in state.completedStageIds
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = when {
                            completed -> MaterialTheme.colorScheme.tertiaryContainer
                            unlocked -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
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
                        Text(character.character, style = MaterialTheme.typography.headlineMedium)
                        Text(character.romanization, style = MaterialTheme.typography.labelSmall)
                        if (!unlocked && !completed) {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
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
