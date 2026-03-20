package com.koreancoach.app.ui.feature.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.R
import com.koreancoach.app.domain.model.Lesson
import com.koreancoach.app.ui.theme.LocalSpacing
import com.koreancoach.app.ui.theme.KoreanBlueContainer
import com.koreancoach.app.ui.theme.SuccessContainer
import com.koreancoach.app.ui.theme.SuccessGreen
import com.koreancoach.app.ui.theme.GoldAccent
import com.koreancoach.app.ui.theme.KoreanBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToLessons: () -> Unit,
    onNavigateToReview: () -> Unit,
    onNavigateToLesson: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToHangulWriting: () -> Unit,
    onNavigateToHangulPath: () -> Unit,
    onNavigateToHangulExplore: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        val greeting = when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
                            in 0..11 -> stringResource(R.string.greeting_morning)
                            in 12..17 -> stringResource(R.string.greeting_afternoon)
                            else -> stringResource(R.string.greeting_evening)
                        }
                        Text(
                            text = if (state.onboarding.learnerName.isNotBlank()) "$greeting, ${state.onboarding.learnerName}!" else stringResource(R.string.greeting_default),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            stringResource(R.string.dashboard_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToAnalytics) {
                        Icon(Icons.Default.BarChart, contentDescription = stringResource(R.string.cd_progress), tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.cd_settings))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            return@Scaffold
        }

        LazyColumn(
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = spacing.xxl,
                start = spacing.md,
                end = spacing.md
            ),
            verticalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            // Streak and Stats
            item {
                Spacer(Modifier.height(spacing.xs))
                StatsCard(state = state)
            }

            item {
                TodayFocusCard(
                    state = state,
                    onOpenHangulPath = onNavigateToHangulPath,
                    onOpenNextLesson = { state.nextSurvivalLesson?.let { onNavigateToLesson(it.id) } }
                )
            }

            item {
                HangulSprintCard(
                    completed = state.hangulCompletedCount,
                    total = state.hangulTotalCount,
                    onOpenPath = onNavigateToHangulPath,
                    onOpenExplore = onNavigateToHangulExplore
                )
            }

            // Review due notification
            if (state.reviewDueCount > 0) {
                item {
                    ReviewActionCard(
                        dueCount = state.reviewDueCount,
                        onClick = onNavigateToReview
                    )
                }
            }

            // Continue learning section
            item {
                SectionHeader(
                    title = stringResource(R.string.survival_korean),
                    actionText = stringResource(R.string.all_lessons),
                    onActionClick = onNavigateToLessons
                )
            }

            if (state.recentLessons.isEmpty()) {
                item { EmptyLessonsState(onNavigateToLessons) }
            } else {
                items(state.recentLessons, key = { it.id }) { lesson ->
                    LessonProgressCard(lesson = lesson, onClick = { onNavigateToLesson(lesson.id) })
                }
            }

            // Quick practice section
            item {
                Text(
                    text = stringResource(R.string.quick_practice),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = spacing.sm)
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.md)
                ) {
                    QuickActionCard(
                        emoji = "✍️",
                        title = stringResource(R.string.writing),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToHangulWriting
                    )
                    QuickActionCard(
                        emoji = "🎤",
                        title = stringResource(R.string.speaking),
                        color = KoreanBlueContainer,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToLessons
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        if (actionText != null && onActionClick != null) {
            TextButton(onClick = onActionClick) {
                Text(actionText, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun TodayFocusCard(
    state: DashboardUiState,
    onOpenHangulPath: () -> Unit,
    onOpenNextLesson: () -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(R.string.today), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            val focusText = when {
                state.nextHangulLesson != null -> "Continue ${state.nextHangulLesson.title}"
                state.nextSurvivalLesson != null -> "Continue ${state.nextSurvivalLesson.title}"
                else -> stringResource(R.string.today_focus_review)
            }
            Text(focusText, style = MaterialTheme.typography.bodyLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = if (state.nextHangulLesson != null) onOpenHangulPath else onOpenNextLesson) {
                    Text(if (state.nextHangulLesson != null) stringResource(R.string.open_hangul_sprint) else stringResource(R.string.open_lesson))
                }
                OutlinedButton(onClick = onOpenHangulPath) {
                    Text(stringResource(R.string.path))
                }
            }
        }
    }
}

@Composable
private fun HangulSprintCard(
    completed: Int,
    total: Int,
    onOpenPath: () -> Unit,
    onOpenExplore: () -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(stringResource(R.string.hangul_sprint), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(stringResource(R.string.stages_complete, completed, total), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("한글", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
            }
            LinearProgressIndicator(
                progress = { if (total == 0) 0f else completed.toFloat() / total },
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onOpenPath, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.guided_path))
                }
                OutlinedButton(onClick = onOpenExplore, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.explore_40))
                }
            }
        }
    }
}

@Composable
private fun StatsCard(state: DashboardUiState) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔥", fontSize = 20.sp)
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.day_streak, state.currentStreak),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (state.currentStreak > 0) stringResource(R.string.streak_keep_up) else stringResource(R.string.streak_start_today),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = stringResource(R.string.level_1),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                StatItem(
                    modifier = Modifier.weight(1f),
                    value = "${state.completedLessons}",
                    label = stringResource(R.string.lessons),
                    icon = Icons.Default.MenuBook,
                    color = MaterialTheme.colorScheme.primary
                )
                StatItem(
                    modifier = Modifier.weight(1f),
                    value = "${state.masteredCards}",
                    label = stringResource(R.string.mastered),
                    icon = Icons.Default.Star,
                    color = GoldAccent
                )
                StatItem(
                    modifier = Modifier.weight(1f),
                    value = "${state.weeklyMinutes}m",
                    label = stringResource(R.string.this_week),
                    icon = Icons.Default.Timer,
                    color = KoreanBlue
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ReviewActionCard(dueCount: Int, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🔔", fontSize = 22.sp)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.review_session_ready),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = stringResource(R.string.items_need_attention, dueCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
            }
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun LessonProgressCard(lesson: Lesson, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        if (lesson.isCompleted) SuccessContainer
                        else MaterialTheme.colorScheme.primaryContainer
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(lesson.emoji, fontSize = 28.sp)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = lesson.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LinearProgressIndicator(
                        progress = { if (lesson.isCompleted) 1f else 0.3f }, // Dummy progress for now
                        modifier = Modifier.weight(1f).height(4.dp).clip(CircleShape),
                        color = if (lesson.isCompleted) SuccessGreen else MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        text = if (lesson.isCompleted) stringResource(R.string.done) else "30%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = if (lesson.isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                contentDescription = null,
                tint = if (lesson.isCompleted) SuccessGreen else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    emoji: String,
    title: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = color,
        modifier = modifier.height(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(emoji, fontSize = 32.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EmptyLessonsState(onNavigateToLessons: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("📚", fontSize = 48.sp)
        Text(
            stringResource(R.string.empty_lessons_title),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            stringResource(R.string.empty_lessons_subtitle),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
