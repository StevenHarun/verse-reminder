package com.versereminder.app.domain.model

data class Verse(
    val id: Long = 0,
    val book: String,
    val chapter: Int,
    val verse: Int,
    val reference: String,
    val textId: String,
    val textEn: String,
    val category: VerseCategory,
    val themeTag: String,
    val isBookmarked: Boolean = false,
    val isCustom: Boolean = false
) {
    fun getTextForVersion(version: BibleVersion): String {
        return if (version.isIndonesian) textId else textEn
    }
}
