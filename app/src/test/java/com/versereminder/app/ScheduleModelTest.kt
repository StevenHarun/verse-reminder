package com.versereminder.app

import com.versereminder.app.domain.model.Schedule
import com.versereminder.app.domain.model.VerseCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ScheduleModelTest {

    @Test
    fun testFormattedTimePadding() {
        val scheduleMorning = Schedule(
            id = 1,
            title = "Ayat Pagi",
            hour = 6,
            minute = 5,
            category = VerseCategory.PEACE,
            isEnabled = true
        )
        assertEquals("06:05", scheduleMorning.formattedTime)

        val scheduleEvening = Schedule(
            id = 2,
            title = "Renungan Malam",
            hour = 19,
            minute = 30,
            category = VerseCategory.WISDOM,
            isEnabled = true
        )
        assertEquals("19:30", scheduleEvening.formattedTime)
    }

    @Test
    fun testToggleScheduleEnabled() {
        val schedule = Schedule(
            id = 1,
            title = "Pengingat",
            hour = 12,
            minute = 0,
            category = VerseCategory.STRENGTH,
            isEnabled = true
        )

        val disabledSchedule = schedule.copy(isEnabled = false)
        assertFalse(disabledSchedule.isEnabled)
        assertTrue(schedule.isEnabled)
    }
}
