package com.koreancoach.app.ui.feature.hangul

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.domain.model.Lesson
import com.koreancoach.app.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HangulPathScreen(
    onBack: () -> Unit,
    onExplore: () -> Unit,
    onOpenLesson: (String) -> Unit,
    viewModel: HangulPathViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hangul Sprint", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onExplore) {
                        Icon(Icons.Default.Explore, contentDescription = "Explore Hangul")
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

        if (state.shouldShowEmptyState) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = spacing.md),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(spacing.sm)
                ) {
                    Text("Hangul path is still getting ready.", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "Please go back and reopen this screen. If it still stays empty, let me know because that means the curriculum failed to seed.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = spacing.md,
                end = spacing.md,
                top = paddingValues.calculateTopPadding(),
                bottom = spacing.xxl
            ),
            verticalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            item {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(spacing.md),
                        verticalArrangement = Arrangement.spacedBy(spacing.sm)
                    ) {
                        Text("Build reading confidence first", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            "Work through 6 guided stages, then unlock Survival Korean with much less guessing.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        LinearProgressIndicator(
                            progress = { state.progressFraction },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "${state.completedCount} / ${state.totalCount} stages complete",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            items(state.lessons, key = { it.id }) { lesson ->
                HangulStageCard(
                    lesson = lesson,
                    onClick = { if (lesson.isUnlocked) onOpenLesson(lesson.id) }
                )
            }
        }
    }
}

@Composable
private fun HangulStageCard(
    lesson: Lesson,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = when {
                lesson.isCompleted -> MaterialTheme.colorScheme.tertiaryContainer
                lesson.isUnlocked -> MaterialTheme.colorScheme.surface
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AssistChip(
                onClick = {},
                enabled = false,
                label = { Text(lesson.emoji) }
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(lesson.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(lesson.subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(lesson.learningObjective, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(
                imageVector = when {
                    lesson.isCompleted -> Icons.Default.TaskAlt
                    lesson.isUnlocked -> Icons.Default.PlayArrow
                    else -> Icons.Default.Lock
                },
                contentDescription = null
            )
        }
    }
}
