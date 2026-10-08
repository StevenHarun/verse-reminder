package com.versereminder.app

import android.app.Application
import com.versereminder.app.data.local.AppDatabase
import com.versereminder.app.data.repository.AuthRepository
import com.versereminder.app.data.repository.ScheduleRepository
import com.versereminder.app.data.repository.SettingsRepository
import com.versereminder.app.data.repository.VerseRepository
import com.versereminder.app.scheduler.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class VerseReminderApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val authRepository by lazy { AuthRepository(this) }
    val settingsRepository by lazy { SettingsRepository(this) }
    val alarmScheduler by lazy { AlarmScheduler(this) }
    val ttsManager by lazy { com.versereminder.app.media.VerseTextToSpeechManager(this) }

    val verseRepository by lazy {
        VerseRepository(
            verseDao = database.verseDao(),
            bookmarkDao = database.bookmarkDao(),
            historyDao = database.historyDao(),
            settingsRepository = settingsRepository
        )
    }

    val readingPlanRepository by lazy {
        com.versereminder.app.data.repository.ReadingPlanRepository(
            readingPlanDao = database.readingPlanDao()
        )
    }

    val scheduleRepository by lazy {
        ScheduleRepository(
            scheduleDao = database.scheduleDao(),
            onScheduleChanged = { schedule ->
                if (schedule.isEnabled) {
                    alarmScheduler.schedule(schedule)
                } else {
                    alarmScheduler.cancel(schedule.id)
                }
            },
            onScheduleDeleted = { scheduleId ->
                alarmScheduler.cancel(scheduleId)
            }
        )
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize and schedule all active reminders safely
        applicationScope.launch {
            try {
                // Ensure pre-population runs if database is newly opened
                AppDatabase.populateInitialData(this@VerseReminderApp, database)
            } catch (e: Throwable) {
                e.printStackTrace()
            }

            try {
                val activeSchedules = scheduleRepository.getActiveSchedules()
                for (schedule in activeSchedules) {
                    alarmScheduler.schedule(schedule)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}
