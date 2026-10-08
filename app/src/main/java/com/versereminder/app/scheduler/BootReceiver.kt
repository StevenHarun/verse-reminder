package com.versereminder.app.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.versereminder.app.VerseReminderApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON"
        ) {
            val app = context.applicationContext as? VerseReminderApp ?: return
            val alarmScheduler = AlarmScheduler(context)

            CoroutineScope(Dispatchers.IO).launch {
                val isEnabled = app.settingsRepository.notificationEnabledFlow.first()
                if (isEnabled) {
                    val activeSchedules = app.scheduleRepository.getActiveSchedules()
                    for (schedule in activeSchedules) {
                        alarmScheduler.schedule(schedule)
                    }
                }
            }
        }
    }
}
