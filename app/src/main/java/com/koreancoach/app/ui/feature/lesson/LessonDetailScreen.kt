package com.koreancoach.app.ui.feature.lesson

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.domain.model.*
import com.koreancoach.app.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    lessonId: String,
    onStartFlashCards: () -> Unit,
    onStartQuiz: () -> Unit,
    onStartPronunciation: () -> Unit = {},
    onBack: () -> Unit,
    viewModel: LessonDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(state.lesson?.title ?: "Lesson", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(state.lesson?.subtitle ?: "", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            state.lesson?.let {
                BottomActionBar(
                    lesson = it,
                    onStartFlashCards = onStartFlashCards,
                    onStartQuiz = onStartQuiz,
                    onStartPronunciation = onStartPronunciation,
                    onMarkComplete = viewModel::markComplete
                )
            }
        }
    ) { paddingValues ->
        if (state.isLoading || state.lesson == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { 
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) 
            }
            return@Scaffold
        }
        val lesson = state.lesson!!

        LazyColumn(
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding() + spacing.md,
                start = spacing.md,
                end = spacing.md
            ),
            verticalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            // Header card
            item {
                LessonHeaderCard(lesson = lesson)
            }

            // Tab selector
            item {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TabRow(
                        selectedTabIndex = state.selectedTab.ordinal,
                        containerColor = androidx.compose.ui.graphics.Color.Transparent,
                        divider = {},
                        indicator = { tabPositions ->
                            if (state.selectedTab.ordinal < tabPositions.size) {
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[state.selectedTab.ordinal]),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    ) {
                        LessonTab.entries.forEach { tab ->
                            Tab(
                                selected = state.selectedTab == tab,
                                onClick = { viewModel.selectTab(tab) },
                                text = { 
                                    Text(
                                        tab.label, 
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = if (state.selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                                    ) 
                                }
                            )
                        }
                    }
                }
            }

            // Tab content
            when (state.selectedTab) {
                LessonTab.VOCABULARY -> {
                    if (lesson.vocabulary.isEmpty()) {
                        item { EmptyTabState("No vocabulary items yet") }
                    } else {
                        items(lesson.vocabulary, key = { it.id }) { vocab ->
                            VocabCard(vocab = vocab)
                        }
                    }
                }
                LessonTab.PHRASES -> {
                    if (lesson.phrases.isEmpty()) {
                        item { EmptyTabState("No phrases yet") }
                    } else {
                        items(lesson.phrases, key = { it.id }) { phrase ->
                            PhraseCard(phrase = phrase)
                        }
                    }
                }
                LessonTab.PRONUNCIATION -> {
                    if (lesson.pronunciationTips.isEmpty()) {
                        item { EmptyTabState("No pronunciation tips yet") }
                    } else {
                        items(lesson.pronunciationTips) { tip ->
                            PronunciationCard(tip = tip)
                        }
                    }
                }
                LessonTab.MEMORY -> {
                    if (lesson.memoryHooks.isEmpty()) {
                        item { EmptyTabState("No memory hooks yet") }
                    } else {
                        items(lesson.memoryHooks) { hook ->
                            MemoryHookCard(hook = hook)
                        }
                    }
                }
            }
            
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun LessonHeaderCard(lesson: Lesson) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(lesson.emoji, fontSize = 48.sp)
            }
            Spacer(Modifier.height(16.dp))
            Text(lesson.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LessonTag(text = "Week ${lesson.weekNumber}", containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                LessonTag(text = "${lesson.estimatedMinutes} min", containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                LessonTag(text = "${lesson.vocabulary.size} words", containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
private fun LessonTag(text: String, containerColor: androidx.compose.ui.graphics.Color) {
    Surface(
        shape = CircleShape,
        color = containerColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun VocabCard(vocab: VocabItem) {
    val spacing = LocalSpacing.current
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(spacing.md)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = vocab.korean, 
                        style = MaterialTheme.typography.headlineMedium, 
                        fontWeight = FontWeight.Bold, 
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = vocab.romanization, 
                        style = MaterialTheme.typography.bodyMedium, 
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = KoreanBlueContainer
                ) {
                    Text(
                        text = vocab.english,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = OnKoreanBlueContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            
            if (vocab.exampleSentence.isNotBlank()) {
                Spacer(Modifier.height(spacing.md))
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(spacing.sm)) {
                        Text(vocab.exampleSentence, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text(vocab.exampleTranslation, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            
            if (vocab.memoryHook.isNotBlank()) {
                Spacer(Modifier.height(spacing.sm))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                    Text(
                        text = vocab.memoryHook, 
                        style = MaterialTheme.typography.bodySmall, 
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}

@Composable
private fun PhraseCard(phrase: PhraseItem) {
    val spacing = LocalSpacing.current
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(spacing.md), verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
            Text(phrase.korean, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(phrase.romanization, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(phrase.english, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            
            if (phrase.context.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                    Text(phrase.context, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            
            if (phrase.usageTip.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = SuccessContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(spacing.sm), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                        Text(phrase.usageTip, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun PronunciationCard(tip: PronunciationTip) {
    val spacing = LocalSpacing.current
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(spacing.md), verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(KoreanBlueContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(tip.sound, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = OnKoreanBlueContainer)
                }
                Column {
                    Text("Sound Tip", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Like: ${tip.englishComparison}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(tip.description, style = MaterialTheme.typography.bodyMedium)
            
            if (tip.commonMistake.isNotBlank()) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = ErrorContainer.copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(spacing.sm), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(16.dp))
                        Text(tip.commonMistake, style = MaterialTheme.typography.bodySmall, color = ErrorRed)
                    }
                }
            }
        }
    }
}

@Composable
private fun MemoryHookCard(hook: MemoryHook) {
    val spacing = LocalSpacing.current
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = GoldAccentContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(spacing.md), verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Psychology, contentDescription = null, tint = OnGoldAccentContainer)
                Text(hook.targetWord, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = OnGoldAccentContainer)
            }
            Text(hook.story, style = MaterialTheme.typography.bodyMedium, color = OnGoldAccentContainer)
            if (hook.visualDescription.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = OnGoldAccentContainer.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                    Text(hook.visualDescription, style = MaterialTheme.typography.bodySmall, color = OnGoldAccentContainer.copy(alpha = 0.7f))
                }
            }
        }
    }
}

@Composable
private fun EmptyTabState(message: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(Icons.Default.Inbox, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.outlineVariant)
        Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun BottomActionBar(
    lesson: Lesson,
    onStartFlashCards: () -> Unit,
    onStartQuiz: () -> Unit,
    onStartPronunciation: () -> Unit,
    onMarkComplete: () -> Unit
) {
    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 12.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onStartFlashCards,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Style, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Review")
                }
                Button(
                    onClick = onStartQuiz,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Take Quiz")
                }
            }
            
            TextButton(
                onClick = onStartPronunciation,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = KoreanBlue)
            ) {
                Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Practice Pronunciation", fontWeight = FontWeight.Bold)
            }
        }
    }
}
