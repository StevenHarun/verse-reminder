package com.versereminder.app.domain.model

data class Schedule(
    val id: Long = 0,
    val title: String,
    val hour: Int,
    val minute: Int,
    val category: VerseCategory = VerseCategory.ALL,
    val isEnabled: Boolean = true
) {
    val formattedTime: String
        get() = String.format("%02d:%02d", hour, minute)
}
