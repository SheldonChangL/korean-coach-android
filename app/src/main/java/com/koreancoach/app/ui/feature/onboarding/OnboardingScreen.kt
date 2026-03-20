package com.koreancoach.app.ui.feature.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreancoach.app.domain.model.LearningReason
import com.koreancoach.app.domain.model.StudyTime
import com.koreancoach.app.ui.theme.LocalSpacing

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    LaunchedEffect(state.isComplete) {
        if (state.isComplete) onComplete()
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
                        (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
                    } else {
                        (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it } + fadeOut())
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
                        OnboardingPage.READY -> ReadyPage(name = state.name)
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
                        Text("Back", style = MaterialTheme.typography.labelLarge)
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
                        .then(if (isLastPage) Modifier.fillMaxWidth(0.7f) else Modifier.width(140.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLastPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        if (isLastPage) "Let's start! 시작!" else "Continue",
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
                    .clip(RoundedCornerShape(50))
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
            text = "Hello!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        Spacer(Modifier.height(spacing.lg))
        Text(
            text = "Master practical Korean with memory hacks that stick. 10 minutes a day is all you need.",
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
            text = "What should we call you?",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(spacing.sm))
        Text(
            text = "We'll use this to cheer you on!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(spacing.xxl))
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Your name") },
            placeholder = { Text("e.g. Alex") },
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
            text = "Why learn Korean?",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "We'll tailor your journey to your goals.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(Modifier.height(spacing.xl))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            items(LearningReason.entries) { reason ->
                OnboardingOptionCard(
                    emoji = reason.emoji,
                    label = reason.label,
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
            text = "Study time preference",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Habits are easier when they're consistent.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(Modifier.height(spacing.xl))
        StudyTime.entries.forEach { time ->
            OnboardingOptionCard(
                emoji = time.emoji,
                label = time.label,
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
    val options = listOf(5 to "Quick (5 min)", 10 to "Standard (10 min)", 15 to "Dedicated (15 min)", 20 to "Intensive (20 min)")
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Set your daily goal",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Even 5 minutes a day builds fluency.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(Modifier.height(spacing.xl))
        options.forEach { (minutes, label) ->
            OnboardingOptionCard(
                emoji = when (minutes) { 5 -> "⚡"; 10 -> "⭐"; 15 -> "🔥"; else -> "💪" },
                label = label,
                isSelected = goalMinutes == minutes,
                onClick = { onGoalChange(minutes) }
            )
            Spacer(Modifier.height(spacing.sm))
        }
    }
}

@Composable
private fun ReadyPage(name: String) {
    val spacing = LocalSpacing.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(SuccessGreen.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text("🎉", fontSize = 64.sp)
        }
        Spacer(Modifier.height(spacing.xl))
        Text(
            text = if (name.isNotBlank()) "You're ready, $name!" else "You're all set!",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(spacing.md))
        Text(
            text = "Your personalized learning path is ready. Let's start with your very first lesson.",
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
                    Text("Today's First Lesson:", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f))
                    Text("Hello, Korea!", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSecondaryContainer)
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
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
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
                    .clip(CircleShape)
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
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
        }
    }
}
