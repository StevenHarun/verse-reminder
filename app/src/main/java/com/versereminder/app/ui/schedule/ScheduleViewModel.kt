package com.versereminder.app.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.versereminder.app.data.repository.ScheduleRepository
import com.versereminder.app.domain.model.Schedule
import com.versereminder.app.domain.model.VerseCategory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    val schedules: StateFlow<List<Schedule>> = scheduleRepository.getAllSchedules()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleSchedule(schedule: Schedule, isEnabled: Boolean) {
        viewModelScope.launch {
            scheduleRepository.toggleSchedule(schedule, isEnabled)
        }
    }

    fun addSchedule(title: String, hour: Int, minute: Int, category: VerseCategory) {
        viewModelScope.launch {
            val newSchedule = Schedule(
                title = title.ifBlank { "Pengingat Ayat" },
                hour = hour,
                minute = minute,
                category = category,
                isEnabled = true
            )
            scheduleRepository.addSchedule(newSchedule)
        }
    }

    fun updateSchedule(schedule: Schedule, title: String, hour: Int, minute: Int, category: VerseCategory) {
        viewModelScope.launch {
            val updated = schedule.copy(
                title = title.ifBlank { schedule.title },
                hour = hour,
                minute = minute,
                category = category
            )
            scheduleRepository.updateSchedule(updated)
        }
    }

    fun deleteSchedule(scheduleId: Long) {
        viewModelScope.launch {
            scheduleRepository.deleteSchedule(scheduleId)
        }
    }

    class Factory(
        private val scheduleRepository: ScheduleRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ScheduleViewModel(scheduleRepository) as T
        }
    }
}
