package com.koreancoach.app.data.curriculum

import com.koreancoach.app.domain.model.HangulCharacter
import com.koreancoach.app.domain.model.HangulCharacterType
import com.koreancoach.app.domain.model.Stroke
import com.koreancoach.app.domain.model.StrokePoint

object HangulCharacterData {

    val basicVowels: List<HangulCharacter> = listOf(
        vowelA(),
        vowelYa(),
        vowelEo(),
        vowelYeo(),
        vowelO(),
        vowelYo(),
        vowelU(),
        vowelYu(),
        vowelEu(),
        vowelI()
    )

    val basicConsonants: List<HangulCharacter> = listOf(
        giyeok(),
        nieun(),
        digeut(),
        rieul(),
        mieum(),
        bieup(),
        siot(),
        ieung(),
        jieut(),
        chieut(),
        kieuk(),
        tieut(),
        pieup(),
        hieut()
    )

    val doubleConsonants: List<HangulCharacter> = listOf(
        ssangGiyeok(),
        ssangDigeut(),
        ssangBieup(),
        ssangSiot(),
        ssangJieut()
    )

    val compoundVowels: List<HangulCharacter> = listOf(
        vowelAe(),
        vowelYae(),
        vowelE(),
        vowelYe(),
        vowelWa(),
        vowelWae(),
        vowelOe(),
        vowelWeo(),
        vowelWe(),
        vowelWi(),
        vowelUi()
    )

    val consonants: List<HangulCharacter> = basicConsonants + doubleConsonants
    val vowels: List<HangulCharacter> = basicVowels + compoundVowels
    val syllables: List<HangulCharacter> = listOf(exampleGa(), exampleNa(), exampleDa(), exampleMa(), exampleSa())

    val allCharacters: List<HangulCharacter> = basicVowels + basicConsonants + doubleConsonants + compoundVowels

    fun findById(id: String): HangulCharacter? = (allCharacters + syllables).firstOrNull { it.id == id }

    private fun point(x: Float, y: Float) = StrokePoint(x, y)

    private fun stroke(hint: String, vararg pts: StrokePoint) = Stroke(
        points = pts.toList(),
        hint = hint
    )

    private fun vertical(x: Float, top: Float = 0.18f, bottom: Float = 0.82f, hint: String = "Draw top to bottom") =
        stroke(hint, point(x, top), point(x, bottom))

    private fun horizontal(left: Float, right: Float, y: Float, hint: String = "Draw left to right") =
        stroke(hint, point(left, y), point(right, y))

    private fun diagonal(x1: Float, y1: Float, x2: Float, y2: Float, hint: String) =
        stroke(hint, point(x1, y1), point(x2, y2))

    private fun circle(
        id: String,
        character: String,
        romanization: String,
        name: String,
        description: String,
        memoryHook: String
    ) = HangulCharacter(
        id = id,
        character = character,
        romanization = romanization,
        name = name,
        description = description,
        strokeCount = 1,
        strokes = listOf(
            stroke(
                "Draw one round loop",
                point(0.50f, 0.20f),
                point(0.72f, 0.32f),
                point(0.72f, 0.68f),
                point(0.50f, 0.80f),
                point(0.28f, 0.68f),
                point(0.28f, 0.32f),
                point(0.50f, 0.20f)
            )
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = memoryHook
    )

    private fun giyeok() = HangulCharacter(
        id = "consonant_giyeok",
        character = "ㄱ",
        romanization = "g / k",
        name = "기역",
        description = "Soft g at the start, k at the end.",
        strokeCount = 2,
        strokes = listOf(
            horizontal(0.22f, 0.78f, 0.28f, "Top bar"),
            vertical(0.78f, 0.28f, 0.76f, "Drop down on the right")
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "Looks like a corner turning right, like the sound beginning to move."
    )

    private fun nieun() = HangulCharacter(
        id = "consonant_nieun",
        character = "ㄴ",
        romanization = "n",
        name = "니은",
        description = "Like n in no.",
        strokeCount = 2,
        strokes = listOf(
            vertical(0.24f, 0.25f, 0.78f, "Left side down"),
            horizontal(0.24f, 0.78f, 0.78f, "Bottom sweep")
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "An L-shape you can lean on while saying nnn."
    )

    private fun digeut() = HangulCharacter(
        id = "consonant_digeut",
        character = "ㄷ",
        romanization = "d / t",
        name = "디귿",
        description = "Soft d at the start, t at the end.",
        strokeCount = 3,
        strokes = listOf(
            horizontal(0.20f, 0.80f, 0.24f, "Top line"),
            vertical(0.20f, 0.24f, 0.76f, "Left side"),
            horizontal(0.20f, 0.80f, 0.76f, "Bottom line")
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "A doorway shape for a d-sound opening up."
    )

    private fun rieul() = HangulCharacter(
        id = "consonant_rieul",
        character = "ㄹ",
        romanization = "r / l",
        name = "리을",
        description = "Flaps like r and can sound like l in the final position.",
        strokeCount = 5,
        strokes = listOf(
            horizontal(0.20f, 0.80f, 0.24f, "Top line"),
            vertical(0.20f, 0.24f, 0.46f, "Down on the left"),
            horizontal(0.20f, 0.70f, 0.46f, "Middle line"),
            vertical(0.70f, 0.46f, 0.76f, "Down on the right"),
            horizontal(0.20f, 0.70f, 0.76f, "Bottom line")
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "A zig-zag ribbon that rolls like a quick Korean r."
    )

    private fun mieum() = HangulCharacter(
        id = "consonant_mieum",
        character = "ㅁ",
        romanization = "m",
        name = "미음",
        description = "Like m in mom.",
        strokeCount = 4,
        strokes = listOf(
            vertical(0.22f, 0.24f, 0.78f, "Left side"),
            horizontal(0.22f, 0.78f, 0.24f, "Top line"),
            vertical(0.78f, 0.24f, 0.78f, "Right side"),
            horizontal(0.22f, 0.78f, 0.78f, "Bottom line")
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "A closed box like your lips closing for mmm."
    )

    private fun bieup() = HangulCharacter(
        id = "consonant_bieup",
        character = "ㅂ",
        romanization = "b / p",
        name = "비읍",
        description = "Soft b at the start, p at the end.",
        strokeCount = 4,
        strokes = listOf(
            vertical(0.22f, 0.22f, 0.78f, "Left side"),
            vertical(0.78f, 0.22f, 0.78f, "Right side"),
            horizontal(0.22f, 0.78f, 0.34f, "Upper middle"),
            horizontal(0.22f, 0.78f, 0.66f, "Lower middle")
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "Two lips facing each other for a b/p sound."
    )

    private fun siot() = HangulCharacter(
        id = "consonant_siot",
        character = "ㅅ",
        romanization = "s",
        name = "시옷",
        description = "Usually s, and closer to sh before i-like vowels.",
        strokeCount = 2,
        strokes = listOf(
            diagonal(0.48f, 0.22f, 0.24f, 0.78f, "Left falling stroke"),
            diagonal(0.48f, 0.22f, 0.72f, 0.78f, "Right falling stroke")
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "Looks like a pointed smile hissing softly."
    )

    private fun ieung() = circle(
        id = "consonant_ieung",
        character = "ㅇ",
        romanization = "silent / ng",
        name = "이응",
        description = "Silent at the start of a syllable, ng at the end.",
        memoryHook = "A round mouth shape that stays silent until the end."
    )

    private fun jieut() = HangulCharacter(
        id = "consonant_jieut",
        character = "ㅈ",
        romanization = "j",
        name = "지읒",
        description = "A softer j sound than English.",
        strokeCount = 3,
        strokes = listOf(
            horizontal(0.24f, 0.72f, 0.20f, "Top bar"),
            diagonal(0.48f, 0.34f, 0.24f, 0.78f, "Left falling stroke"),
            diagonal(0.48f, 0.34f, 0.72f, 0.78f, "Right falling stroke")
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "A siot with a hat, like a j wearing a cap."
    )

    private fun chieut() = HangulCharacter(
        id = "consonant_chieut",
        character = "ㅊ",
        romanization = "ch",
        name = "치읓",
        description = "An aspirated ch with a puff of air.",
        strokeCount = 4,
        strokes = listOf(
            horizontal(0.24f, 0.72f, 0.16f, "Top accent"),
            horizontal(0.24f, 0.72f, 0.28f, "Main top bar"),
            diagonal(0.48f, 0.40f, 0.24f, 0.78f, "Left falling stroke"),
            diagonal(0.48f, 0.40f, 0.72f, 0.78f, "Right falling stroke")
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "A jieut with extra air on top."
    )

    private fun kieuk() = HangulCharacter(
        id = "consonant_kieuk",
        character = "ㅋ",
        romanization = "k",
        name = "키읔",
        description = "A strong aspirated k.",
        strokeCount = 3,
        strokes = listOf(
            horizontal(0.22f, 0.78f, 0.24f, "Top bar"),
            horizontal(0.22f, 0.60f, 0.48f, "Middle arm"),
            vertical(0.78f, 0.24f, 0.78f, "Right side")
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "Like ㄱ plus an extra kick of air."
    )

    private fun tieut() = HangulCharacter(
        id = "consonant_tieut",
        character = "ㅌ",
        romanization = "t",
        name = "티읕",
        description = "A strong aspirated t.",
        strokeCount = 4,
        strokes = listOf(
            horizontal(0.20f, 0.80f, 0.20f, "Top bar"),
            horizontal(0.20f, 0.80f, 0.42f, "Middle bar"),
            vertical(0.20f, 0.20f, 0.78f, "Left side"),
            horizontal(0.20f, 0.80f, 0.78f, "Bottom bar")
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "A digeut with extra breath in the middle."
    )

    private fun pieup() = HangulCharacter(
        id = "consonant_pieup",
        character = "ㅍ",
        romanization = "p",
        name = "피읖",
        description = "A strong aspirated p.",
        strokeCount = 4,
        strokes = listOf(
            vertical(0.22f, 0.18f, 0.82f, "Left side"),
            vertical(0.78f, 0.18f, 0.82f, "Right side"),
            horizontal(0.22f, 0.78f, 0.28f, "Upper bar"),
            horizontal(0.22f, 0.78f, 0.58f, "Middle bar")
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "Like ㅂ with more puff and a taller frame."
    )

    private fun hieut() = HangulCharacter(
        id = "consonant_hieut",
        character = "ㅎ",
        romanization = "h",
        name = "히읗",
        description = "A breathy h sound.",
        strokeCount = 3,
        strokes = listOf(
            horizontal(0.28f, 0.72f, 0.28f, "Top bar"),
            stroke("Draw the round middle", point(0.50f, 0.42f), point(0.66f, 0.52f), point(0.50f, 0.70f), point(0.34f, 0.52f), point(0.50f, 0.42f)),
            horizontal(0.28f, 0.72f, 0.78f, "Bottom bar")
        ),
        type = HangulCharacterType.CONSONANT,
        memoryHook = "A circle breathing between two soft lines."
    )

    private fun ssangGiyeok() = duplicatedConsonant(
        id = "consonant_ssang_giyeok",
        character = "ㄲ",
        romanization = "kk",
        name = "쌍기역",
        description = "A tense kk sound.",
        baseFactory = ::giyeok,
        memoryHook = "Two ㄱ corners pressed tight together."
    )

    private fun ssangDigeut() = duplicatedConsonant(
        id = "consonant_ssang_digeut",
        character = "ㄸ",
        romanization = "tt",
        name = "쌍디귿",
        description = "A tense tt sound.",
        baseFactory = ::digeut,
        memoryHook = "A doubled doorway with extra tension."
    )

    private fun ssangBieup() = duplicatedConsonant(
        id = "consonant_ssang_bieup",
        character = "ㅃ",
        romanization = "pp",
        name = "쌍비읍",
        description = "A tense pp sound.",
        baseFactory = ::bieup,
        memoryHook = "Two lip boxes squeezed together."
    )

    private fun ssangSiot() = duplicatedConsonant(
        id = "consonant_ssang_siot",
        character = "ㅆ",
        romanization = "ss",
        name = "쌍시옷",
        description = "A tense ss sound.",
        baseFactory = ::siot,
        memoryHook = "Two sharp siot peaks for extra bite."
    )

    private fun ssangJieut() = duplicatedConsonant(
        id = "consonant_ssang_jieut",
        character = "ㅉ",
        romanization = "jj",
        name = "쌍지읒",
        description = "A tense jj sound.",
        baseFactory = ::jieut,
        memoryHook = "A doubled jieut with a tight release."
    )

    private fun duplicatedConsonant(
        id: String,
        character: String,
        romanization: String,
        name: String,
        description: String,
        baseFactory: () -> HangulCharacter,
        memoryHook: String
    ): HangulCharacter {
        val base = baseFactory()
        val left = offset(base.strokes, -0.12f)
        val right = offset(base.strokes, 0.12f)
        return HangulCharacter(
            id = id,
            character = character,
            romanization = romanization,
            name = name,
            description = description,
            strokeCount = left.size + right.size,
            strokes = left + right,
            type = HangulCharacterType.CONSONANT,
            memoryHook = memoryHook
        )
    }

    private fun offset(strokes: List<Stroke>, dx: Float): List<Stroke> =
        strokes.map { stroke ->
            Stroke(
                points = stroke.points.map { point ->
                    StrokePoint((point.x + dx).coerceIn(0f, 1f), point.y)
                },
                hint = stroke.hint
            )
        }

    private fun vowelA() = verticalVowel(
        id = "vowel_a",
        character = "ㅏ",
        romanization = "a",
        name = "아",
        description = "Open a sound, like a in father.",
        armDirection = ArmDirection.RIGHT,
        armCount = 1,
        memoryHook = "A standing line with one arm reaching right for ah."
    )

    private fun vowelYa() = verticalVowel(
        id = "vowel_ya",
        character = "ㅑ",
        romanization = "ya",
        name = "야",
        description = "ya sound.",
        armDirection = ArmDirection.RIGHT,
        armCount = 2,
        memoryHook = "Double right arms for a quick y-glide before ah."
    )

    private fun vowelEo() = verticalVowel(
        id = "vowel_eo",
        character = "ㅓ",
        romanization = "eo",
        name = "어",
        description = "eo, a core Korean vowel.",
        armDirection = ArmDirection.LEFT,
        armCount = 1,
        memoryHook = "The arm points left, reminding you this is not plain ah."
    )

    private fun vowelYeo() = verticalVowel(
        id = "vowel_yeo",
        character = "ㅕ",
        romanization = "yeo",
        name = "여",
        description = "yeo sound.",
        armDirection = ArmDirection.LEFT,
        armCount = 2,
        memoryHook = "Two left arms for the y-glide plus eo."
    )

    private fun vowelO() = horizontalVowel(
        id = "vowel_o",
        character = "ㅗ",
        romanization = "o",
        name = "오",
        description = "o sound.",
        stemPosition = StemPosition.TOP,
        stemCount = 1,
        memoryHook = "A pole growing up from the sky-line says oh!"
    )

    private fun vowelYo() = horizontalVowel(
        id = "vowel_yo",
        character = "ㅛ",
        romanization = "yo",
        name = "요",
        description = "yo sound.",
        stemPosition = StemPosition.TOP,
        stemCount = 2,
        memoryHook = "Two stems on top give you the extra y in yo."
    )

    private fun vowelU() = horizontalVowel(
        id = "vowel_u",
        character = "ㅜ",
        romanization = "u",
        name = "우",
        description = "u sound.",
        stemPosition = StemPosition.BOTTOM,
        stemCount = 1,
        memoryHook = "The pole hangs below the line, like a bowl saying oo."
    )

    private fun vowelYu() = horizontalVowel(
        id = "vowel_yu",
        character = "ㅠ",
        romanization = "yu",
        name = "유",
        description = "yu sound.",
        stemPosition = StemPosition.BOTTOM,
        stemCount = 2,
        memoryHook = "Two hanging stems make the y-glide in yu."
    )

    private fun vowelEu() = HangulCharacter(
        id = "vowel_eu",
        character = "ㅡ",
        romanization = "eu",
        name = "으",
        description = "Flat-lipped eu sound.",
        strokeCount = 1,
        strokes = listOf(horizontal(0.20f, 0.80f, 0.50f, "Draw the flat line")),
        type = HangulCharacterType.VOWEL,
        memoryHook = "A flat horizontal mouth for the flat-lipped eu sound."
    )

    private fun vowelI() = HangulCharacter(
        id = "vowel_i",
        character = "ㅣ",
        romanization = "i",
        name = "이",
        description = "ee sound.",
        strokeCount = 1,
        strokes = listOf(vertical(0.50f, 0.18f, 0.82f, "Draw the straight line")),
        type = HangulCharacterType.VOWEL,
        memoryHook = "A tall straight line like a stretched ee sound."
    )

    private fun vowelAe() = compoundVerticalVowel(
        id = "vowel_ae",
        character = "ㅐ",
        romanization = "ae",
        name = "애",
        description = "A blend close to eh/ae.",
        leftArms = 0,
        rightArms = 2,
        memoryHook = "ㅏ grows one more right arm to blend into ae."
    )

    private fun vowelYae() = compoundVerticalVowel(
        id = "vowel_yae",
        character = "ㅒ",
        romanization = "yae",
        name = "얘",
        description = "yae sound.",
        leftArms = 0,
        rightArms = 3,
        memoryHook = "The extra right strokes show a y + ae blend."
    )

    private fun vowelE() = compoundVerticalVowel(
        id = "vowel_e",
        character = "ㅔ",
        romanization = "e",
        name = "에",
        description = "A clean e sound.",
        leftArms = 2,
        rightArms = 0,
        memoryHook = "ㅓ grows one more left arm to become e."
    )

    private fun vowelYe() = compoundVerticalVowel(
        id = "vowel_ye",
        character = "ㅖ",
        romanization = "ye",
        name = "예",
        description = "ye sound.",
        leftArms = 3,
        rightArms = 0,
        memoryHook = "More left arms signal the y-glide in ye."
    )

    private fun vowelWa() = compositeVowel(
        id = "vowel_wa",
        character = "ㅘ",
        romanization = "wa",
        name = "와",
        description = "o + a = wa.",
        first = vowelO(),
        second = offset(vowelA().strokes, 0.16f),
        memoryHook = "Start at ㅗ and slide into ㅏ to say wa."
    )

    private fun vowelWae() = compositeVowel(
        id = "vowel_wae",
        character = "ㅙ",
        romanization = "wae",
        name = "왜",
        description = "o + ae = wae.",
        first = vowelO(),
        second = offset(vowelAe().strokes, 0.16f),
        memoryHook = "ㅗ meets ㅐ, making a wider wae sound."
    )

    private fun vowelOe() = compositeVowel(
        id = "vowel_oe",
        character = "ㅚ",
        romanization = "oe",
        name = "외",
        description = "o + i family sound.",
        first = vowelO(),
        second = offset(vowelI().strokes, 0.18f),
        memoryHook = "ㅗ pulls in ㅣ to glide toward oe."
    )

    private fun vowelWeo() = compositeVowel(
        id = "vowel_weo",
        character = "ㅝ",
        romanization = "wo",
        name = "워",
        description = "u + eo = wo.",
        first = vowelU(),
        second = offset(vowelEo().strokes, 0.16f),
        memoryHook = "ㅜ moves toward ㅓ to produce wo."
    )

    private fun vowelWe() = compositeVowel(
        id = "vowel_we",
        character = "ㅞ",
        romanization = "we",
        name = "웨",
        description = "u + e = we.",
        first = vowelU(),
        second = offset(vowelE().strokes, 0.16f),
        memoryHook = "ㅜ pairs with ㅔ for a clear we sound."
    )

    private fun vowelWi() = compositeVowel(
        id = "vowel_wi",
        character = "ㅟ",
        romanization = "wi",
        name = "위",
        description = "u + i = wi.",
        first = vowelU(),
        second = offset(vowelI().strokes, 0.18f),
        memoryHook = "ㅜ leans into ㅣ to make wi."
    )

    private fun vowelUi() = compositeVowel(
        id = "vowel_ui",
        character = "ㅢ",
        romanization = "ui",
        name = "의",
        description = "eu + i = ui.",
        first = vowelEu(),
        second = offset(vowelI().strokes, 0.20f),
        memoryHook = "A flat ㅡ with a trailing ㅣ becomes ui."
    )

    private fun verticalVowel(
        id: String,
        character: String,
        romanization: String,
        name: String,
        description: String,
        armDirection: ArmDirection,
        armCount: Int,
        memoryHook: String
    ): HangulCharacter {
        val armXs = when (armCount) {
            1 -> listOf(0.50f)
            2 -> listOf(0.40f, 0.60f)
            3 -> listOf(0.34f, 0.50f, 0.66f)
            else -> error("Unsupported arm count")
        }
        val centerX = if (armDirection == ArmDirection.RIGHT) 0.38f else 0.62f
        val armEndX = if (armDirection == ArmDirection.RIGHT) 0.80f else 0.20f
        val strokes = mutableListOf<Stroke>()
        strokes += vertical(centerX, 0.18f, 0.82f, "Main vertical line")
        armXs.forEach { ySelector ->
            val y = when (armCount) {
                1 -> 0.50f
                2 -> if (ySelector < 0.5f) 0.38f else 0.62f
                else -> when (ySelector) {
                    0.34f -> 0.30f
                    0.50f -> 0.50f
                    else -> 0.70f
                }
            }
            strokes += horizontal(centerX, armEndX, y, "Short vowel arm")
        }
        return HangulCharacter(
            id = id,
            character = character,
            romanization = romanization,
            name = name,
            description = description,
            strokeCount = strokes.size,
            strokes = strokes,
            type = HangulCharacterType.VOWEL,
            memoryHook = memoryHook
        )
    }

    private fun compoundVerticalVowel(
        id: String,
        character: String,
        romanization: String,
        name: String,
        description: String,
        leftArms: Int,
        rightArms: Int,
        memoryHook: String
    ): HangulCharacter {
        val strokes = mutableListOf<Stroke>()
        strokes += vertical(0.50f, 0.18f, 0.82f, "Main vertical line")
        repeat(leftArms) { index ->
            val y = 0.34f + index * 0.16f
            strokes += horizontal(0.50f, 0.22f, y, "Left compound arm")
        }
        repeat(rightArms) { index ->
            val y = 0.34f + index * 0.16f
            strokes += horizontal(0.50f, 0.80f, y, "Right compound arm")
        }
        return HangulCharacter(
            id = id,
            character = character,
            romanization = romanization,
            name = name,
            description = description,
            strokeCount = strokes.size,
            strokes = strokes,
            type = HangulCharacterType.VOWEL,
            memoryHook = memoryHook
        )
    }

    private fun horizontalVowel(
        id: String,
        character: String,
        romanization: String,
        name: String,
        description: String,
        stemPosition: StemPosition,
        stemCount: Int,
        memoryHook: String
    ): HangulCharacter {
        val stemXs = when (stemCount) {
            1 -> listOf(0.50f)
            2 -> listOf(0.40f, 0.60f)
            else -> error("Unsupported stem count")
        }
        val stemTop = if (stemPosition == StemPosition.TOP) 0.18f else 0.50f
        val stemBottom = if (stemPosition == StemPosition.TOP) 0.50f else 0.82f
        val lineY = if (stemPosition == StemPosition.TOP) 0.56f else 0.44f
        val strokes = mutableListOf<Stroke>()
        stemXs.forEach { x ->
            strokes += vertical(x, stemTop, stemBottom, "Short stem")
        }
        strokes += horizontal(0.20f, 0.80f, lineY, "Main horizontal line")
        return HangulCharacter(
            id = id,
            character = character,
            romanization = romanization,
            name = name,
            description = description,
            strokeCount = strokes.size,
            strokes = strokes,
            type = HangulCharacterType.VOWEL,
            memoryHook = memoryHook
        )
    }

    private fun compositeVowel(
        id: String,
        character: String,
        romanization: String,
        name: String,
        description: String,
        first: HangulCharacter,
        second: List<Stroke>,
        memoryHook: String
    ): HangulCharacter = HangulCharacter(
        id = id,
        character = character,
        romanization = romanization,
        name = name,
        description = description,
        strokeCount = first.strokes.size + second.size,
        strokes = first.strokes + second,
        type = HangulCharacterType.VOWEL,
        memoryHook = memoryHook
    )

    private fun exampleSyllable(
        id: String,
        character: String,
        romanization: String,
        name: String,
        description: String,
        strokes: List<Stroke>,
        memoryHook: String
    ) = HangulCharacter(
        id = id,
        character = character,
        romanization = romanization,
        name = name,
        description = description,
        strokeCount = strokes.size,
        strokes = strokes,
        type = HangulCharacterType.SYLLABLE,
        memoryHook = memoryHook
    )

    private fun exampleGa() = exampleSyllable(
        id = "syllable_ga",
        character = "가",
        romanization = "ga",
        name = "가",
        description = "ㄱ + ㅏ",
        strokes = giyeok().strokes + offset(vowelA().strokes, 0.08f),
        memoryHook = "The first classic block many learners read."
    )

    private fun exampleNa() = exampleSyllable(
        id = "syllable_na",
        character = "나",
        romanization = "na",
        name = "나",
        description = "ㄴ + ㅏ",
        strokes = nieun().strokes + offset(vowelA().strokes, 0.08f),
        memoryHook = "Useful because 나 also means me."
    )

    private fun exampleDa() = exampleSyllable(
        id = "syllable_da",
        character = "다",
        romanization = "da",
        name = "다",
        description = "ㄷ + ㅏ",
        strokes = digeut().strokes + offset(vowelA().strokes, 0.08f),
        memoryHook = "A basic block for many beginner verbs."
    )

    private fun exampleMa() = exampleSyllable(
        id = "syllable_ma",
        character = "마",
        romanization = "ma",
        name = "마",
        description = "ㅁ + ㅏ",
        strokes = mieum().strokes + offset(vowelA().strokes, 0.08f),
        memoryHook = "Feels like saying ma with a square mouth."
    )

    private fun exampleSa() = exampleSyllable(
        id = "syllable_sa",
        character = "사",
        romanization = "sa",
        name = "사",
        description = "ㅅ + ㅏ",
        strokes = siot().strokes + offset(vowelA().strokes, 0.08f),
        memoryHook = "One of the first blocks you can decode by sound."
    )

    private enum class ArmDirection { LEFT, RIGHT }
    private enum class StemPosition { TOP, BOTTOM }
}
