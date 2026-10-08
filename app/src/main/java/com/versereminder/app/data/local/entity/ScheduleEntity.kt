package com.versereminder.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.versereminder.app.domain.model.Schedule
import com.versereminder.app.domain.model.VerseCategory

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val hour: Int,
    val minute: Int,
    val category: String,
    val isEnabled: Boolean
) {
    fun toDomain(): Schedule {
        return Schedule(
            id = id,
            title = title,
            hour = hour,
            minute = minute,
            category = VerseCategory.fromString(category),
            isEnabled = isEnabled
        )
    }

    companion object {
        fun fromDomain(domain: Schedule): ScheduleEntity {
            return ScheduleEntity(
                id = domain.id,
                title = domain.title,
                hour = domain.hour,
                minute = domain.minute,
                category = domain.category.name,
                isEnabled = domain.isEnabled
            )
        }
    }
}
