package com.versereminder.app.data.repository

import com.versereminder.app.data.local.SeedVerses
import com.versereminder.app.data.local.dao.BookmarkDao
import com.versereminder.app.data.local.dao.HistoryDao
import com.versereminder.app.data.local.dao.HistoryWithVerse
import com.versereminder.app.data.local.dao.VerseDao
import com.versereminder.app.data.local.entity.BookmarkEntity
import com.versereminder.app.data.local.entity.HistoryEntity
import com.versereminder.app.data.local.entity.VerseEntity
import com.versereminder.app.domain.model.Verse
import com.versereminder.app.domain.model.VerseCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class VerseRepository(
    private val verseDao: VerseDao,
    private val bookmarkDao: BookmarkDao,
    private val historyDao: HistoryDao,
    private val settingsRepository: SettingsRepository
) {

    fun getAllVerses(): Flow<List<Verse>> {
        return combine(
            verseDao.getAllVerses(),
            bookmarkDao.getAllBookmarkedVerseIds()
        ) { verses, bookmarkedIds ->
            val idSet = bookmarkedIds.toSet()
            verses.map { it.toDomain(isBookmarked = idSet.contains(it.id)) }
        }
    }

    fun getVersesByCategory(category: VerseCategory): Flow<List<Verse>> {
        val flow = if (category == VerseCategory.ALL) {
            verseDao.getAllVerses()
        } else {
            verseDao.getVersesByCategory(category.name)
        }

        return combine(flow, bookmarkDao.getAllBookmarkedVerseIds()) { verses, bookmarkedIds ->
            val idSet = bookmarkedIds.toSet()
            if (verses.isEmpty()) {
                val fallbackList = if (category == VerseCategory.ALL) {
                    SeedVerses.list
                } else {
                    SeedVerses.list.filter { it.category == category }
                }
                fallbackList.map { it.copy(isBookmarked = idSet.contains(it.id)) }
            } else {
                verses.map { it.toDomain(isBookmarked = idSet.contains(it.id)) }
            }
        }
    }

    fun getBookmarkedVerses(): Flow<List<Verse>> {
        return bookmarkDao.getBookmarkedVerses().combine(
            bookmarkDao.getAllBookmarkedVerseIds()
        ) { verses, _ ->
            verses.map { it.toDomain(isBookmarked = true) }
        }
    }

    fun getAllHistory(): Flow<List<HistoryWithVerse>> {
        return historyDao.getAllHistory()
    }

    fun getInitialDailyVerse(): Verse = SeedVerses.getDailySeedVerse()

    suspend fun getDailyVerse(): Verse {
        try {
            val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val (cachedDate, cachedVerseId) = settingsRepository.getDailyVerseCache()

            if (cachedDate == todayStr && cachedVerseId != null) {
                val cachedVerse = verseDao.getVerseById(cachedVerseId)
                if (cachedVerse != null) {
                    val isBookmarked = bookmarkDao.isBookmarked(cachedVerse.id)
                    return cachedVerse.toDomain(isBookmarked = isBookmarked)
                }
            }

            // Pick a verse based on day of year for stable daily selection
            val allVerses = verseDao.getAllVerses().first()
            if (allVerses.isNotEmpty()) {
                val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                val selectedIndex = dayOfYear % allVerses.size
                val chosenVerse = allVerses[selectedIndex]

                settingsRepository.saveDailyVerseCache(todayStr, chosenVerse.id)
                try {
                    historyDao.insertHistory(
                        HistoryEntity(
                            verseId = chosenVerse.id,
                            source = "Ayat Harian"
                        )
                    )
                } catch (_: Exception) {}

                val isBookmarked = bookmarkDao.isBookmarked(chosenVerse.id)
                return chosenVerse.toDomain(isBookmarked = isBookmarked)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return SeedVerses.getDailySeedVerse()
    }

    suspend fun getRandomVerseByCategory(category: VerseCategory): Verse? {
        val entity = if (category == VerseCategory.ALL) {
            verseDao.getRandomVerse()
        } else {
            verseDao.getRandomVerseByCategory(category.name)
        } ?: verseDao.getRandomVerse()

        return entity?.let {
            val isBookmarked = bookmarkDao.isBookmarked(it.id)
            it.toDomain(isBookmarked = isBookmarked)
        }
    }

    suspend fun toggleBookmark(verseId: Long): Boolean {
        val isCurrentlyBookmarked = bookmarkDao.isBookmarked(verseId)
        if (isCurrentlyBookmarked) {
            bookmarkDao.deleteBookmark(verseId)
            return false
        } else {
            bookmarkDao.insertBookmark(BookmarkEntity(verseId = verseId))
            return true
        }
    }

    suspend fun addBookmark(verseId: Long) {
        bookmarkDao.insertBookmark(BookmarkEntity(verseId = verseId))
    }

    suspend fun removeBookmark(verseId: Long) {
        bookmarkDao.deleteBookmark(verseId)
    }

    suspend fun recordHistory(verseId: Long, source: String) {
        historyDao.insertHistory(
            HistoryEntity(
                verseId = verseId,
                source = source
            )
        )
    }

    suspend fun addCustomVerse(
        reference: String,
        textId: String,
        textEn: String = "",
        category: VerseCategory = VerseCategory.HOPE
    ): Long {
        val cleanRef = reference.trim()
        val book = cleanRef.substringBefore(" ").ifBlank { "Pribadi" }
        val chapterVerse = cleanRef.substringAfter(" ", "1:1")
        val chapter = chapterVerse.substringBefore(":").toIntOrNull() ?: 1
        val verseNum = chapterVerse.substringAfter(":").substringBefore("-").substringBefore(",").toIntOrNull() ?: 1

        val entity = VerseEntity(
            book = book,
            chapter = chapter,
            verse = verseNum,
            reference = cleanRef,
            textId = textId.trim(),
            textEn = textEn.ifBlank { textId }.trim(),
            category = category.name,
            themeTag = category.displayName.uppercase(),
            isCustom = true
        )

        val newId = verseDao.insertVerse(entity)
        bookmarkDao.insertBookmark(BookmarkEntity(verseId = newId))
        return newId
    }

    suspend fun deleteCustomVerse(verseId: Long) {
        verseDao.deleteVerseById(verseId)
        bookmarkDao.deleteBookmark(verseId)
    }

    fun searchVerses(query: String): Flow<List<Verse>> {
        val cleanQuery = query.trim()
        if (cleanQuery.isEmpty()) {
            return getAllVerses()
        }
        return combine(
            verseDao.searchVerses(cleanQuery),
            bookmarkDao.getAllBookmarkedVerseIds()
        ) { verses, bookmarkedIds ->
            val idSet = bookmarkedIds.toSet()
            if (verses.isEmpty()) {
                SeedVerses.list.filter { verse ->
                    verse.reference.contains(cleanQuery, ignoreCase = true) ||
                            verse.textId.contains(cleanQuery, ignoreCase = true) ||
                            verse.textEn.contains(cleanQuery, ignoreCase = true) ||
                            verse.category.displayName.contains(cleanQuery, ignoreCase = true)
                }.map { it.copy(isBookmarked = idSet.contains(it.id)) }
            } else {
                verses.map { it.toDomain(isBookmarked = idSet.contains(it.id)) }
            }
        }
    }
}
