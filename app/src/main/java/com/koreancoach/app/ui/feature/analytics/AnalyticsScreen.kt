package com.koreancoach.app.ui.feature.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.R
import com.koreancoach.app.data.local.dao.DailyPronunciationRow
import com.koreancoach.app.domain.model.DailyStudyMinutes
import com.koreancoach.app.domain.model.QuizAccuracyPoint
import com.koreancoach.app.ui.theme.LocalSpacing
import com.koreancoach.app.ui.theme.SuccessGreen
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onBack: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.analytics_title), fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, stringResource(R.string.back)) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            // Headline stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.sm)
            ) {
                StatSummaryCard(
                    modifier = Modifier.weight(1f),
                    emoji = "🔥",
                    value = "${state.currentStreak}",
                    label = stringResource(R.string.analytics_day_streak)
                )
                StatSummaryCard(
                    modifier = Modifier.weight(1f),
                    emoji = "⏱️",
                    value = "${state.weeklyTotalMinutes}",
                    label = stringResource(R.string.analytics_minutes_this_week)
                )
                StatSummaryCard(
                    modifier = Modifier.weight(1f),
                    emoji = "🎯",
                    value = state.averageAccuracy?.let { "${it.toInt()}%" } ?: "—",
                    label = stringResource(R.string.analytics_avg_accuracy)
                )
            }

            // Pronunciation score headline
            state.overallPronunciationScore?.let { avg ->
                StatSummaryCard(
                    modifier = Modifier.fillMaxWidth(),
                    emoji = "🎤",
                    value = "${avg.toInt()}%",
                    label = stringResource(R.string.analytics_overall_pronunciation_score)
                )
            }

            // Weekly study minutes chart
            AnalyticsCard(title = stringResource(R.string.analytics_weekly_study_time)) {
                if (state.weeklyStudyMinutes.isEmpty()) {
                    EmptyChartState(
                        icon = "📅",
                        message = stringResource(R.string.analytics_empty_study_time)
                    )
                } else {
                    StudyMinutesBarChart(data = state.weeklyStudyMinutes)
                }
            }

            // Quiz accuracy trend
            AnalyticsCard(title = stringResource(R.string.analytics_quiz_accuracy_trend)) {
                if (state.quizAccuracyTrend.isEmpty()) {
                    EmptyChartState(
                        icon = "📝",
                        message = stringResource(R.string.analytics_empty_quiz_accuracy)
                    )
                } else {
                    AccuracyLineChart(data = state.quizAccuracyTrend)
                }
            }

            // Pronunciation trend (last 7 days)
            AnalyticsCard(title = stringResource(R.string.analytics_pronunciation_last_7_days)) {
                if (state.weeklyPronunciationTrend.isEmpty()) {
                    EmptyChartState(
                        icon = "🎤",
                        message = stringResource(R.string.analytics_empty_pronunciation)
                    )
                } else {
                    PronunciationTrendChart(data = state.weeklyPronunciationTrend)
                }
            }

            Spacer(Modifier.height(spacing.xl))
        }
    }
}

@Composable
private fun StatSummaryCard(
    modifier: Modifier = Modifier,
    emoji: String,
    value: String,
    label: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(emoji, fontSize = 24.sp)
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun AnalyticsCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            content()
        }
    }
}

@Composable
private fun EmptyChartState(icon: String, message: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(icon, fontSize = 36.sp)
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StudyMinutesBarChart(data: List<DailyStudyMinutes>) {
    val maxMinutes = data.maxOfOrNull { it.minutes } ?: 1
    val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault()).withZone(ZoneId.systemDefault())
    val barColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier.fillMaxWidth().height(120.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { day ->
            val fraction = (day.minutes.toFloat() / maxMinutes).coerceIn(0.05f, 1f)
            val dayLabel = dayFormatter.format(Instant.ofEpochMilli(day.dayEpoch * 86400000L))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    stringResource(R.string.minutes_short, day.minutes),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .fillMaxHeight(fraction)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(barColor)
                )
                Spacer(Modifier.height(4.dp))
                Text(dayLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun AccuracyLineChart(data: List<QuizAccuracyPoint>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        data.forEach { point ->
            val dateLabel = DateTimeFormatter.ofPattern("MMM d", Locale.getDefault())
                .withZone(ZoneId.systemDefault())
                .format(Instant.ofEpochMilli(point.completedAtMs))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    dateLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(40.dp)
                )
                LinearProgressIndicator(
                    progress = { point.accuracy / 100f },
                    modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = when {
                        point.accuracy >= 80 -> SuccessGreen
                        point.accuracy >= 60 -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.error
                    },
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text(
                    "${point.accuracy}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = when {
                        point.accuracy >= 80 -> SuccessGreen
                        point.accuracy >= 60 -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.error
                    },
                    modifier = Modifier.width(36.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun PronunciationTrendChart(data: List<DailyPronunciationRow>) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault()).withZone(ZoneId.systemDefault())
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        data.forEach { row ->
            val dayLabel = dayFormatter.format(Instant.ofEpochMilli(row.dayEpoch * 86400000L))
            val score = row.avgScore.toInt().coerceIn(0, 100)
            val barColor = when {
                score >= 80 -> SuccessGreen
                score >= 60 -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.error
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    dayLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(40.dp)
                )
                LinearProgressIndicator(
                    progress = { score / 100f },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = barColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text(
                    "$score%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = barColor,
                    modifier = Modifier.width(36.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
