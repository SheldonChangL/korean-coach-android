package com.koreancoach.app.ui.feature.hangul.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreancoach.app.R
import com.koreancoach.app.ui.common.ConfettiOverlay
import com.koreancoach.app.ui.theme.LocalSpacing
import kotlinx.coroutines.delay

// Initial consonants ordered by Korean syllable composition index
private val INITIAL_CONSONANTS = listOf(
    "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ",
    "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
)

// Vowels ordered by Korean syllable composition index
private val COMPOSITION_VOWELS = listOf(
    "ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ",
    "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"
)

// Subset of basic consonants used as distractor options
private val SIMPLE_CONSONANTS = listOf(
    "ㄱ", "ㄴ", "ㄷ", "ㄹ", "ㅁ", "ㅂ", "ㅅ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
)

// Subset of basic vowels used as distractor options
private val SIMPLE_VOWELS = listOf(
    "ㅏ", "ㅓ", "ㅗ", "ㅜ", "ㅡ", "ㅣ", "ㅑ", "ㅕ", "ㅛ", "ㅠ"
)

private fun decomposeSyllable(syllable: Char): Triple<Int, Int, Int>? {
    val code = syllable.code
    if (code < 0xAC00 || code > 0xD7A3) return null
    val offset = code - 0xAC00
    return Triple(offset / (21 * 28), (offset / 28) % 21, offset % 28)
}

/** Combine a consonant jamo and vowel jamo into a Korean syllable block (no final consonant). */
internal fun composeSyllable(consonant: String, vowel: String): String? {
    val ci = INITIAL_CONSONANTS.indexOf(consonant)
    val vi = COMPOSITION_VOWELS.indexOf(vowel)
    if (ci < 0 || vi < 0) return null
    return (0xAC00 + ci * 21 * 28 + vi * 28).toChar().toString()
}

/**
 * Returns (consonantOptions, vowelOptions) for a target syllable, or null when the syllable
 * is not a simple CV block (no final consonant, basic consonant, basic vowel).
 * The returned lists include the correct option shuffled among distractors.
 */
fun syllableOptionsFor(targetSyllable: String): Pair<List<String>, List<String>>? {
    val char = targetSyllable.firstOrNull() ?: return null
    val (ci, vi, fi) = decomposeSyllable(char) ?: return null
    if (fi != 0) return null // skip syllables with a final (batchim) consonant
    val correctConsonant = INITIAL_CONSONANTS.getOrNull(ci) ?: return null
    val correctVowel = COMPOSITION_VOWELS.getOrNull(vi) ?: return null
    if (correctConsonant !in SIMPLE_CONSONANTS) return null
    if (correctVowel !in SIMPLE_VOWELS) return null

    val consonantPool = SIMPLE_CONSONANTS.filter { it != correctConsonant }.shuffled().take(3)
    val vowelPool = SIMPLE_VOWELS.filter { it != correctVowel }.shuffled().take(3)

    return (consonantPool + correctConsonant).shuffled() to (vowelPool + correctVowel).shuffled()
}

/**
 * Interactive syllable-building exercise: tap a consonant and a vowel to form the target syllable.
 *
 * @param targetSyllable The syllable the learner should build (e.g. "가").
 * @param consonantOptions Shuffled list of consonant options (includes the correct one).
 * @param vowelOptions Shuffled list of vowel options (includes the correct one).
 * @param onSpeak Called with the Korean text for TTS feedback on a correct answer.
 * @param onComplete Called once when the correct combination is selected.
 */
@Composable
fun SyllableBuilder(
    targetSyllable: String,
    consonantOptions: List<String>,
    vowelOptions: List<String>,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier,
    onComplete: () -> Unit = {}
) {
    val spacing = LocalSpacing.current

    var selectedConsonant by remember(targetSyllable) { mutableStateOf<String?>(null) }
    var selectedVowel by remember(targetSyllable) { mutableStateOf<String?>(null) }
    var showConfetti by remember { mutableStateOf(false) }

    val previewSyllable = if (selectedConsonant != null && selectedVowel != null)
        composeSyllable(selectedConsonant!!, selectedVowel!!) else null
    val isCorrect: Boolean? = previewSyllable?.let { it == targetSyllable }
    val isLocked = isCorrect == true

    LaunchedEffect(isCorrect) {
        if (isCorrect == true) {
            showConfetti = true
            onSpeak(targetSyllable)
            onComplete()
            delay(2500)
            showConfetti = false
        }
    }

    Box(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            Text(
                stringResource(R.string.syllable_builder_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Target syllable display
            Text(
                stringResource(R.string.syllable_builder_target),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = targetSyllable,
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Current selection equation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.xs, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SyllableSlot(label = selectedConsonant ?: "?", isSelected = selectedConsonant != null)
                Text(
                    " + ",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                SyllableSlot(label = selectedVowel ?: "?", isSelected = selectedVowel != null)
                Text(
                    " = ",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AnimatedContent(
                    targetState = previewSyllable,
                    transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
                    label = "preview_syllable"
                ) { syllable ->
                    SyllableSlot(
                        label = syllable ?: "?",
                        isSelected = syllable != null,
                        isCorrect = if (syllable != null) isCorrect else null,
                        isLarge = true
                    )
                }
            }

            // Feedback
            AnimatedVisibility(visible = isCorrect != null) {
                Text(
                    text = if (isCorrect == true) stringResource(R.string.syllable_builder_correct)
                           else stringResource(R.string.syllable_builder_wrong),
                    color = if (isCorrect == true) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Consonant options
            Text(
                stringResource(R.string.syllable_builder_pick_consonant),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.xs)
            ) {
                consonantOptions.forEach { consonant ->
                    val isSelected = selectedConsonant == consonant
                    OutlinedButton(
                        onClick = {
                            if (!isLocked) {
                                selectedConsonant = consonant
                            }
                        },
                        enabled = !isLocked,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                                            else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            consonant,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            // Vowel options
            Text(
                stringResource(R.string.syllable_builder_pick_vowel),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.xs)
            ) {
                vowelOptions.forEach { vowel ->
                    val isSelected = selectedVowel == vowel
                    OutlinedButton(
                        onClick = {
                            if (!isLocked) {
                                selectedVowel = vowel
                            }
                        },
                        enabled = !isLocked,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.tertiaryContainer
                                            else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            vowel,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            // Try again button when wrong
            AnimatedVisibility(visible = isCorrect == false) {
                TextButton(
                    onClick = {
                        selectedConsonant = null
                        selectedVowel = null
                    }
                ) {
                    Text(stringResource(R.string.syllable_builder_try_again))
                }
            }
        }

        // Confetti overlay on correct answer
        ConfettiOverlay(
            active = showConfetti,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
private fun SyllableSlot(
    label: String,
    isSelected: Boolean,
    isCorrect: Boolean? = null,
    isLarge: Boolean = false,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        isCorrect == true -> MaterialTheme.colorScheme.primary
        isCorrect == false -> MaterialTheme.colorScheme.error
        isSelected -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.outline
    }
    val textColor = when {
        isCorrect == true -> MaterialTheme.colorScheme.primary
        isCorrect == false -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .size(if (isLarge) 56.dp else 44.dp)
            .border(
                width = if (isSelected || isCorrect != null) 2.dp else 1.dp,
                color = borderColor,
                shape = MaterialTheme.shapes.small
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = if (isLarge) MaterialTheme.typography.headlineMedium
                    else MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
