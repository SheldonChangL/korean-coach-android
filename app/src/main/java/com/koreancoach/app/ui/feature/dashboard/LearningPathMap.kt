package com.koreancoach.app.ui.feature.dashboard

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreancoach.app.domain.model.Lesson
import com.koreancoach.app.ui.theme.KoreanRed
import com.koreancoach.app.ui.theme.KoreanBlue

@Composable
fun LearningPathMap(
    lessons: List<Lesson>,
    onLessonClick: (Lesson) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(lessons) { index, lesson ->
            val isLast = index == lessons.size - 1
            PathNode(
                lesson = lesson,
                isLast = isLast,
                onClick = { onLessonClick(lesson) }
            )
        }
    }
}

@Composable
fun PathNode(
    lesson: Lesson,
    isLast: Boolean,
    onClick: () -> Unit
) {
    val isInProgress = lesson.isUnlocked && !lesson.isCompleted
    val isLocked = !lesson.isUnlocked
    val isCompleted = lesson.isCompleted

    val nodeColor by animateColorAsState(
        targetValue = when {
            isCompleted -> KoreanBlue
            isInProgress -> KoreanRed
            else -> Color.LightGray
        },
        label = "nodeColor"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp)
        ) {
            // Glow effect for in-progress
            if (isInProgress) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(nodeColor.copy(alpha = pulseAlpha))
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(nodeColor)
                    .shadow(if (isInProgress) 8.dp else 0.dp, CircleShape)
                    .clickable(enabled = !isLocked) { onClick() }
            ) {
                Text(
                    text = lesson.emoji,
                    fontSize = 24.sp
                )
            }
            
            if (isCompleted) {
                // Small checkmark could be added here
            }
        }

        Text(
            text = lesson.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (isLocked) Color.Gray else MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
        
        Text(
            text = lesson.subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (!isLast) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .background(if (isCompleted) KoreanBlue else Color.LightGray)
            )
        }
    }
}
