package com.versereminder.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.versereminder.app.data.local.entity.BookmarkEntity
import com.versereminder.app.data.local.entity.VerseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Query("SELECT v.* FROM verses v INNER JOIN bookmarks b ON v.id = b.verseId ORDER BY b.savedAt DESC")
    fun getBookmarkedVerses(): Flow<List<VerseEntity>>

    @Query("SELECT verseId FROM bookmarks")
    fun getAllBookmarkedVerseIds(): Flow<List<Long>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE verseId = :verseId)")
    fun observeIsBookmarked(verseId: Long): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE verseId = :verseId)")
    suspend fun isBookmarked(verseId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE verseId = :verseId")
    suspend fun deleteBookmark(verseId: Long)
}
