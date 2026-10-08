package com.versereminder.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.versereminder.app.domain.model.Verse
import com.versereminder.app.domain.model.VerseCategory

@Entity(tableName = "verses")
data class VerseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val book: String,
    val chapter: Int,
    val verse: Int,
    val reference: String,
    val textId: String,
    val textEn: String,
    val category: String,
    val themeTag: String,
    @ColumnInfo(defaultValue = "0")
    val isCustom: Boolean = false
) {
    fun toDomain(isBookmarked: Boolean = false): Verse {
        return Verse(
            id = id,
            book = book,
            chapter = chapter,
            verse = verse,
            reference = reference,
            textId = textId,
            textEn = textEn,
            category = VerseCategory.fromString(category),
            themeTag = themeTag,
            isBookmarked = isBookmarked,
            isCustom = isCustom
        )
    }
}
