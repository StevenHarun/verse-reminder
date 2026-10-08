package com.versereminder.app.data.repository

import com.versereminder.app.data.local.dao.ScheduleDao
import com.versereminder.app.data.local.entity.ScheduleEntity
import com.versereminder.app.domain.model.Schedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScheduleRepository(
    private val scheduleDao: ScheduleDao,
    private val onScheduleChanged: suspend (Schedule) -> Unit = {},
    private val onScheduleDeleted: suspend (Long) -> Unit = {}
) {

    fun getAllSchedules(): Flow<List<Schedule>> {
        return scheduleDao.getAllSchedules().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getActiveSchedules(): List<Schedule> {
        return scheduleDao.getActiveSchedules().map { it.toDomain() }
    }

    suspend fun addSchedule(schedule: Schedule): Long {
        val entity = ScheduleEntity.fromDomain(schedule)
        val id = scheduleDao.insertSchedule(entity)
        val updatedSchedule = schedule.copy(id = id)
        if (updatedSchedule.isEnabled) {
            onScheduleChanged(updatedSchedule)
        }
        return id
    }

    suspend fun updateSchedule(schedule: Schedule) {
        val entity = ScheduleEntity.fromDomain(schedule)
        scheduleDao.updateSchedule(entity)
        onScheduleChanged(schedule)
    }

    suspend fun toggleSchedule(schedule: Schedule, isEnabled: Boolean) {
        val updated = schedule.copy(isEnabled = isEnabled)
        scheduleDao.updateSchedule(ScheduleEntity.fromDomain(updated))
        onScheduleChanged(updated)
    }

    suspend fun deleteSchedule(scheduleId: Long) {
        scheduleDao.deleteScheduleById(scheduleId)
        onScheduleDeleted(scheduleId)
    }
}
