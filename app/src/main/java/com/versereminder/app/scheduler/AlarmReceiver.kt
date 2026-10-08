package com.versereminder.app.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.versereminder.app.VerseReminderApp
import com.versereminder.app.domain.model.VerseCategory
import com.versereminder.app.widget.VerseWidgetUpdater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val app = context.applicationContext as? VerseReminderApp ?: return
        val scheduleId = intent.getLongExtra(AlarmScheduler.EXTRA_SCHEDULE_ID, -1L)
        val scheduleTitle = intent.getStringExtra(AlarmScheduler.EXTRA_SCHEDULE_TITLE) ?: "Pengingat Ayat"
        val categoryStr = intent.getStringExtra(AlarmScheduler.EXTRA_SCHEDULE_CATEGORY) ?: "ALL"
        val category = VerseCategory.fromString(categoryStr)

        CoroutineScope(Dispatchers.IO).launch {
            val settingsRepo = app.settingsRepository
            val isEnabled = settingsRepo.notificationEnabledFlow.first()
            if (isEnabled) {
                val verseRepo = app.verseRepository
                val verse = verseRepo.getRandomVerseByCategory(category)
                if (verse != null) {
                    val bibleVersion = settingsRepo.bibleVersionFlow.first()
                    val previewMode = settingsRepo.previewModeFlow.first()
                    val soundEnabled = settingsRepo.notificationSoundFlow.first()
                    val vibrateEnabled = settingsRepo.notificationVibrateFlow.first()

                    val notificationHelper = NotificationHelper(context)
                    notificationHelper.showVerseNotification(
                        notificationId = if (scheduleId > 0) scheduleId.toInt() else 1001,
                        title = scheduleTitle,
                        verse = verse,
                        bibleVersion = bibleVersion,
                        previewMode = previewMode,
                        soundEnabled = soundEnabled,
                        vibrateEnabled = vibrateEnabled
                    )

                    // Record to history
                    verseRepo.recordHistory(verse.id, "Jadwal: $scheduleTitle")

                    // Update Home Screen Widget
                    VerseWidgetUpdater.updateWidget(context)
                }

                // Reschedule for next day if schedule still exists, is enabled, and notifications are globally enabled
                if (scheduleId > 0) {
                    val schedule = app.database.scheduleDao().getScheduleById(scheduleId)
                    if (schedule != null && schedule.isEnabled) {
                        val alarmScheduler = AlarmScheduler(context)
                        alarmScheduler.schedule(schedule.toDomain())
                    }
                }
            }
        }
    }
}
