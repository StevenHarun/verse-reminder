package com.versereminder.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.versereminder.app.domain.model.ReadingPlanProgress

@Entity(tableName = "reading_plan_progress")
data class ReadingPlanProgressEntity(
    @PrimaryKey
    val planId: String,
    val completedDays: String, // Comma-separated: "1,2,3"
    val currentDay: Int,
    val lastReadTimestamp: Long,
    val isCompleted: Boolean
) {
    fun toDomain(): ReadingPlanProgress {
        val daysSet = if (completedDays.isBlank()) {
            emptySet()
        } else {
            completedDays.split(",").mapNotNull { it.trim().toIntOrNull() }.toSet()
        }
        return ReadingPlanProgress(
            planId = planId,
            completedDays = daysSet,
            currentDay = currentDay,
            lastReadTimestamp = lastReadTimestamp,
            isCompleted = isCompleted
        )
    }

    companion object {
        fun fromDomain(domain: ReadingPlanProgress): ReadingPlanProgressEntity {
            return ReadingPlanProgressEntity(
                planId = domain.planId,
                completedDays = domain.completedDays.sorted().joinToString(","),
                currentDay = domain.currentDay,
                lastReadTimestamp = domain.lastReadTimestamp,
                isCompleted = domain.isCompleted
            )
        }
    }
}
