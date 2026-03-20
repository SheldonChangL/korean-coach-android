package com.koreancoach.app

import com.koreancoach.app.data.curriculum.HangulCharacterData
import com.koreancoach.app.domain.model.HangulCharacterType
import org.junit.Assert.*
import org.junit.Test

class HangulCharacterDataTest {

    @Test
    fun `has exactly 19 consonants`() {
        assertEquals(19, HangulCharacterData.consonants.size)
    }

    @Test
    fun `has exactly 21 vowels`() {
        assertEquals(21, HangulCharacterData.vowels.size)
    }

    @Test
    fun `has exactly 40 hangul characters for sprint`() {
        assertEquals(40, HangulCharacterData.allCharacters.size)
    }

    @Test
    fun `keeps 5 example syllables`() {
        assertEquals(5, HangulCharacterData.syllables.size)
    }

    @Test
    fun `all characters has correctly typed`() {
        HangulCharacterData.consonants.forEach {
            assertEquals("${it.id} should be CONSONANT", HangulCharacterType.CONSONANT, it.type)
        }
        HangulCharacterData.syllables.forEach {
            assertEquals("${it.id} should be SYLLABLE", HangulCharacterType.SYLLABLE, it.type)
        }
    }

    @Test
    fun `consonants include the beginner core set`() {
        val chars = HangulCharacterData.consonants.map { it.character }
        assertTrue("Should contain ㄱ", "ㄱ" in chars)
        assertTrue("Should contain ㄴ", "ㄴ" in chars)
        assertTrue("Should contain ㄷ", "ㄷ" in chars)
        assertTrue("Should contain ㅁ", "ㅁ" in chars)
        assertTrue("Should contain ㅅ", "ㅅ" in chars)
        assertTrue("Should contain ㄲ", "ㄲ" in chars)
        assertTrue("Should contain ㅎ", "ㅎ" in chars)
    }

    @Test
    fun `vowels include core and compound examples`() {
        val chars = HangulCharacterData.vowels.map { it.character }
        assertTrue("Should contain ㅏ", "ㅏ" in chars)
        assertTrue("Should contain ㅓ", "ㅓ" in chars)
        assertTrue("Should contain ㅗ", "ㅗ" in chars)
        assertTrue("Should contain ㅘ", "ㅘ" in chars)
        assertTrue("Should contain ㅢ", "ㅢ" in chars)
    }

    @Test
    fun `all characters have at least one stroke`() {
        HangulCharacterData.allCharacters.forEach { char ->
            assertTrue("${char.character} must have at least 1 stroke", char.strokes.isNotEmpty())
            assertTrue("${char.character} strokeCount matches strokes list", char.strokeCount == char.strokes.size)
        }
    }

    @Test
    fun `all strokes have at least two points`() {
        HangulCharacterData.allCharacters.forEach { char ->
            char.strokes.forEach { stroke ->
                assertTrue("Each stroke must have ≥2 points", stroke.points.size >= 2)
            }
        }
    }

    @Test
    fun `all stroke points are normalised 0-1`() {
        HangulCharacterData.allCharacters.forEach { char ->
            char.strokes.forEach { stroke ->
                stroke.points.forEach { pt ->
                    assertTrue("${char.character} stroke x=${pt.x} must be in 0..1", pt.x in 0f..1f)
                    assertTrue("${char.character} stroke y=${pt.y} must be in 0..1", pt.y in 0f..1f)
                }
            }
        }
    }

    @Test
    fun `all characters have non-blank memory hooks`() {
        HangulCharacterData.allCharacters.forEach { char ->
            assertTrue("${char.character} must have a memory hook", char.memoryHook.isNotBlank())
        }
    }

    @Test
    fun `all IDs are unique`() {
        val ids = HangulCharacterData.allCharacters.map { it.id }
        assertEquals("All character IDs must be unique", ids.size, ids.toSet().size)
    }

    @Test
    fun `findById returns correct character`() {
        val giyeok = HangulCharacterData.findById("consonant_giyeok")
        assertNotNull(giyeok)
        assertEquals("ㄱ", giyeok!!.character)
    }

    @Test
    fun `findById returns null for unknown id`() {
        assertNull(HangulCharacterData.findById("nonexistent_id"))
    }
}
