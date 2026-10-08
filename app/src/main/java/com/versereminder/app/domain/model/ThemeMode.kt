package com.versereminder.app.domain.model

enum class ThemeMode(val title: String) {
    SYSTEM("Sistem"),
    LIGHT("Terang"),
    DARK("Gelap");

    companion object {
        fun fromString(value: String): ThemeMode {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: SYSTEM
        }
    }
}
