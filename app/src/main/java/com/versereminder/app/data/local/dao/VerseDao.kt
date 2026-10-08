package com.versereminder.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.versereminder.app.data.local.entity.VerseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VerseDao {

    @Query("SELECT * FROM verses ORDER BY id ASC")
    fun getAllVerses(): Flow<List<VerseEntity>>

    @Query("SELECT * FROM verses WHERE category = :category ORDER BY id ASC")
    fun getVersesByCategory(category: String): Flow<List<VerseEntity>>

    @Query("SELECT * FROM verses WHERE id = :id LIMIT 1")
    suspend fun getVerseById(id: Long): VerseEntity?

    @Query("SELECT * FROM verses ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomVerse(): VerseEntity?

    @Query("SELECT * FROM verses WHERE category = :category ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomVerseByCategory(category: String): VerseEntity?

    @Query("SELECT COUNT(*) FROM verses")
    suspend fun getVerseCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<VerseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerse(verse: VerseEntity): Long

    @Query("DELETE FROM verses WHERE id = :id")
    suspend fun deleteVerseById(id: Long)

    @Query("SELECT * FROM verses WHERE isCustom = 1 ORDER BY id DESC")
    fun getCustomVerses(): Flow<List<VerseEntity>>

    @Query("""
        SELECT * FROM verses 
        WHERE reference LIKE '%' || :query || '%' 
           OR textId LIKE '%' || :query || '%' 
           OR textEn LIKE '%' || :query || '%' 
           OR category LIKE '%' || :query || '%'
        ORDER BY isCustom DESC, id ASC
    """)
    fun searchVerses(query: String): Flow<List<VerseEntity>>
}
