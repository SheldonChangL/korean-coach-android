package com.koreancoach.app.ui.feature.lessons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.R
import com.koreancoach.app.domain.model.Lesson
import com.koreancoach.app.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonListScreen(
    onLessonClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: LessonListViewModel = hiltViewModel()
) {
    val lessonsByWeek by viewModel.lessonsByWeek.collectAsState()
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.all_lessons), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        if (lessonsByWeek.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📖", fontSize = 48.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        stringResource(R.string.lesson_list_loading),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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
            verticalArrangement = Arrangement.spacedBy(spacing.xs)
        ) {
            lessonsByWeek.forEach { (week, lessons) ->
                item(key = "week_$week") {
                    WeekHeader(week = week, lessons = lessons)
                }
                items(lessons, key = { it.id }) { lesson ->
                    LessonRow(lesson = lesson, onClick = { if (lesson.isUnlocked) onLessonClick(lesson.id) })
                }
                item { Spacer(Modifier.height(spacing.sm)) }
            }
        }
    }
}

@Composable
private fun WeekHeader(week: Int, lessons: List<Lesson>) {
    val completed = lessons.count { it.isCompleted }
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            stringResource(R.string.week_title, week),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            stringResource(R.string.week_progress, completed, lessons.size),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LessonRow(lesson: Lesson, onClick: () -> Unit) {
    val spacing = LocalSpacing.current
    val isLocked = !lesson.isUnlocked
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = when {
            lesson.isCompleted -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
            isLocked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else -> MaterialTheme.colorScheme.surface
        },
        tonalElevation = if (isLocked) 0.dp else 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (isLocked) MaterialTheme.colorScheme.outlineVariant
                        else if (lesson.isCompleted) MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.primaryContainer
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isLocked) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = stringResource(R.string.cd_locked),
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(lesson.emoji, fontSize = 22.sp)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isLocked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = lesson.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (isLocked) 0.5f else 1f)
                )
                Text(
                    text = stringResource(R.string.lesson_day_minutes, lesson.dayNumber, lesson.estimatedMinutes),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (isLocked) 0.5f else 1f)
                )
            }
            when {
                lesson.isCompleted -> Icon(Icons.Default.CheckCircle, contentDescription = stringResource(R.string.cd_completed), tint = MaterialTheme.colorScheme.tertiary)
                isLocked -> Icon(Icons.Default.Lock, contentDescription = stringResource(R.string.cd_locked), tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(18.dp))
                else -> Icon(Icons.Default.PlayArrow, contentDescription = stringResource(R.string.cd_start), tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
