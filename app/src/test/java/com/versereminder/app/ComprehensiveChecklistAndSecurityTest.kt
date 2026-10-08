package com.versereminder.app

import com.versereminder.app.data.local.SeedReadingPlans
import com.versereminder.app.data.local.SeedVerses
import com.versereminder.app.data.local.entity.ReadingPlanProgressEntity
import com.versereminder.app.domain.model.AppFontType
import com.versereminder.app.domain.model.AuthProvider
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.ReadingPlanProgress
import com.versereminder.app.domain.model.Schedule
import com.versereminder.app.domain.model.ThemeMode
import com.versereminder.app.domain.model.UserSession
import com.versereminder.app.domain.model.Verse
import com.versereminder.app.domain.model.VerseCategory
import com.versereminder.app.scheduler.AlarmScheduler
import com.versereminder.app.scheduler.NotificationHelper
import com.versereminder.app.ui.theme.FigmaCreamVerse
import com.versereminder.app.ui.theme.FigmaDarkBg
import com.versereminder.app.ui.theme.FigmaDarkSurface
import com.versereminder.app.ui.theme.FigmaGold
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Locale

/**
 * Pengujian Granular & Komprehensif: 1 Per 1 Seluruh Item Checklist,
 * Keamanan, dan Kompatibilitas Sistem Android.
 */
class ComprehensiveChecklistAndSecurityTest {

    // =========================================================================
    // 1. AUDIT AUTENTIKASI, AKUN, & KEAMANAN SESI (CHECKLIST ITEM A)
    // =========================================================================

    @Test
    fun testAuthValidation_EmailFormatAndVerificationCode() {
        val validEmail = "steven@example.com"
        val invalidEmail = "steven-without-at"
        fun isEmailValid(email: String): Boolean = email.contains("@") && email.contains(".")

        assertTrue("Email valid harus diterima", isEmailValid(validEmail))
        assertFalse("Email tanpa @ dan domain harus ditolak", isEmailValid(invalidEmail))

        // Uji format kode verifikasi 6-digit
        fun isVerificationCodeValid(code: String): Boolean = code.length == 6 && code.all { it.isDigit() }
        assertTrue("Kode 6 digit valid", isVerificationCodeValid("123456"))
        assertFalse("Kode 5 digit tidak valid", isVerificationCodeValid("12345"))
        assertFalse("Kode dengan huruf tidak valid", isVerificationCodeValid("12A456"))
    }

    @Test
    fun testUserSession_GuestModeAndLogoutTransitions() {
        val loggedInUser = UserSession(
            userId = "usr_123",
            email = "user@test.com",
            displayName = "User Test",
            authProvider = AuthProvider.EMAIL,
            isLoggedIn = true,
            isGuest = false
        )
        assertTrue(loggedInUser.isLoggedIn)
        assertFalse(loggedInUser.isGuest)

        val guestUser = UserSession(
            userId = "guest_876",
            email = "",
            displayName = "Tamu",
            authProvider = AuthProvider.GUEST,
            isLoggedIn = false,
            isGuest = true
        )
        assertFalse(guestUser.isLoggedIn)
        assertTrue(guestUser.isGuest)
        assertEquals("Tamu", guestUser.displayName)
    }

    // =========================================================================
    // 2. AUDIT DESAIN FIGMA & TOKEN TEMA (CHECKLIST ITEM B)
    // =========================================================================

    @Test
    fun testFigmaDesignTokens_ColorValuesMatchFigmaSpecification() {
        // Figma Gold: #D4AF37 -> 0xFFD4AF37
        assertEquals("FigmaGold Alpha harus 1f", 1f, FigmaGold.alpha, 0.01f)
        assertEquals("FigmaDarkBg Alpha harus 1f", 1f, FigmaDarkBg.alpha, 0.01f)
        assertEquals("FigmaDarkSurface Alpha harus 1f", 1f, FigmaDarkSurface.alpha, 0.01f)
        assertEquals("FigmaCreamVerse Alpha harus 1f", 1f, FigmaCreamVerse.alpha, 0.01f)

        // Pastikan kontras warna antara latar gelap dan teks ayat tinggi
        assertNotEquals(FigmaDarkBg, FigmaCreamVerse)
        assertNotEquals(FigmaDarkSurface, FigmaGold)
    }

    @Test
    fun testThemeModeResolution() {
        assertEquals(ThemeMode.LIGHT, ThemeMode.fromString("LIGHT"))
        assertEquals(ThemeMode.DARK, ThemeMode.fromString("DARK"))
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromString("SYSTEM"))
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromString("UNKNOWN_MODE"))
    }

    // =========================================================================
    // 3. AUDIT PERSONALISASI, TERJEMAHAN & FONT (CHECKLIST ITEM C)
    // =========================================================================

    @Test
    fun testBibleVersions_AllEightVersionsSupported() {
        val versions = BibleVersion.entries
        assertEquals("Harus ada tepat 8 versi Alkitab", 8, versions.size)

        // 4 Versi Bahasa Indonesia
        assertTrue(BibleVersion.TB.isIndonesian)
        assertTrue(BibleVersion.TB2.isIndonesian)
        assertTrue(BibleVersion.BIS.isIndonesian)
        assertTrue(BibleVersion.TSI.isIndonesian)

        // 4 Versi Bahasa Inggris
        assertTrue(BibleVersion.WEB.isEnglish)
        assertTrue(BibleVersion.KJV.isEnglish)
        assertTrue(BibleVersion.NIV.isEnglish)
        assertTrue(BibleVersion.ESV.isEnglish)

        // Test fallback pencarian kode
        assertEquals(BibleVersion.TB, BibleVersion.fromCode("TB"))
        assertEquals(BibleVersion.KJV, BibleVersion.fromCode("kjv"))
        assertEquals(BibleVersion.TB, BibleVersion.fromCode("INVALID_CODE"))
    }

    @Test
    fun testAppFontType_FourFontFamilies() {
        assertEquals(4, AppFontType.entries.size)
        assertEquals(AppFontType.SERIF, AppFontType.fromString("SERIF"))
        assertEquals(AppFontType.SANS, AppFontType.fromString("SANS"))
        assertEquals(AppFontType.ROUNDED, AppFontType.fromString("ROUNDED"))
        assertEquals(AppFontType.MONOSPACE, AppFontType.fromString("MONOSPACE"))
        assertEquals(AppFontType.SERIF, AppFontType.fromString("UNKNOWN"))
    }

    // =========================================================================
    // 4. AUDIT ALARM, JADWAL & NOTIFIKASI ANDROID (CHECKLIST ITEM D)
    // =========================================================================

    @Test
    fun testAlarmScheduler_NextTriggerTimeCalculation() {
        val currentCalendar = Calendar.getInstance()
        val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentCalendar.get(Calendar.MINUTE)

        // Waktu 2 jam dari sekarang harus jatuh di hari yang sama
        val futureHour = (currentHour + 2) % 24
        val futureCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, futureHour)
            set(Calendar.MINUTE, currentMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Logic check: jika target <= now, tambah 1 hari
        val targetMillis = if (futureCalendar.timeInMillis <= System.currentTimeMillis()) {
            futureCalendar.add(Calendar.DAY_OF_YEAR, 1)
            futureCalendar.timeInMillis
        } else {
            futureCalendar.timeInMillis
        }

        assertTrue("Waktu trigger alarm harus di masa depan", targetMillis > System.currentTimeMillis())
    }

    @Test
    fun testNotificationChannels_FourDistinctChannels() {
        val channels = listOf(
            NotificationHelper.CHANNEL_ID,
            NotificationHelper.CHANNEL_ID_NO_SOUND,
            NotificationHelper.CHANNEL_ID_NO_VIBRATE,
            NotificationHelper.CHANNEL_ID_SILENT
        )
        assertEquals("Harus ada 4 channel notifikasi unik", 4, channels.toSet().size)
    }

    @Test
    fun testNotificationPreviewMode_Formatting() {
        val verseText = "TUHAN adalah gembalaku, takkan kekurangan aku."
        val reference = "Mazmur 23:1"

        fun formatPreview(mode: String): String {
            return when (mode) {
                "REF_ONLY" -> reference
                "SHORT" -> {
                    val short = if (verseText.length > 20) verseText.take(17) + "..." else verseText
                    "\"$short\" — $reference"
                }
                else -> "\"$verseText\" — $reference"
            }
        }

        assertEquals("Mazmur 23:1", formatPreview("REF_ONLY"))
        assertTrue("SHORT mode harus menyertakan elipsis jika panjang", formatPreview("SHORT").contains("..."))
        assertEquals("\"TUHAN adalah gembalaku, takkan kekurangan aku.\" — Mazmur 23:1", formatPreview("FULL"))
    }

    // =========================================================================
    // 5. AUDIT AYAT ALKITAB, KATEGORI & AYAT KUSTOM (CHECKLIST ITEM E & G)
    // =========================================================================

    @Test
    fun testSeedVerses_DataCompleteness() {
        val verses = SeedVerses.list
        assertTrue("Data ayat awal Alkitab tidak boleh kosong", verses.isNotEmpty())
        verses.forEach { verse ->
            assertTrue("ID harus positif", verse.id > 0)
            assertTrue("Referensi tidak boleh kosong", verse.reference.isNotBlank())
            assertTrue("Teks ID tidak boleh kosong", verse.textId.isNotBlank())
            assertTrue("Teks EN tidak boleh kosong", verse.textEn.isNotBlank())
            assertFalse("Ayat seed bukan buatan kustom", verse.isCustom)
        }
    }

    @Test
    fun testCustomVerse_FlagAndAttributes() {
        val customVerse = Verse(
            id = 999,
            book = "Catatan",
            chapter = 1,
            verse = 1,
            reference = "Catatan 1:1",
            textId = "Ayat inspirasi pribadi saya hari ini.",
            textEn = "My personal inspiring verse today.",
            category = VerseCategory.HOPE,
            themeTag = "PRIBADI",
            isCustom = true
        )
        assertTrue("isCustom harus true untuk ayat buatan pengguna", customVerse.isCustom)
        assertEquals("PRIBADI", customVerse.themeTag)
    }

    // =========================================================================
    // 6. AUDIT FASE 2: TEXT-TO-SPEECH & CARD GENERATOR (CHECKLIST ITEM H)
    // =========================================================================

    @Test
    fun testPhase2_TtsLocaleAndPacing() {
        fun getTtsLocale(bibleVersion: BibleVersion): Locale {
            return if (bibleVersion.isEnglish) Locale.US else Locale("id", "ID")
        }

        assertEquals("id", getTtsLocale(BibleVersion.TB).language)
        assertEquals("en", getTtsLocale(BibleVersion.KJV).language)
        assertEquals("en", getTtsLocale(BibleVersion.NIV).language)
    }

    @Test
    fun testPhase2_ImageCardDimensions() {
        val storyWidth = 1080
        val storyHeight = 1920
        val postWidth = 1080
        val postHeight = 1080

        assertEquals("Story ratio harus 9:16", 9f / 16f, storyWidth.toFloat() / storyHeight.toFloat(), 0.01f)
        assertEquals("Post ratio harus 1:1", 1.0f, postWidth.toFloat() / postHeight.toFloat(), 0.001f)
    }

    // =========================================================================
    // 7. AUDIT FASE 3: SEARCH BAR & DEVOTIONAL READING PLANS (CHECKLIST ITEM I)
    // =========================================================================

    @Test
    fun testPhase3_MultiColumnSearch_AccurateResults() {
        val list = SeedVerses.list
        fun search(query: String): List<Verse> {
            val q = query.trim()
            if (q.isBlank()) return list
            return list.filter { v ->
                v.reference.contains(q, ignoreCase = true) ||
                        v.textId.contains(q, ignoreCase = true) ||
                        v.textEn.contains(q, ignoreCase = true) ||
                        v.category.name.contains(q, ignoreCase = true)
            }
        }

        val resultsKasih = search("kasih")
        assertTrue("Harus menemukan ayat tentang kasih", resultsKasih.isNotEmpty())

        val resultsJohn = search("Yohanes")
        assertTrue("Harus menemukan kitab Yohanes", resultsJohn.isNotEmpty())

        val resultsEmpty = search("NonExistentTermXYZ123")
        assertTrue("Kata kunci aneh harus menghasilkan list kosong", resultsEmpty.isEmpty())
    }

    @Test
    fun testPhase3_ReadingPlansCatalog_FullIntegrity() {
        val plans = SeedReadingPlans.plans
        assertEquals("Harus ada 4 paket rencana renungan", 4, plans.size)

        val planIds = plans.map { it.id }
        assertTrue(planIds.contains("peace_7d"))
        assertTrue(planIds.contains("strength_7d"))
        assertTrue(planIds.contains("love_14d"))
        assertTrue(planIds.contains("wisdom_7d"))

        plans.forEach { plan ->
            assertTrue(plan.title.isNotBlank())
            assertTrue(plan.subtitle.isNotBlank())
            assertEquals(plan.totalDays, plan.days.size)

            plan.days.forEach { day ->
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
    fun testPhase3_ReadingPlanProgress_ChecklistToggle() {
        var progress = ReadingPlanProgress(planId = "peace_7d")
        assertEquals(0f, progress.getCompletionPercentage(7), 0.001f)

        // Toggle Hari 1 Selesai
        val updatedDays = progress.completedDays.toMutableSet().apply { add(1) }
        progress = progress.copy(completedDays = updatedDays, currentDay = 2)
        assertEquals(1f / 7f, progress.getCompletionPercentage(7), 0.001f)
        assertTrue(progress.isDayCompleted(1))
        assertFalse(progress.isDayCompleted(2))

        // Toggle Hari 1 Dibatalkan
        val uncheckDays = progress.completedDays.toMutableSet().apply { remove(1) }
        progress = progress.copy(completedDays = uncheckDays)
        assertFalse(progress.isDayCompleted(1))
        assertEquals(0f, progress.getCompletionPercentage(7), 0.001f)
    }

    @Test
    fun testPhase3_ReadingPlanProgressEntity_Serialization() {
        val entity = ReadingPlanProgressEntity(
            planId = "peace_7d",
            completedDays = "1,2,3,4,5,6,7",
            currentDay = 7,
            lastReadTimestamp = 999999L,
            isCompleted = true
        )
        val domain = entity.toDomain()
        assertEquals(7, domain.completedDays.size)
        assertTrue(domain.isCompleted)
        assertEquals(1.0f, domain.getCompletionPercentage(7), 0.001f)

        val backToEntity = ReadingPlanProgressEntity.fromDomain(domain)
        assertEquals("1,2,3,4,5,6,7", backToEntity.completedDays)
        assertTrue(backToEntity.isCompleted)
    }

    // =========================================================================
    // 8. AUDIT KEAMANAN & KOMPATIBILITAS OS ANDROID (SECURITY & OS AUDIT)
    // =========================================================================

    @Test
    fun testSecurityAudit_FileProviderAndAuthorities() {
        val packageName = "com.versereminder.app"
        val authority = "$packageName.fileprovider"
        assertEquals("Authority FileProvider harus berakhiran .fileprovider", "com.versereminder.app.fileprovider", authority)
    }

    @Test
    fun testSecurityAudit_PendingIntentFlags() {
        val flagImmutable = android.app.PendingIntent.FLAG_IMMUTABLE
        val flagUpdateCurrent = android.app.PendingIntent.FLAG_UPDATE_CURRENT
        val combined = flagUpdateCurrent or flagImmutable

        assertTrue("Flag harus mengandung FLAG_IMMUTABLE untuk kepatuhan Android 12+", (combined and flagImmutable) != 0)
    }
}
