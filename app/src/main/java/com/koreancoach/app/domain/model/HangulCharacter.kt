package com.koreancoach.app.domain.model

/**
 * A Hangul character or syllable used in writing practice.
 *
 * @param id           Unique identifier, e.g. "consonant_giyeok"
 * @param character    The Korean character(s), e.g. "ㄱ" or "가"
 * @param romanization Romanized pronunciation, e.g. "g/k"
 * @param name         Korean name for the character, e.g. "기역"
 * @param description  Short description of how it's used / sounds
 * @param strokeCount  Total number of strokes
 * @param strokes      Ordered list of strokes (each stroke = list of control points)
 * @param type         CONSONANT, VOWEL, or SYLLABLE
 * @param memoryHook   Mnemonic to help remember the shape
 */
data class HangulCharacter(
    val id: String,
    val character: String,
    val romanization: String,
    val name: String,
    val description: String,
    val strokeCount: Int,
    val strokes: List<Stroke>,
    val type: HangulCharacterType,
    val memoryHook: String
)

enum class HangulCharacterType { CONSONANT, VOWEL, SYLLABLE }

/**
 * A single stroke defined by a sequence of normalised (0f–1f) path points.
 * The canvas scales these to whatever size it renders at.
 *
 * @param points  Sequence of (x, y) pairs defining the stroke path.
 * @param hint    Short instruction shown to the learner, e.g. "Draw left to right".
 */
data class Stroke(
    val points: List<StrokePoint>,
    val hint: String
)

/**
 * A normalised canvas point (both axes in 0f–1f range).
 */
data class StrokePoint(val x: Float, val y: Float)
