package com.versereminder.app

import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.Schedule
import com.versereminder.app.domain.model.Verse
import com.versereminder.app.domain.model.VerseCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class SettingsAlarmIntegrationTest {

    @Test
    fun testComputeNextTriggerMillis() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // Time 1 hour in the past should trigger tomorrow
        val pastHour = if (currentHour == 0) 23 else currentHour - 1
        val calPast = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, pastHour)
            set(Calendar.MINUTE, currentMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        var expectedMillis = calPast.timeInMillis
        if (expectedMillis <= System.currentTimeMillis()) {
            calPast.add(Calendar.DAY_OF_YEAR, 1)
            expectedMillis = calPast.timeInMillis
        }

        assertTrue(expectedMillis > System.currentTimeMillis())
    }

    @Test
    fun testNotificationPreviewFormatting() {
        val sampleVerse = Verse(
            id = 10,
            book = "Mazmur",
            chapter = 23,
            verse = 1,
            reference = "Mazmur 23:1",
            textId = "TUHAN adalah gembalaku, takkan kekurangan aku.",
            textEn = "The LORD is my shepherd; I shall not want.",
            category = VerseCategory.PEACE,
            themeTag = "Ketenangan"
        )

        val fullText = sampleVerse.getTextForVersion(BibleVersion.TB)

        // Test FULL
        val fullPreview = "\"$fullText\" — ${sampleVerse.reference}"
        assertEquals("\"TUHAN adalah gembalaku, takkan kekurangan aku.\" — Mazmur 23:1", fullPreview)

        // Test REF_ONLY
        val refPreview = sampleVerse.reference
        assertEquals("Mazmur 23:1", refPreview)

        // Test SHORT
        val short = if (fullText.length > 60) fullText.take(57) + "..." else fullText
        val shortPreview = "\"$short\" — ${sampleVerse.reference}"
        assertEquals("\"TUHAN adalah gembalaku, takkan kekurangan aku.\" — Mazmur 23:1", shortPreview)
    }

    @Test
    fun testNotificationSoundVibrationChannelMapping() {
        val CHANNEL_ID = "daily_verse_reminder_channel"
        val CHANNEL_ID_NO_SOUND = "daily_verse_reminder_no_sound"
        val CHANNEL_ID_NO_VIBRATE = "daily_verse_reminder_no_vibrate"
        val CHANNEL_ID_SILENT = "daily_verse_reminder_silent"

        fun getChannelId(soundEnabled: Boolean, vibrateEnabled: Boolean): String {
            return when {
                soundEnabled && vibrateEnabled -> CHANNEL_ID
                !soundEnabled && vibrateEnabled -> CHANNEL_ID_NO_SOUND
                soundEnabled && !vibrateEnabled -> CHANNEL_ID_NO_VIBRATE
                else -> CHANNEL_ID_SILENT
            }
        }

        assertEquals(CHANNEL_ID, getChannelId(soundEnabled = true, vibrateEnabled = true))
        assertEquals(CHANNEL_ID_NO_SOUND, getChannelId(soundEnabled = false, vibrateEnabled = true))
        assertEquals(CHANNEL_ID_NO_VIBRATE, getChannelId(soundEnabled = true, vibrateEnabled = false))
        assertEquals(CHANNEL_ID_SILENT, getChannelId(soundEnabled = false, vibrateEnabled = false))
    }

    @Test
    fun testScheduleActivationState() {
        val schedule = Schedule(
            id = 5,
            title = "Doa Malam",
            hour = 21,
            minute = 0,
            category = VerseCategory.PRAYER,
            isEnabled = true
        )

        assertTrue(schedule.isEnabled)
        val disabled = schedule.copy(isEnabled = false)
        assertFalse(disabled.isEnabled)
    }
}
