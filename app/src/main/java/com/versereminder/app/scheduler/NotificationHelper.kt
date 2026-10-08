package com.versereminder.app.scheduler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.versereminder.app.MainActivity
import com.versereminder.app.R
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.Verse

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "daily_verse_reminder_channel"
        const val CHANNEL_ID_NO_SOUND = "daily_verse_reminder_no_sound"
        const val CHANNEL_ID_NO_VIBRATE = "daily_verse_reminder_no_vibrate"
        const val CHANNEL_ID_SILENT = "daily_verse_reminder_silent"

        const val CHANNEL_NAME = "Pengingat Ayat Alkitab"
        const val CHANNEL_DESC = "Notifikasi ayat harian dan jadwal pengingat firman Tuhan"
        const val ACTION_BOOKMARK = "com.versereminder.app.ACTION_BOOKMARK"
        const val EXTRA_VERSE_ID = "extra_verse_id"
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelDefault = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESC
                enableVibration(true)
            }

            val channelNoSound = NotificationChannel(
                CHANNEL_ID_NO_SOUND,
                "$CHANNEL_NAME (Getar Saja)",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifikasi getar tanpa suara"
                enableVibration(true)
                setSound(null, null)
            }

            val channelNoVibrate = NotificationChannel(
                CHANNEL_ID_NO_VIBRATE,
                "$CHANNEL_NAME (Suara Saja)",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifikasi bersuara tanpa getar"
                enableVibration(false)
            }

            val channelSilent = NotificationChannel(
                CHANNEL_ID_SILENT,
                "$CHANNEL_NAME (Senyap)",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifikasi senyap tanpa suara dan getar"
                enableVibration(false)
                setSound(null, null)
            }

            notificationManager.createNotificationChannels(
                listOf(channelDefault, channelNoSound, channelNoVibrate, channelSilent)
            )
        }
    }

    fun showVerseNotification(
        notificationId: Int,
        title: String,
        verse: Verse,
        bibleVersion: BibleVersion = BibleVersion.TB,
        previewMode: String = "FULL",
        soundEnabled: Boolean = true,
        vibrateEnabled: Boolean = true
    ) {
        val verseText = verse.getTextForVersion(bibleVersion)

        val contentText = when (previewMode) {
            "REF_ONLY" -> verse.reference
            "SHORT" -> {
                val short = if (verseText.length > 60) verseText.take(57) + "..." else verseText
                "\"$short\" — ${verse.reference}"
            }
            else -> "\"$verseText\" — ${verse.reference}"
        }

        // Determine target notification channel based on user settings
        val targetChannelId = when {
            soundEnabled && vibrateEnabled -> CHANNEL_ID
            !soundEnabled && vibrateEnabled -> CHANNEL_ID_NO_SOUND
            soundEnabled && !vibrateEnabled -> CHANNEL_ID_NO_VIBRATE
            else -> CHANNEL_ID_SILENT
        }

        // 1. Open app PendingIntent
        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("EXTRA_VERSE_ID", verse.id)
        }
        val tapPendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 2. Share Action PendingIntent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Ayat Alkitab: ${verse.reference}")
            putExtra(Intent.EXTRA_TEXT, "📖 ${verse.reference}\n\n\"$verseText\"\n\n— Dibagikan dari Verse Reminder")
        }
        val chooserIntent = Intent.createChooser(shareIntent, "Bagikan Ayat")
        val sharePendingIntent = PendingIntent.getActivity(
            context,
            notificationId + 1000,
            chooserIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 3. Bookmark Action PendingIntent
        val bookmarkIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = ACTION_BOOKMARK
            putExtra(EXTRA_VERSE_ID, verse.id)
            putExtra("extra_notification_id", notificationId)
        }
        val bookmarkPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId + 2000,
            bookmarkIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, targetChannelId)
            .setSmallIcon(R.drawable.ic_verse_notification)
            .setContentTitle("🔔 $title")
            .setContentText(contentText)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("\"$verseText\"\n\n${verse.reference} (${bibleVersion.code})")
                    .setSummaryText(verse.themeTag)
            )
            .setPriority(if (!soundEnabled && !vibrateEnabled) NotificationCompat.PRIORITY_LOW else NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(tapPendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_verse_notification, "Simpan", bookmarkPendingIntent)
            .addAction(R.drawable.ic_verse_notification, "Bagikan", sharePendingIntent)

        if (!soundEnabled && !vibrateEnabled) {
            notificationBuilder.setSilent(true)
        } else {
            if (!soundEnabled) {
                notificationBuilder.setSound(null)
            }
            if (!vibrateEnabled) {
                notificationBuilder.setVibrate(longArrayOf(0))
            }
        }

        val notification = notificationBuilder.build()
        notificationManager.notify(notificationId, notification)
    }
}
