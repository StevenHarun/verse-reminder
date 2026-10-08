package com.versereminder.app.domain.model

enum class BibleVersion(val code: String, val title: String, val language: String, val isIndonesian: Boolean) {
    TB("TB", "Terjemahan Baru (TB)", "Bahasa Indonesia", true),
    TB2("TB2", "Terjemahan Baru Edisi 2 (TB2)", "Bahasa Indonesia", true),
    BIS("BIS", "Alkitab Kabar Baik (BIS / BIMK)", "Bahasa Indonesia", true),
    TSI("TSI", "Terjemahan Sederhana Indonesia (TSI)", "Bahasa Indonesia", true),
    WEB("WEB", "World English Bible (WEB)", "English", false),
    KJV("KJV", "King James Version (KJV)", "English", false),
    NIV("NIV", "New International Version (NIV)", "English", false),
    ESV("ESV", "English Standard Version (ESV)", "English", false);

    val isEnglish: Boolean
        get() = !isIndonesian

    companion object {
        fun fromCode(code: String): BibleVersion {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: TB
        }
    }
}
