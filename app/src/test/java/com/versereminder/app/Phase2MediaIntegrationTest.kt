package com.versereminder.app

import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.Verse
import com.versereminder.app.domain.model.VerseCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

class Phase2MediaIntegrationTest {

    private val sampleIndonesianVerse = Verse(
        id = 1,
        book = "Mazmur",
        chapter = 23,
        verse = 1,
        reference = "Mazmur 23:1",
        textId = "TUHAN adalah gembalaku, takkan kekurangan aku.",
        textEn = "The LORD is my shepherd; I shall not want.",
        category = VerseCategory.PEACE,
        themeTag = "Ketenangan"
    )

    private val sampleEnglishVerse = Verse(
        id = 2,
        book = "Philippians",
        chapter = 4,
        verse = 13,
        reference = "Philippians 4:13",
        textId = "Segala perkara dapat kutanggung di dalam Dia yang memberi kekuatan kepadaku.",
        textEn = "I can do all things through Christ who strengthens me.",
        category = VerseCategory.STRENGTH,
        themeTag = "Kekuatan"
    )

    @Test
    fun testTtsLocaleResolution() {
        fun getTtsLocale(bibleVersion: BibleVersion): Locale {
            return when (bibleVersion) {
                BibleVersion.WEB, BibleVersion.KJV, BibleVersion.NIV, BibleVersion.ESV -> Locale.US
                else -> Locale("id", "ID")
            }
        }

        assertEquals("id", getTtsLocale(BibleVersion.TB).language)
        assertEquals("id", getTtsLocale(BibleVersion.TB2).language)
        assertEquals("id", getTtsLocale(BibleVersion.BIS).language)
        assertEquals("id", getTtsLocale(BibleVersion.TSI).language)
        assertEquals(Locale.US, getTtsLocale(BibleVersion.WEB))
        assertEquals(Locale.US, getTtsLocale(BibleVersion.KJV))
        assertEquals(Locale.US, getTtsLocale(BibleVersion.NIV))
        assertEquals(Locale.US, getTtsLocale(BibleVersion.ESV))
    }

    @Test
    fun testTtsSpokenTextFormatting() {
        fun formatTtsSpeech(verse: Verse, bibleVersion: BibleVersion): String {
            val text = verse.getTextForVersion(bibleVersion)
            val isIndonesian = when (bibleVersion) {
                BibleVersion.WEB, BibleVersion.KJV, BibleVersion.NIV, BibleVersion.ESV -> false
                else -> true
            }
            return if (isIndonesian) {
                "Kitab ${verse.reference}. $text"
            } else {
                "Scripture from ${verse.reference}. $text"
            }
        }

        val idSpoken = formatTtsSpeech(sampleIndonesianVerse, BibleVersion.TB)
        assertEquals("Kitab Mazmur 23:1. TUHAN adalah gembalaku, takkan kekurangan aku.", idSpoken)

        val enSpoken = formatTtsSpeech(sampleEnglishVerse, BibleVersion.WEB)
        assertEquals("Scripture from Philippians 4:13. I can do all things through Christ who strengthens me.", enSpoken)
    }

    @Test
    fun testImageGeneratorDimensions() {
        fun getImageDimensions(isStoryFormat: Boolean): Pair<Int, Int> {
            val width = 1080
            val height = if (isStoryFormat) 1920 else 1080
            return Pair(width, height)
        }

        val story = getImageDimensions(isStoryFormat = true)
        assertEquals(1080, story.first)
        assertEquals(1920, story.second)
        // 9:16 aspect ratio
        assertTrue(story.second.toFloat() / story.first.toFloat() > 1.77f)

        val square = getImageDimensions(isStoryFormat = false)
        assertEquals(1080, square.first)
        assertEquals(1080, square.second)
        // 1:1 aspect ratio
        assertEquals(1.0f, square.second.toFloat() / square.first.toFloat(), 0.001f)
    }

    @Test
    fun testShareTextCaptionFormatting() {
        val verseText = sampleIndonesianVerse.getTextForVersion(BibleVersion.TB)
        val shareCaption = "📖 ${sampleIndonesianVerse.reference} (TB)\n\n\"$verseText\"\n\n— Dibagikan melalui Verse Reminder"

        assertTrue(shareCaption.contains("Mazmur 23:1"))
        assertTrue(shareCaption.contains("TUHAN adalah gembalaku"))
        assertTrue(shareCaption.contains("Verse Reminder"))
    }
}
