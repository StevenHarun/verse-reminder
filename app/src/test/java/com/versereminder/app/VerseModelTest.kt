package com.versereminder.app

import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.Verse
import com.versereminder.app.domain.model.VerseCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VerseModelTest {

    @Test
    fun testVerseTranslationSelection() {
        val verse = Verse(
            id = 1,
            book = "Mazmur",
            chapter = 23,
            verse = 1,
            reference = "Mazmur 23:1",
            textId = "TUHAN adalah gembalaku, takkan kekurangan aku.",
            textEn = "The LORD is my shepherd; I shall not want.",
            category = VerseCategory.PEACE,
            themeTag = "Ketenangan",
            isBookmarked = false
        )

        assertEquals("TUHAN adalah gembalaku, takkan kekurangan aku.", verse.getTextForVersion(BibleVersion.TB))
        assertEquals("TUHAN adalah gembalaku, takkan kekurangan aku.", verse.getTextForVersion(BibleVersion.TB2))
        assertEquals("TUHAN adalah gembalaku, takkan kekurangan aku.", verse.getTextForVersion(BibleVersion.BIS))
        assertEquals("TUHAN adalah gembalaku, takkan kekurangan aku.", verse.getTextForVersion(BibleVersion.TSI))
        assertEquals("The LORD is my shepherd; I shall not want.", verse.getTextForVersion(BibleVersion.WEB))
        assertEquals("The LORD is my shepherd; I shall not want.", verse.getTextForVersion(BibleVersion.KJV))
        assertEquals("The LORD is my shepherd; I shall not want.", verse.getTextForVersion(BibleVersion.NIV))
        assertEquals("The LORD is my shepherd; I shall not want.", verse.getTextForVersion(BibleVersion.ESV))
    }

    @Test
    fun testVerseCategoryFromString() {
        assertEquals(VerseCategory.PEACE, VerseCategory.fromString("PEACE"))
        assertEquals(VerseCategory.PRAYER, VerseCategory.fromString("prayer"))
        assertEquals(VerseCategory.STRENGTH, VerseCategory.fromString("STRENGTH"))
        assertEquals(VerseCategory.ALL, VerseCategory.fromString("unknown_category"))
    }

    @Test
    fun testBookmarkStateToggle() {
        val verse = Verse(
            id = 1,
            book = "Filipi",
            chapter = 4,
            verse = 13,
            reference = "Filipi 4:13",
            textId = "Segala perkara dapat kutanggung di dalam Dia yang memberi kekuatan kepadaku.",
            textEn = "I can do all things through Christ who strengthens me.",
            category = VerseCategory.STRENGTH,
            themeTag = "Kekuatan",
            isBookmarked = false
        )

        val bookmarked = verse.copy(isBookmarked = true)
        assertTrue(bookmarked.isBookmarked)
        assertFalse(verse.isBookmarked)
    }

    @Test
    fun testCustomVerseProperties() {
        val customVerse = Verse(
            id = 100,
            book = "Yohanes",
            chapter = 14,
            verse = 6,
            reference = "Yohanes 14:6",
            textId = "Akulah jalan dan kebenaran dan hidup.",
            textEn = "I am the way and the truth and the life.",
            category = VerseCategory.FAITH,
            themeTag = "IMAN",
            isBookmarked = true,
            isCustom = true
        )

        assertTrue(customVerse.isCustom)
        assertTrue(customVerse.isBookmarked)
        assertEquals("IMAN", customVerse.themeTag)
        assertEquals("Akulah jalan dan kebenaran dan hidup.", customVerse.getTextForVersion(BibleVersion.TB))
        assertEquals("I am the way and the truth and the life.", customVerse.getTextForVersion(BibleVersion.WEB))
    }

    @Test
    fun testCustomVerseReferenceParsing() {
        val ref = "Mazmur 91:1-2"
        val book = ref.substringBefore(" ").ifBlank { "Pribadi" }
        val chapterVerse = ref.substringAfter(" ", "1:1")
        val chapter = chapterVerse.substringBefore(":").toIntOrNull() ?: 1
        val verseNum = chapterVerse.substringAfter(":").substringBefore("-").substringBefore(",").toIntOrNull() ?: 1

        assertEquals("Mazmur", book)
        assertEquals(91, chapter)
        assertEquals(1, verseNum)
    }
}
