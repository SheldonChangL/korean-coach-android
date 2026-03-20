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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.data.local.dao.DailyPronunciationRow
import com.koreancoach.app.domain.model.DailyStudyMinutes
import com.koreancoach.app.domain.model.QuizAccuracyPoint
import com.koreancoach.app.ui.theme.LocalSpacing
import com.koreancoach.app.ui.theme.SuccessGreen
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
                title = { Text("Progress", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } },
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
                    label = "Day streak"
                )
                StatSummaryCard(
                    modifier = Modifier.weight(1f),
                    emoji = "⏱️",
                    value = "${state.weeklyTotalMinutes}",
                    label = "Min this week"
                )
                StatSummaryCard(
                    modifier = Modifier.weight(1f),
                    emoji = "🎯",
                    value = state.averageAccuracy?.let { "${it.toInt()}%" } ?: "—",
                    label = "Avg accuracy"
                )
            }

            // Pronunciation score headline
            state.overallPronunciationScore?.let { avg ->
                StatSummaryCard(
                    modifier = Modifier.fillMaxWidth(),
                    emoji = "🎤",
                    value = "${avg.toInt()}%",
                    label = "Overall pronunciation score"
                )
            }

            // Weekly study minutes chart
            AnalyticsCard(title = "Weekly Study Time") {
                if (state.weeklyStudyMinutes.isEmpty()) {
                    EmptyChartState(
                        icon = "📅",
                        message = "Complete your first lesson to see study time here!"
                    )
                } else {
                    StudyMinutesBarChart(data = state.weeklyStudyMinutes)
                }
            }

            // Quiz accuracy trend
            AnalyticsCard(title = "Quiz Accuracy Trend") {
                if (state.quizAccuracyTrend.isEmpty()) {
                    EmptyChartState(
                        icon = "📝",
                        message = "Take a quiz to track your accuracy over time!"
                    )
                } else {
                    AccuracyLineChart(data = state.quizAccuracyTrend)
                }
            }

            // Pronunciation trend (last 7 days)
            AnalyticsCard(title = "Pronunciation Score — Last 7 Days") {
                if (state.weeklyPronunciationTrend.isEmpty()) {
                    EmptyChartState(
                        icon = "🎤",
                        message = "Practice pronunciation to see your score trend here!"
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
    val dayFormatter = DateTimeFormatter.ofPattern("EEE").withZone(ZoneId.systemDefault())
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
                    "${day.minutes}m",
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
            val dateLabel = DateTimeFormatter.ofPattern("MMM d")
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
    val dayFormatter = DateTimeFormatter.ofPattern("EEE").withZone(ZoneId.systemDefault())
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
