package com.versereminder.app.domain.model

enum class VerseCategory(val displayName: String, val icon: String) {
    ALL("Semua", "📖"),
    PRAYER("Doa", "🙏"),
    LOVE("Kasih", "❤️"),
    STRENGTH("Kekuatan", "💪"),
    PEACE("Ketenangan", "😌"),
    GROWTH("Pertumbuhan", "🌱"),
    WISDOM("Hikmat", "💡"),
    HOPE("Pengharapan", "🌅"),
    FAITH("Iman", "🔥"),
    GRATITUDE("Syukur", "🙌");

    companion object {
        fun fromString(value: String): VerseCategory {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: ALL
        }
    }
}
