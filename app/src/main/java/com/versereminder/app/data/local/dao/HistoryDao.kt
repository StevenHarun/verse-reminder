package com.versereminder.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.versereminder.app.data.local.entity.HistoryEntity
import com.versereminder.app.data.local.entity.VerseEntity
import kotlinx.coroutines.flow.Flow

data class HistoryWithVerse(
    val historyId: Long,
    val verseId: Long,
    val displayedAt: Long,
    val source: String,
    val book: String,
    val chapter: Int,
    val verse: Int,
    val reference: String,
    val textId: String,
    val textEn: String,
    val category: String,
    val themeTag: String
)

@Dao
interface HistoryDao {

    @Query("""
        SELECT h.id AS historyId, h.verseId AS verseId, h.displayedAt AS displayedAt, h.source AS source,
               v.book AS book, v.chapter AS chapter, v.verse AS verse, v.reference AS reference,
               v.textId AS textId, v.textEn AS textEn, v.category AS category, v.themeTag AS themeTag
        FROM history h
        INNER JOIN verses v ON h.verseId = v.id
        ORDER BY h.displayedAt DESC
    """)
    fun getAllHistory(): Flow<List<HistoryWithVerse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryEntity): Long

    @Query("DELETE FROM history")
    suspend fun clearHistory()
}
