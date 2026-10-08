package com.versereminder.app.widget

import android.content.Context
import androidx.glance.appwidget.updateAll

object VerseWidgetUpdater {
    suspend fun updateWidget(context: Context) {
        try {
            VerseWidget().updateAll(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
