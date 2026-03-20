package com.koreancoach.app.data.curriculum

import com.koreancoach.app.domain.model.*

/**
 * Stroke-order data for the beginner Hangul progression:
 *   Consonants: ㄱ ㄴ ㄷ ㅁ ㅅ
 *   Syllables:  가 나 다 마 사
 *
 * Points are normalised to a 0–1 square canvas. The rendering composable
 * scales them to whatever dp size is used.
 */
object HangulCharacterData {

    // ─── Consonants ───────────────────────────────────────────────────────────

    private val giyeok = HangulCharacter(
        id = "consonant_giyeok",
        character = "ㄱ",
        romanization = "g / k",
        name = "기역 (giyeok)",
        description = "Sounds like 'g' at the start of a syllable, 'k' at the end.",
        strokeCount = 2,
        strokes = listOf(
            Stroke(
                points = listOf(
                    StrokePoint(0.20f, 0.30f),
                    StrokePoint(0.80f, 0.30f)
                ),
                hint = "Draw a horizontal line from left to right"
            ),
            Stroke(
                points = listOf(
                    StrokePoint(0.80f, 0.30f),
                    StrokePoint(0.80f, 0.75f)
                ),
                hint = "Drop straight down from the right end"
            )
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "ㄱ looks like the number '7' — 'g' for 'go seven'!"
    )

    private val nieun = HangulCharacter(
        id = "consonant_nieun",
        character = "ㄴ",
        romanization = "n",
        name = "니은 (nieun)",
        description = "Sounds like 'n' as in 'no'.",
        strokeCount = 2,
        strokes = listOf(
            Stroke(
                points = listOf(
                    StrokePoint(0.20f, 0.30f),
                    StrokePoint(0.20f, 0.75f)
                ),
                hint = "Draw a vertical line downward"
            ),
            Stroke(
                points = listOf(
                    StrokePoint(0.20f, 0.75f),
                    StrokePoint(0.80f, 0.75f)
                ),
                hint = "Sweep right along the bottom"
            )
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "ㄴ is an 'L' shape — 'N' stands for kNee-down, like an 'L'."
    )

    private val digeut = HangulCharacter(
        id = "consonant_digeut",
        character = "ㄷ",
        romanization = "d / t",
        name = "디귿 (digeut)",
        description = "Sounds like 'd' at the start, 't' at the end of a syllable.",
        strokeCount = 3,
        strokes = listOf(
            Stroke(
                points = listOf(
                    StrokePoint(0.20f, 0.30f),
                    StrokePoint(0.80f, 0.30f)
                ),
                hint = "Top horizontal line, left to right"
            ),
            Stroke(
                points = listOf(
                    StrokePoint(0.20f, 0.30f),
                    StrokePoint(0.20f, 0.75f)
                ),
                hint = "Left side, drop straight down"
            ),
            Stroke(
                points = listOf(
                    StrokePoint(0.20f, 0.75f),
                    StrokePoint(0.80f, 0.75f)
                ),
                hint = "Bottom horizontal line, left to right"
            )
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "ㄷ looks like a 'C' rotated — 'D' for Door frame open to the right."
    )

    private val mieum = HangulCharacter(
        id = "consonant_mieum",
        character = "ㅁ",
        romanization = "m",
        name = "미음 (mieum)",
        description = "Sounds like 'm' as in 'moon'.",
        strokeCount = 4,
        strokes = listOf(
            Stroke(
                points = listOf(
                    StrokePoint(0.20f, 0.30f),
                    StrokePoint(0.20f, 0.75f)
                ),
                hint = "Left side, top to bottom"
            ),
            Stroke(
                points = listOf(
                    StrokePoint(0.20f, 0.30f),
                    StrokePoint(0.80f, 0.30f)
                ),
                hint = "Top, left to right"
            ),
            Stroke(
                points = listOf(
                    StrokePoint(0.80f, 0.30f),
                    StrokePoint(0.80f, 0.75f)
                ),
                hint = "Right side, top to bottom"
            ),
            Stroke(
                points = listOf(
                    StrokePoint(0.20f, 0.75f),
                    StrokePoint(0.80f, 0.75f)
                ),
                hint = "Bottom, left to right"
            )
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "ㅁ is a perfect square — 'M' for Mouth, which is a box shape!"
    )

    private val siot = HangulCharacter(
        id = "consonant_siot",
        character = "ㅅ",
        romanization = "s / sh",
        name = "시옷 (siot)",
        description = "Sounds like 's' or 'sh' depending on the following vowel.",
        strokeCount = 2,
        strokes = listOf(
            Stroke(
                points = listOf(
                    StrokePoint(0.50f, 0.25f),
                    StrokePoint(0.20f, 0.75f)
                ),
                hint = "Left diagonal stroke, from centre-top down-left"
            ),
            Stroke(
                points = listOf(
                    StrokePoint(0.50f, 0.25f),
                    StrokePoint(0.80f, 0.75f)
                ),
                hint = "Right diagonal stroke, from centre-top down-right"
            )
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "ㅅ looks like a bird's wings or a 'V' — 'S' for Swooping bird!"
    )

    // ─── Syllables ────────────────────────────────────────────────────────────

    private val ga = HangulCharacter(
        id = "syllable_ga",
        character = "가",
        romanization = "ga",
        name = "가 (ga)",
        description = "ㄱ + ㅏ — the first syllable learners encounter.",
        strokeCount = 4,
        strokes = listOf(
            Stroke(
                points = listOf(StrokePoint(0.20f, 0.25f), StrokePoint(0.45f, 0.25f)),
                hint = "ㄱ top stroke — short horizontal"
            ),
            Stroke(
                points = listOf(StrokePoint(0.45f, 0.25f), StrokePoint(0.45f, 0.52f)),
                hint = "ㄱ drop stroke — short vertical"
            ),
            Stroke(
                points = listOf(StrokePoint(0.65f, 0.20f), StrokePoint(0.65f, 0.80f)),
                hint = "ㅏ vertical bar — tall, right side"
            ),
            Stroke(
                points = listOf(StrokePoint(0.65f, 0.45f), StrokePoint(0.85f, 0.45f)),
                hint = "ㅏ small horizontal tick — points right"
            )
        ),
        type = HangulCharacterType.SYLLABLE,
        memoryHook = "가 = ㄱ (7-shape) + ㅏ (pole with flag) → 'ga' like 'garage'."
    )

    private val na = HangulCharacter(
        id = "syllable_na",
        character = "나",
        romanization = "na",
        name = "나 (na)",
        description = "ㄴ + ㅏ — means 'I / me' in Korean!",
        strokeCount = 4,
        strokes = listOf(
            Stroke(
                points = listOf(StrokePoint(0.20f, 0.25f), StrokePoint(0.20f, 0.52f)),
                hint = "ㄴ down stroke"
            ),
            Stroke(
                points = listOf(StrokePoint(0.20f, 0.52f), StrokePoint(0.48f, 0.52f)),
                hint = "ㄴ right sweep"
            ),
            Stroke(
                points = listOf(StrokePoint(0.65f, 0.20f), StrokePoint(0.65f, 0.80f)),
                hint = "ㅏ vertical bar"
            ),
            Stroke(
                points = listOf(StrokePoint(0.65f, 0.45f), StrokePoint(0.85f, 0.45f)),
                hint = "ㅏ tick pointing right"
            )
        ),
        type = HangulCharacterType.SYLLABLE,
        memoryHook = "나 means 'I/me' — the L-shape is you standing up saying 'Na, it's me!'"
    )

    private val da = HangulCharacter(
        id = "syllable_da",
        character = "다",
        romanization = "da",
        name = "다 (da)",
        description = "ㄷ + ㅏ — means 'all / everything' in Korean.",
        strokeCount = 5,
        strokes = listOf(
            Stroke(
                points = listOf(StrokePoint(0.15f, 0.22f), StrokePoint(0.50f, 0.22f)),
                hint = "ㄷ top bar"
            ),
            Stroke(
                points = listOf(StrokePoint(0.15f, 0.22f), StrokePoint(0.15f, 0.55f)),
                hint = "ㄷ left side down"
            ),
            Stroke(
                points = listOf(StrokePoint(0.15f, 0.55f), StrokePoint(0.50f, 0.55f)),
                hint = "ㄷ bottom bar"
            ),
            Stroke(
                points = listOf(StrokePoint(0.68f, 0.18f), StrokePoint(0.68f, 0.82f)),
                hint = "ㅏ vertical bar"
            ),
            Stroke(
                points = listOf(StrokePoint(0.68f, 0.45f), StrokePoint(0.88f, 0.45f)),
                hint = "ㅏ tick right"
            )
        ),
        type = HangulCharacterType.SYLLABLE,
        memoryHook = "다 — ㄷ is a door-frame, ㅏ is the welcome post. Enter 'da' house!"
    )

    private val ma = HangulCharacter(
        id = "syllable_ma",
        character = "마",
        romanization = "ma",
        name = "마 (ma)",
        description = "ㅁ + ㅏ — sounds like 'ma' as in mother.",
        strokeCount = 6,
        strokes = listOf(
            Stroke(
                points = listOf(StrokePoint(0.15f, 0.22f), StrokePoint(0.15f, 0.58f)),
                hint = "ㅁ left side"
            ),
            Stroke(
                points = listOf(StrokePoint(0.15f, 0.22f), StrokePoint(0.50f, 0.22f)),
                hint = "ㅁ top"
            ),
            Stroke(
                points = listOf(StrokePoint(0.50f, 0.22f), StrokePoint(0.50f, 0.58f)),
                hint = "ㅁ right side"
            ),
            Stroke(
                points = listOf(StrokePoint(0.15f, 0.58f), StrokePoint(0.50f, 0.58f)),
                hint = "ㅁ bottom"
            ),
            Stroke(
                points = listOf(StrokePoint(0.68f, 0.18f), StrokePoint(0.68f, 0.82f)),
                hint = "ㅏ vertical bar"
            ),
            Stroke(
                points = listOf(StrokePoint(0.68f, 0.45f), StrokePoint(0.88f, 0.45f)),
                hint = "ㅏ tick right"
            )
        ),
        type = HangulCharacterType.SYLLABLE,
        memoryHook = "마 — ㅁ is the 'ma' (mom) box-mouth, ㅏ is her walking stick!"
    )

    private val sa = HangulCharacter(
        id = "syllable_sa",
        character = "사",
        romanization = "sa",
        name = "사 (sa)",
        description = "ㅅ + ㅏ — means 'four' or 'buy' in Korean.",
        strokeCount = 4,
        strokes = listOf(
            Stroke(
                points = listOf(StrokePoint(0.35f, 0.22f), StrokePoint(0.15f, 0.55f)),
                hint = "ㅅ left wing — diagonal down-left"
            ),
            Stroke(
                points = listOf(StrokePoint(0.35f, 0.22f), StrokePoint(0.55f, 0.55f)),
                hint = "ㅅ right wing — diagonal down-right"
            ),
            Stroke(
                points = listOf(StrokePoint(0.72f, 0.18f), StrokePoint(0.72f, 0.82f)),
                hint = "ㅏ vertical bar"
            ),
            Stroke(
                points = listOf(StrokePoint(0.72f, 0.45f), StrokePoint(0.90f, 0.45f)),
                hint = "ㅏ tick right"
            )
        ),
        type = HangulCharacterType.SYLLABLE,
        memoryHook = "사 — the ㅅ bird lands on the ㅏ perch. 'Sa' like 'safari'!"
    )

    // ─── Public API ───────────────────────────────────────────────────────────

    val consonants: List<HangulCharacter> = listOf(giyeok, nieun, digeut, mieum, siot)

    val syllables: List<HangulCharacter> = listOf(ga, na, da, ma, sa)

    val allCharacters: List<HangulCharacter> = consonants + syllables

    fun findById(id: String): HangulCharacter? = allCharacters.find { it.id == id }
}
