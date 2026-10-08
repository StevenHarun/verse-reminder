package com.versereminder.app

import com.versereminder.app.data.local.SeedReadingPlans
import com.versereminder.app.data.local.entity.ReadingPlanProgressEntity
import com.versereminder.app.domain.model.ReadingPlanProgress
import com.versereminder.app.domain.model.Verse
import com.versereminder.app.domain.model.VerseCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class Phase3SearchAndPlanTest {

    private val sampleVerses = listOf(
        Verse(
            id = 1,
            book = "Mazmur",
            chapter = 23,
            verse = 1,
            reference = "Mazmur 23:1",
            textId = "TUHAN adalah gembalaku, takkan kekurangan aku.",
            textEn = "The LORD is my shepherd; I shall not want.",
            category = VerseCategory.PEACE,
            themeTag = "Ketenangan"
        ),
        Verse(
            id = 2,
            book = "Filipi",
            chapter = 4,
            verse = 6,
            reference = "Filipi 4:6-7",
            textId = "Janganlah hendaknya kamu kuatir tentang apapun juga, tetapi nyatakanlah keinginanmu dalam doa.",
            textEn = "Do not be anxious about anything, but in every situation present your requests to God.",
            category = VerseCategory.PEACE,
            themeTag = "Damai"
        ),
        Verse(
            id = 3,
            book = "Yohanes",
            chapter = 3,
            verse = 16,
            reference = "Yohanes 3:16",
            textId = "Karena begitu besar kasih Allah akan dunia ini...",
            textEn = "For God so loved the world that he gave his one and only Son...",
            category = VerseCategory.LOVE,
            themeTag = "Kasih"
        ),
        Verse(
            id = 4,
            book = "Pribadi",
            chapter = 1,
            verse = 1,
            reference = "Amsal 4:23",
            textId = "Jagalah hatimu dengan segala kewaspadaan...",
            textEn = "Guard your heart with all diligence...",
            category = VerseCategory.WISDOM,
            themeTag = "Hikmat",
            isCustom = true
        )
    )

    @Test
    fun testSearchKeywordMatchingLogic() {
        fun search(query: String): List<Verse> {
            val q = query.trim()
            if (q.isBlank()) return sampleVerses
            return sampleVerses.filter { verse ->
                verse.reference.contains(q, ignoreCase = true) ||
                        verse.textId.contains(q, ignoreCase = true) ||
                        verse.textEn.contains(q, ignoreCase = true) ||
                        verse.category.name.contains(q, ignoreCase = true) ||
                        verse.themeTag.contains(q, ignoreCase = true)
            }
        }

        // Test reference search
        val psalmResults = search("Mazmur 23")
        assertEquals(1, psalmResults.size)
        assertEquals("Mazmur 23:1", psalmResults[0].reference)

        // Test Indonesian keyword search
        val peaceResults = search("kuatir")
        assertEquals(1, peaceResults.size)
        assertEquals("Filipi 4:6-7", peaceResults[0].reference)

        // Test English keyword search
        val loveEnglishResults = search("loved")
        assertEquals(1, loveEnglishResults.size)
        assertEquals("Yohanes 3:16", loveEnglishResults[0].reference)

        // Test Custom verse search
        val customResults = search("Amsal")
        assertEquals(1, customResults.size)
        assertTrue(customResults[0].isCustom)

        // Test blank returns all
        val allResults = search("   ")
        assertEquals(sampleVerses.size, allResults.size)
    }

    @Test
    fun testCuratedReadingPlansIntegrity() {
        val plans = SeedReadingPlans.plans
        assertEquals(4, plans.size)

        val planIds = plans.map { it.id }.toSet()
        assertTrue(planIds.contains("peace_7d"))
        assertTrue(planIds.contains("strength_7d"))
        assertTrue(planIds.contains("love_14d"))
        assertTrue(planIds.contains("wisdom_7d"))

        plans.forEach { plan ->
            assertTrue(plan.title.isNotBlank())
            assertTrue(plan.subtitle.isNotBlank())
            assertTrue(plan.description.isNotBlank())
            assertTrue(plan.iconEmoji.isNotBlank())
            assertEquals(plan.totalDays, plan.days.size)

            plan.days.forEachIndexed { index, day ->
                assertEquals(index + 1, day.dayNumber)
                assertTrue(day.title.isNotBlank())
                assertTrue(day.verseReference.isNotBlank())
                assertTrue(day.verseTextId.isNotBlank())
                assertTrue(day.verseTextEn.isNotBlank())
                assertTrue(day.devotionalText.isNotBlank())
                assertTrue(day.prayerText.isNotBlank())
            }
        }
    }

    @Test
    fun testReadingPlanProgressCalculation() {
        val peacePlan = SeedReadingPlans.getPlanById("peace_7d")
        assertNotNull(peacePlan)

        // Initial progress: 0%
        val initialProgress = ReadingPlanProgress(planId = "peace_7d")
        assertEquals(0f, initialProgress.getCompletionPercentage(peacePlan!!.totalDays), 0.001f)
        assertFalse(initialProgress.isDayCompleted(1))
        assertFalse(initialProgress.isCompleted)

        // Complete 2 days: 2/7 = ~0.2857f
        val partialProgress = initialProgress.copy(completedDays = setOf(1, 2))
        assertEquals(2f / 7f, partialProgress.getCompletionPercentage(peacePlan.totalDays), 0.001f)
        assertTrue(partialProgress.isDayCompleted(1))
        assertTrue(partialProgress.isDayCompleted(2))
        assertFalse(partialProgress.isDayCompleted(3))

        // Complete all 7 days
        val fullProgress = initialProgress.copy(
            completedDays = (1..7).toSet(),
            isCompleted = true
        )
        assertEquals(1.0f, fullProgress.getCompletionPercentage(peacePlan.totalDays), 0.001f)
        assertTrue(fullProgress.isCompleted)
    }

    @Test
    fun testReadingPlanProgressEntitySerialization() {
        val progress = ReadingPlanProgress(
            planId = "peace_7d",
            completedDays = setOf(1, 3, 5),
            currentDay = 6,
            lastReadTimestamp = 123456789L,
            isCompleted = false
        )

        val entity = ReadingPlanProgressEntity.fromDomain(progress)
        assertEquals("peace_7d", entity.planId)
        assertEquals("1,3,5", entity.completedDays)
        assertEquals(6, entity.currentDay)
        assertEquals(123456789L, entity.lastReadTimestamp)
        assertFalse(entity.isCompleted)

        val restoredDomain = entity.toDomain()
        assertEquals(progress.planId, restoredDomain.planId)
        assertEquals(progress.completedDays, restoredDomain.completedDays)
        assertEquals(progress.currentDay, restoredDomain.currentDay)
        assertEquals(progress.lastReadTimestamp, restoredDomain.lastReadTimestamp)
        assertEquals(progress.isCompleted, restoredDomain.isCompleted)

        // Test empty completed days string
        val emptyEntity = ReadingPlanProgressEntity(
            planId = "test",
            completedDays = "",
            currentDay = 1,
            lastReadTimestamp = 0L,
            isCompleted = false
        )
        assertTrue(emptyEntity.toDomain().completedDays.isEmpty())
    }
}
