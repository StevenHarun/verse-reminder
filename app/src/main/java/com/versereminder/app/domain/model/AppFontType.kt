package com.versereminder.app.domain.model

import androidx.compose.ui.text.font.FontFamily

enum class AppFontType(val displayName: String, val description: String) {
    SERIF("Serif Klasik", "Anggun dan sakral untuk perenungan"),
    SANS("Modern Sans", "Jernih, bersih, dan mudah dibaca"),
    ROUNDED("Rounded Soft", "Hangat, santai, dan nyaman untuk mata"),
    MONOSPACE("Naskah Otentik", "Klasik bergaya manuskrip Alkitab");

    val fontFamily: FontFamily
        get() = when (this) {
            SERIF -> FontFamily.Serif
            SANS -> FontFamily.SansSerif
            ROUNDED -> FontFamily.Default
            MONOSPACE -> FontFamily.Monospace
        }

    companion object {
        fun fromString(value: String): AppFontType {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: SERIF
        }
    }
}
