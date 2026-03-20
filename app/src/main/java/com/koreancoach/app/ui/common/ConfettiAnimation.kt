package com.koreancoach.app.ui.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.*
import kotlin.random.Random

private val confettiColors = listOf(
    Color(0xFFE63946), // Korean red
    Color(0xFFF4A261), // Gold
    Color(0xFF2A9D8F), // Teal
    Color(0xFF9B5DE5), // Purple
    Color(0xFFFFD700), // Yellow
    Color(0xFF00B4D8), // Sky blue
    Color(0xFFFF6B6B), // Coral
    Color(0xFF06D6A0)  // Mint
)

private data class Particle(
    val x: Float,
    val startY: Float,
    val color: Color,
    val size: Float,
    val speed: Float,
    val wobble: Float,
    val rotation: Float
)

@Composable
fun ConfettiOverlay(
    active: Boolean,
    modifier: Modifier = Modifier
) {
    if (!active) return

    val particles = remember {
        List(80) {
            Particle(
                x = Random.nextFloat(),
                startY = -Random.nextFloat() * 0.3f,
                color = confettiColors.random(),
                size = Random.nextFloat() * 12f + 6f,
                speed = Random.nextFloat() * 0.4f + 0.3f,
                wobble = Random.nextFloat() * 4f - 2f,
                rotation = Random.nextFloat() * 360f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confetti_progress"
    )

    Canvas(modifier = modifier) {
        particles.forEach { p ->
            val elapsed = (progress + p.startY + 0.3f) % 1f
            val px = (p.x + sin(elapsed * 2 * PI.toFloat() * p.wobble) * 0.06f) * size.width
            val py = elapsed * size.height * 1.2f - size.height * 0.1f

            if (py < size.height) {
                drawCircle(
                    color = p.color.copy(alpha = (1f - elapsed * 0.8f).coerceIn(0f, 1f)),
                    radius = p.size,
                    center = Offset(px, py)
                )
            }
        }
    }
}
