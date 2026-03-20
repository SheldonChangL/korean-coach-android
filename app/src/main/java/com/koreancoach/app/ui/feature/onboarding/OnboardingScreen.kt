package com.koreancoach.app.ui.feature.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.R
import com.koreancoach.app.domain.model.HangulLevel
import com.koreancoach.app.domain.model.LearningReason
import com.koreancoach.app.domain.model.SpeechRatePreset
import com.koreancoach.app.domain.model.StudyTime
import com.koreancoach.app.domain.model.UiLanguage
import com.koreancoach.app.ui.theme.LocalSpacing

@Composable
fun OnboardingScreen(
    onComplete: (Boolean) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    LaunchedEffect(state.isComplete) {
        if (state.isComplete) onComplete(state.hangulLevel != HangulLevel.CAN_READ)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = spacing.md)
        ) {
            // Progress Header
            Spacer(modifier = Modifier.height(spacing.lg))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OnboardingProgressDots(
                    currentPage = state.currentPage,
                    totalPages = OnboardingPage.entries.size
                )
            }
            Spacer(modifier = Modifier.height(spacing.xl))

            // Page content with smooth transitions
            AnimatedContent(
                targetState = state.currentPage,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { it } + fadeIn()).togetherWith(
                            slideOutHorizontally { -it } + fadeOut())
                    } else {
                        (slideInHorizontally { -it } + fadeIn()).togetherWith(
                            slideOutHorizontally { it } + fadeOut())
                    } using SizeTransform(clip = false)
                },
                modifier = Modifier.weight(1f),
                label = "onboarding_page"
            ) { page ->
                Box(modifier = Modifier.fillMaxSize()) {
                    when (OnboardingPage.entries[page]) {
                        OnboardingPage.WELCOME -> WelcomePage()
                        OnboardingPage.NAME -> NamePage(
                            name = state.name,
                            onNameChange = viewModel::setName,
                            onContinue = viewModel::nextPage
                        )

                        OnboardingPage.REASON -> ReasonPage(
                            selected = state.selectedReason,
                            onSelect = viewModel::setReason
                        )

                        OnboardingPage.TIME -> StudyTimePage(
                            selected = state.selectedTime,
                            onSelect = viewModel::setStudyTime
                        )

                        OnboardingPage.GOAL -> GoalPage(
                            goalMinutes = state.dailyGoalMinutes,
                            onGoalChange = viewModel::setDailyGoal
                        )

                        OnboardingPage.LANGUAGE -> LanguagePage(
                            selected = state.uiLanguage,
                            onSelect = viewModel::setUiLanguage
                        )

                        OnboardingPage.HANGUL -> HangulLevelPage(
                            selected = state.hangulLevel,
                            onSelect = viewModel::setHangulLevel
                        )

                        OnboardingPage.SPEECH -> SpeechSetupPage(
                            autoPlayHangul = state.autoPlayHangul,
                            selectedRate = state.speechRatePreset,
                            onToggleAutoPlay = viewModel::setAutoPlayHangul,
                            onSelectRate = viewModel::setSpeechRate
                        )

                        OnboardingPage.READY -> ReadyPage(
                            name = state.name,
                            hangulLevel = state.hangulLevel
                        )
                    }
                }
            }

            // Navigation buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing.xl),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.currentPage > 0) {
                    TextButton(
                        onClick = viewModel::prevPage,
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
                    ) {
                        Text(
                            stringResource(R.string.back),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(64.dp))
                }

                val isLastPage = state.currentPage == OnboardingPage.entries.size - 1
                Button(
                    onClick = if (isLastPage) viewModel::completeOnboarding else viewModel::nextPage,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .height(56.dp)
                        .then(
                            if (isLastPage) Modifier.fillMaxWidth(0.7f) else Modifier.width(
                                140.dp
                            )
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLastPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        if (isLastPage) stringResource(R.string.onboarding_start) else stringResource(
                            R.string.continue_button
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingProgressDots(
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalPages) { index ->
            val isSelected = index == currentPage
            Box(
                modifier = Modifier
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant
                    )
                    .size(if (isSelected) 24.dp else 8.dp, 8.dp)
            )
        }
    }
}

@Composable
private fun WelcomePage() {
    val spacing = LocalSpacing.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text("🇰🇷", fontSize = 84.sp)
        }
        Spacer(Modifier.height(spacing.xl))
        Text(
            text = "안녕하세요!",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.welcome_hello),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        Spacer(Modifier.height(spacing.lg))
        Text(
            text = stringResource(R.string.welcome_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = spacing.md),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun NamePage(
    name: String,
    onNameChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    val spacing = LocalSpacing.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(spacing.xl))
        Text("👋", fontSize = 64.sp)
        Spacer(Modifier.height(spacing.lg))
        Text(
            text = stringResource(R.string.name_page_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(spacing.sm))
        Text(
            text = stringResource(R.string.name_page_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(spacing.xxl))
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.your_name_label)) },
            placeholder = { Text(stringResource(R.string.name_placeholder)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onContinue()
                }
            )
        )
    }
}

@Composable
private fun ReasonPage(
    selected: LearningReason,
    onSelect: (LearningReason) -> Unit
) {
    val spacing = LocalSpacing.current
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.reason_page_title),
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(R.string.reason_page_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(Modifier.height(spacing.xl))
        androidx.compose.foundation.lazy.LazyColumn(
            verticalArrangement = Arrangement.spacedBy(
                spacing.sm
            )
        ) {
            items(LearningReason.entries) { reason ->
                OnboardingOptionCard(
                    emoji = reason.emoji,
                    label = stringResource(reason.labelRes),
                    isSelected = reason == selected,
                    onClick = { onSelect(reason) }
                )
            }
        }
    }
}

@Composable
private fun StudyTimePage(
    selected: StudyTime,
    onSelect: (StudyTime) -> Unit
) {
    val spacing = LocalSpacing.current
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.time_page_title),
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(R.string.time_page_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(Modifier.height(spacing.xl))
        StudyTime.entries.forEach { time ->
            OnboardingOptionCard(
                emoji = time.emoji,
                label = stringResource(time.labelRes),
                isSelected = time == selected,
                onClick = { onSelect(time) }
            )
            Spacer(Modifier.height(spacing.sm))
        }
    }
}

@Composable
private fun GoalPage(
    goalMinutes: Int,
    onGoalChange: (Int) -> Unit
) {
    val spacing = LocalSpacing.current
    val options = listOf(
        5 to stringResource(R.string.goal_quick),
        10 to stringResource(R.string.goal_standard),
        15 to stringResource(R.string.goal_dedicated),
        20 to stringResource(R.string.goal_intensive)
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.goal_page_title),
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(R.string.goal_page_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(Modifier.height(spacing.xl))
        options.forEach { (minutes, label) ->
            OnboardingOptionCard(
                emoji = when (minutes) {
                    5 -> "⚡"; 10 -> "⭐"; 15 -> "🔥"; else -> "💪"
                },
                label = label,
                isSelected = goalMinutes == minutes,
                onClick = { onGoalChange(minutes) }
            )
            Spacer(Modifier.height(spacing.sm))
        }
    }
}

@Composable
private fun LanguagePage(
    selected: UiLanguage,
    onSelect: (UiLanguage) -> Unit
) {
    val spacing = LocalSpacing.current
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.language_page_title),
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(R.string.language_page_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(Modifier.height(spacing.xl))
        UiLanguage.entries.forEach { language ->
            OnboardingOptionCard(
                emoji = if (language == UiLanguage.TRADITIONAL_CHINESE) {
                    stringResource(R.string.lang_badge_chinese)
                } else {
                    stringResource(R.string.lang_badge_english)
                },
                label = stringResource(language.labelRes),
                isSelected = language == selected,
                onClick = { onSelect(language) }
            )
            Spacer(Modifier.height(spacing.sm))
        }
    }
}

@Composable
private fun HangulLevelPage(
    selected: HangulLevel,
    onSelect: (HangulLevel) -> Unit
) {
    val spacing = LocalSpacing.current
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.hangul_page_title),
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(R.string.hangul_page_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(Modifier.height(spacing.xl))
        HangulLevel.entries.forEach { level ->
            OnboardingOptionCard(
                emoji = level.emoji,
                label = stringResource(level.labelRes),
                isSelected = level == selected,
                onClick = { onSelect(level) }
            )
            Spacer(Modifier.height(spacing.sm))
        }
    }
}

@Composable
private fun SpeechSetupPage(
    autoPlayHangul: Boolean,
    selectedRate: SpeechRatePreset,
    onToggleAutoPlay: (Boolean) -> Unit,
    onSelectRate: (SpeechRatePreset) -> Unit
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(spacing.lg)
    ) {
        Text(
            text = stringResource(R.string.speech_setup_title),
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(R.string.speech_setup_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OnboardingOptionCard(
            emoji = if (autoPlayHangul) "🔊" else "🔈",
            label = if (autoPlayHangul) stringResource(R.string.autoplay_on) else stringResource(R.string.autoplay_off),
            isSelected = autoPlayHangul,
            onClick = { onToggleAutoPlay(!autoPlayHangul) }
        )
        Text(
            stringResource(R.string.playback_speed),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        SpeechRatePreset.entries.forEach { rate ->
            OnboardingOptionCard(
                emoji = if (rate == SpeechRatePreset.SLOW) "🐢" else "⚡",
                label = stringResource(rate.labelRes),
                isSelected = rate == selectedRate,
                onClick = { onSelectRate(rate) }
            )
            Spacer(Modifier.height(spacing.sm))
        }
    }
}

@Composable
private fun ReadyPage(
    name: String,
    hangulLevel: HangulLevel
) {
    val spacing = LocalSpacing.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(com.koreancoach.app.ui.theme.SuccessGreen.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text("🎉", fontSize = 64.sp)
        }
        Spacer(Modifier.height(spacing.xl))
        Text(
            text = if (name.isNotBlank()) stringResource(
                R.string.ready_title_named,
                name
            ) else stringResource(R.string.ready_title_all_set),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(spacing.md))
        Text(
            text = if (hangulLevel == HangulLevel.CAN_READ)
                stringResource(R.string.ready_subtitle_read)
            else
                stringResource(R.string.ready_subtitle_learn),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(spacing.xxl))
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.md)
            ) {
                Text("🇰🇷", fontSize = 32.sp)
                Column {
                    Text(
                        stringResource(R.string.starting_path_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        if (hangulLevel == HangulLevel.CAN_READ) stringResource(R.string.survival_korean)
                        else stringResource(R.string.hangul_sprint),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingOptionCard(
    emoji: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isSelected) 0.dp else 1.dp,
        border = if (isSelected) BorderStroke(
            2.dp,
            MaterialTheme.colorScheme.primary
        ) else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 20.sp)
            }
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
            )
            if (isSelected) {
                Spacer(Modifier.weight(1f))
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
