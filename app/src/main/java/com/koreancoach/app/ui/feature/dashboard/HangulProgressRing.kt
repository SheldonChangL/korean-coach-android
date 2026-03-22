package com.koreancoach.app.ui.feature.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.koreancoach.app.R
import com.koreancoach.app.ui.theme.*

@Composable
fun HangulProgressRing(
    masteredVowels: Int,
    totalVowels: Int,
    masteredConsonants: Int,
    totalConsonants: Int,
    masteredDoubleConsonants: Int,
    totalDoubleConsonants: Int,
    masteredCompoundVowels: Int,
    totalCompoundVowels: Int,
    onNavigateToExplore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalMastered = masteredVowels + masteredConsonants + masteredDoubleConsonants + masteredCompoundVowels
    val totalCount = totalVowels + totalConsonants + totalDoubleConsonants + totalCompoundVowels

    val animatedMasteredVowels by animateFloatAsState(targetValue = masteredVowels.toFloat(), label = "vowels")
    val animatedMasteredConsonants by animateFloatAsState(targetValue = masteredConsonants.toFloat(), label = "consonants")
    val animatedMasteredDoubleConsonants by animateFloatAsState(targetValue = masteredDoubleConsonants.toFloat(), label = "double")
    val animatedMasteredCompoundVowels by animateFloatAsState(targetValue = masteredCompoundVowels.toFloat(), label = "compound")

    Box(
        modifier = modifier
            .size(160.dp)
            .clickable { onNavigateToExplore() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 16.dp.toPx()
            val innerRadius = size.minDimension / 2 - strokeWidth
            
            // Background Ring
            drawCircle(
                color = OutlineVariant.copy(alpha = 0.3f),
                radius = innerRadius,
                style = Stroke(width = strokeWidth)
            )

            var startAngle = -90f

            // Vowels (KoreanRed)
            val vowelAngle = (totalVowels.toFloat() / totalCount) * 360f
            val vowelProgressAngle = (animatedMasteredVowels / totalVowels) * vowelAngle
            drawArc(
                color = KoreanRed,
                startAngle = startAngle,
                sweepAngle = vowelProgressAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            startAngle += vowelAngle

            // Consonants (KoreanBlue)
            val consonantAngle = (totalConsonants.toFloat() / totalCount) * 360f
            val consonantProgressAngle = (animatedMasteredConsonants / totalConsonants) * consonantAngle
            drawArc(
                color = KoreanBlue,
                startAngle = startAngle,
                sweepAngle = consonantProgressAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            startAngle += consonantAngle

            // Double Consonants (GoldAccent)
            val doubleAngle = (totalDoubleConsonants.toFloat() / totalCount) * 360f
            val doubleProgressAngle = (animatedMasteredDoubleConsonants / totalDoubleConsonants) * doubleAngle
            drawArc(
                color = GoldAccent,
                startAngle = startAngle,
                sweepAngle = doubleProgressAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            startAngle += doubleAngle

            // Compound Vowels (TealAccent)
            val compoundAngle = (totalCompoundVowels.toFloat() / totalCount) * 360f
            val compoundProgressAngle = (animatedMasteredCompoundVowels / totalCompoundVowels) * compoundAngle
            drawArc(
                color = TealAccent,
                startAngle = startAngle,
                sweepAngle = compoundProgressAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$totalMastered",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "/ $totalCount",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.sounds_mastered),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
