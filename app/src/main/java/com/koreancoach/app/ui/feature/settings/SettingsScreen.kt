package com.koreancoach.app.ui.feature.settings

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.R
import com.koreancoach.app.domain.model.SpeechRatePreset
import com.koreancoach.app.domain.model.ThemeMode
import com.koreancoach.app.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title), fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, stringResource(R.string.back)) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.lg)
        ) {
            // Profile / User Section
            SettingsSection(title = stringResource(R.string.learning_profile)) {
                Text(
                    text = stringResource(R.string.settings_name_format, state.learnerName.ifBlank { stringResource(R.string.not_set) }),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(R.string.settings_goal_format, state.dailyGoalMinutes),
                    style = MaterialTheme.typography.bodyLarge
                )
                Slider(
                    value = state.dailyGoalMinutes.toFloat(),
                    onValueChange = { viewModel.updateDailyGoal(it.toInt()) },
                    valueRange = 5f..60f,
                    steps = 11
                )
            }

            // Reminders Section
            SettingsSection(title = stringResource(R.string.reminders)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(stringResource(R.string.daily_notifications), style = MaterialTheme.typography.bodyLarge)
                        Text(
                            stringResource(R.string.daily_notifications_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = state.reminderEnabled,
                        onCheckedChange = { viewModel.toggleReminders(it) }
                    )
                }
                
                if (state.reminderEnabled) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.reminder_time), style = MaterialTheme.typography.bodyLarge)
                        TextButton(onClick = { /* Show time picker */ }) {
                            Text(
                                String.format("%02d:%02d", state.reminderHour, state.reminderMinute),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Appearance Section
            SettingsSection(title = stringResource(R.string.appearance)) {
                Text(stringResource(R.string.theme), style = MaterialTheme.typography.bodyLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                ) {
                    ThemeMode.entries.forEach { mode ->
                        FilterChip(
                            selected = state.themeMode == mode,
                            onClick = { viewModel.updateTheme(mode) },
                            label = { Text(mode.label) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            SettingsSection(title = stringResource(R.string.speech)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.autoplay_hangul), style = MaterialTheme.typography.bodyLarge)
                        Text(
                            stringResource(R.string.autoplay_hangul_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = state.autoPlayHangul,
                        onCheckedChange = { viewModel.updateAutoPlayHangul(it) }
                    )
                }

                Text(stringResource(R.string.playback_speed_label), style = MaterialTheme.typography.bodyLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                ) {
                    SpeechRatePreset.entries.forEach { rate ->
                        FilterChip(
                            selected = state.speechRatePreset == rate,
                            onClick = { viewModel.updateSpeechRate(rate) },
                            label = { Text(rate.name.lowercase().replaceFirstChar { it.uppercase() }) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Text(stringResource(R.string.korean_voice), style = MaterialTheme.typography.bodyLarge)
                if (!state.speechReady) {
                    Text(
                        stringResource(R.string.no_tts_error),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else if (state.availableVoices.isEmpty()) {
                    Text(
                        stringResource(R.string.no_voice_error),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    state.availableVoices.forEach { voice ->
                        FilterChip(
                            selected = state.preferredVoiceKey == voice.key,
                            onClick = { viewModel.updatePreferredVoiceKey(voice.key) },
                            label = { Text(voice.label) }
                        )
                    }
                }
            }

            // About Section
            SettingsSection(title = stringResource(R.string.about)) {
                Text(stringResource(R.string.app_version), style = MaterialTheme.typography.bodyMedium)
                Text(
                    stringResource(R.string.about_description),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(spacing.xl))
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        content()
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }
}
