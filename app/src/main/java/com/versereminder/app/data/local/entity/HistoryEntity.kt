package com.versereminder.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val verseId: Long,
    val displayedAt: Long = System.currentTimeMillis(),
    val source: String = "Daily Verse"
)
