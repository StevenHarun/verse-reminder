package com.versereminder.app.domain.model

data class ReadingPlan(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val iconEmoji: String,
    val totalDays: Int,
    val category: VerseCategory,
    val days: List<ReadingPlanDay>
)

data class ReadingPlanDay(
    val dayNumber: Int,
    val title: String,
    val verseReference: String,
    val verseTextId: String,
    val verseTextEn: String,
    val devotionalText: String,
    val prayerText: String
)

data class ReadingPlanProgress(
    val planId: String,
    val completedDays: Set<Int> = emptySet(),
    val currentDay: Int = 1,
    val lastReadTimestamp: Long = 0L,
    val isCompleted: Boolean = false
) {
    fun getCompletionPercentage(totalDays: Int): Float {
        if (totalDays <= 0) return 0f
        return (completedDays.size.toFloat() / totalDays.toFloat()).coerceIn(0f, 1f)
    }

    fun isDayCompleted(dayNumber: Int): Boolean {
        return completedDays.contains(dayNumber)
    }
}
