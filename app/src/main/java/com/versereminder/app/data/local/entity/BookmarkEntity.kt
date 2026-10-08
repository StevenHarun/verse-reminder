package com.versereminder.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey
    val verseId: Long,
    val savedAt: Long = System.currentTimeMillis()
)
