package com.versereminder.app.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.versereminder.app.VerseReminderApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == NotificationHelper.ACTION_BOOKMARK) {
            val verseId = intent.getLongExtra(NotificationHelper.EXTRA_VERSE_ID, -1L)
            if (verseId > 0) {
                val app = context.applicationContext as? VerseReminderApp ?: return
                CoroutineScope(Dispatchers.IO).launch {
                    app.verseRepository.addBookmark(verseId)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Ayat disimpan ke Bookmark ⭐", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
