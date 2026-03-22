package com.koreancoach.app.ui.feature.hangul

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.R
import com.koreancoach.app.data.curriculum.HangulCharacterData
import com.koreancoach.app.domain.model.CheckpointItem
import com.koreancoach.app.domain.model.DialogueItem
import com.koreancoach.app.domain.model.ReadingDrill
import com.koreancoach.app.domain.model.ScriptItem
import com.koreancoach.app.domain.model.SpeechSpec
import com.koreancoach.app.domain.model.WritingTarget
import com.koreancoach.app.ui.common.SpeechIconButton
import com.koreancoach.app.ui.theme.LocalSpacing

private sealed class PracticeRow {
    data class Grouped(val writing: WritingTarget, val reading: ReadingDrill) : PracticeRow()
    data class WriteOnly(val target: WritingTarget) : PracticeRow()
    data class ReadOnly(val drill: ReadingDrill) : PracticeRow()
}

private fun buildPracticeRows(writings: List<WritingTarget>, readings: List<ReadingDrill>): List<PracticeRow> {
    val readingsByGroup = readings.filter { it.practiceGroupId.isNotBlank() }.associateBy { it.practiceGroupId }
    val usedReadingIds = mutableSetOf<String>()
    val rows = mutableListOf<PracticeRow>()
    for (w in writings) {
        if (w.practiceGroupId.isNotBlank()) {
            val r = readingsByGroup[w.practiceGroupId]
            if (r != null) {
                rows.add(PracticeRow.Grouped(w, r))
                usedReadingIds.add(r.id)
            } else {
                rows.add(PracticeRow.WriteOnly(w))
            }
        } else {
            rows.add(PracticeRow.WriteOnly(w))
        }
    }
    for (r in readings) {
        if (r.id !in usedReadingIds) {
            rows.add(PracticeRow.ReadOnly(r))
        }
    }
    return rows
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HangulStageScreen(
    showBackButton: Boolean,
    onBack: () -> Unit,
    onBackToPath: () -> Unit,
    onOpenWriting: (String) -> Unit,
    viewModel: HangulStageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    DisposableEffect(viewModel) {
        onDispose { viewModel.stopSpeaking() }
    }

    LaunchedEffect(state.lesson?.id, state.autoPlayHangul) {
        if (state.lesson != null) {
            viewModel.maybeAutoPlay()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(state.lesson?.title ?: stringResource(R.string.hangul_stage_default_title), fontWeight = FontWeight.Bold)
                },
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
        },
        bottomBar = {
            state.lesson?.let { lesson ->
                BottomAppBar {
                    Button(
                        onClick = {
                            viewModel.stopSpeaking()
                            if (lesson.isCompleted) {
                                onBackToPath()
                            } else {
                                viewModel.markComplete(onBackToPath)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing.md, vertical = spacing.xs)
                    ) {
                        Text(if (lesson.isCompleted) stringResource(R.string.back_to_path) else stringResource(R.string.complete_stage))
                    }
                }
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                CircularProgressIndicator(modifier = Modifier.padding(24.dp))
            }
            return@Scaffold
        }

        val lesson = state.lesson
        if (lesson == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = spacing.md),
                contentAlignment = Alignment.Center
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                    Text(stringResource(R.string.error_loading_stage), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        stringResource(R.string.error_loading_stage_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            return@Scaffold
        }

        val practiceRows = remember(lesson.writingTargets, lesson.readingDrills) {
            buildPracticeRows(lesson.writingTargets, lesson.readingDrills)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding() + 80.dp,
                start = spacing.md,
                end = spacing.md
            ),
            verticalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            item {
                ElevatedCard {
                    Column(modifier = Modifier.padding(spacing.md), verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                        Text(lesson.subtitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(lesson.learningObjective, style = MaterialTheme.typography.bodyMedium)
                        if (!state.speechState.isReady) {
                            AssistChip(onClick = {}, enabled = false, label = { Text(stringResource(R.string.tts_unavailable)) })
                        }
                    }
                }
            }

            if (lesson.scriptItems.isNotEmpty()) {
                item { Text(stringResource(R.string.listen_and_learn), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                items(lesson.scriptItems, key = { it.id }) { item ->
                    ScriptCard(item = item)
                }
            }

            if (practiceRows.isNotEmpty()) {
                item { Text(stringResource(R.string.practice_section), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                items(practiceRows, key = { row ->
                    when (row) {
                        is PracticeRow.Grouped -> "grouped_${row.writing.characterId}"
                        is PracticeRow.WriteOnly -> "write_${row.target.characterId}"
                        is PracticeRow.ReadOnly -> "read_${row.drill.id}"
                    }
                }) { row ->
                    when (row) {
                        is PracticeRow.Grouped -> GroupedPracticeCard(
                            writing = row.writing,
                            reading = row.reading,
                            isSpeaking = state.speechState.isSpeaking,
                            onPlay = { viewModel.play(row.reading.speech, row.reading.displayText) },
                            onStop = viewModel::stopSpeaking,
                            onOpenWriting = { characterId ->
                                viewModel.stopSpeaking()
                                onOpenWriting(characterId)
                            }
                        )
                        is PracticeRow.WriteOnly -> WritingTargetCard(
                            target = row.target,
                            onOpenWriting = { characterId ->
                                viewModel.stopSpeaking()
                                onOpenWriting(characterId)
                            }
                        )
                        is PracticeRow.ReadOnly -> ReadingDrillCard(
                            drill = row.drill,
                            isSpeaking = state.speechState.isSpeaking,
                            onPlay = { viewModel.play(row.drill.speech, row.drill.displayText) },
                            onStop = viewModel::stopSpeaking
                        )
                    }
                }
            }

            if (lesson.dialogueItems.isNotEmpty()) {
                item { Text(stringResource(R.string.mini_dialogue), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                items(lesson.dialogueItems, key = { it.id }) { dialogue ->
                    DialogueCard(dialogue = dialogue, isSpeaking = state.speechState.isSpeaking, onPlay = { spec, fallback -> viewModel.play(spec, fallback) }, onStop = viewModel::stopSpeaking)
                }
            }

            if (lesson.checkpointItems.isNotEmpty()) {
                item { Text(stringResource(R.string.quick_check), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                items(lesson.checkpointItems, key = { it.id }) { checkpoint ->
                    CheckpointCard(
                        checkpoint = checkpoint,
                        selectedAnswer = state.selectedAnswers[checkpoint.id],
                        isComplete = checkpoint.id in state.completedCheckpoints,
                        onAnswer = { viewModel.answerCheckpoint(checkpoint, it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ScriptCard(
    item: ScriptItem
) {
    ElevatedCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
                Text(item.text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(item.translation, style = MaterialTheme.typography.bodyMedium)
                if (item.emphasis.isNotBlank()) {
                    Text(item.emphasis, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
        }
    }
}

@Composable
private fun GroupedPracticeCard(
    writing: WritingTarget,
    reading: ReadingDrill,
    isSpeaking: Boolean,
    onPlay: () -> Unit,
    onStop: () -> Unit,
    onOpenWriting: (String) -> Unit
) {
    ElevatedCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(reading.prompt, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    Text(reading.displayText, style = MaterialTheme.typography.headlineMedium)
                    if (reading.romanization.isNotBlank() || reading.translation.isNotBlank()) {
                        Text("${reading.romanization} · ${reading.translation}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                SpeechIconButton(isPlaying = isSpeaking, onClick = if (isSpeaking) onStop else onPlay)
            }
            HorizontalDivider()
            Text(writing.prompt, style = MaterialTheme.typography.bodyMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                FilledTonalButton(onClick = { onOpenWriting(writing.characterId) }) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.practice))
                }
            }
        }
    }
}

@Composable
private fun WritingTargetCard(
    target: WritingTarget,
    onOpenWriting: (String) -> Unit
) {
    val characterLabel = HangulCharacterData.findById(target.characterId)?.character ?: target.characterId
    ElevatedCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(target.prompt, style = MaterialTheme.typography.bodyLarge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    characterLabel,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                FilledTonalButton(onClick = { onOpenWriting(target.characterId) }) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.practice))
                }
            }
        }
    }
}

@Composable
private fun ReadingDrillCard(
    drill: ReadingDrill,
    isSpeaking: Boolean,
    onPlay: () -> Unit,
    onStop: () -> Unit
) {
    ElevatedCard {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(drill.prompt, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(drill.displayText, style = MaterialTheme.typography.headlineMedium)
                Text("${drill.romanization} · ${drill.translation}", style = MaterialTheme.typography.bodyMedium)
            }
            SpeechIconButton(isPlaying = isSpeaking, onClick = if (isSpeaking) onStop else onPlay)
        }
    }
}

@Composable
private fun DialogueCard(
    dialogue: DialogueItem,
    isSpeaking: Boolean,
    onPlay: (SpeechSpec, String) -> Unit,
    onStop: () -> Unit
) {
    ElevatedCard {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(dialogue.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            dialogue.lines.forEach { line ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(line.speaker, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        Text(line.text, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                        Text(line.translation, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    SpeechIconButton(
                        isPlaying = isSpeaking,
                        onClick = {
                            if (isSpeaking) onStop() else onPlay(line.speech, line.text)
                        }
                    )
                }
            }
            if (dialogue.comprehensionQuestion.isNotBlank()) {
                Text(
                    text = stringResource(R.string.hangul_check_prefix, dialogue.comprehensionQuestion),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CheckpointCard(
    checkpoint: CheckpointItem,
    selectedAnswer: String?,
    isComplete: Boolean,
    onAnswer: (String) -> Unit
) {
    ElevatedCard {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(checkpoint.prompt, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            checkpoint.options.forEach { option ->
                FilterChip(
                    selected = selectedAnswer == option,
                    onClick = { onAnswer(option) },
                    label = { Text(option) }
                )
            }
            if (selectedAnswer != null) {
                Text(
                    if (isComplete) checkpoint.explanation else stringResource(R.string.try_again),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isComplete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
