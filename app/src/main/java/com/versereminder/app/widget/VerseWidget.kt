package com.versereminder.app.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.versereminder.app.MainActivity
import com.versereminder.app.VerseReminderApp
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.Verse
import kotlinx.coroutines.flow.first

class VerseWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val app = context.applicationContext as? VerseReminderApp
        val verseRepo = app?.verseRepository
        val settingsRepo = app?.settingsRepository

        val verse = verseRepo?.getDailyVerse()
        val version = settingsRepo?.bibleVersionFlow?.first() ?: BibleVersion.TB

        provideContent {
            GlanceTheme {
                VerseWidgetContent(
                    context = context,
                    verse = verse,
                    bibleVersion = version
                )
            }
        }
    }
}

@Composable
private fun VerseWidgetContent(
    context: Context,
    verse: Verse?,
    bibleVersion: BibleVersion
) {
    val widgetBg = Color(0xFF0F172A)
    val quoteBoxBg = Color(0xFF1E293B)
    val textWhite = Color(0xFFF8FAFC)
    val textMuted = Color(0xFF94A3B8)
    val goldAccent = Color(0xFFF5BE47)

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(widgetBg)
            .cornerRadius(20.dp)
            .padding(14.dp)
            .clickable(actionStartActivity<MainActivity>())
    ) {
        Column(modifier = GlanceModifier.fillMaxSize()) {
            // Header Row
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📖 Ayat Hari Ini",
                    style = TextStyle(
                        color = ColorProvider(goldAccent),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = GlanceModifier.defaultWeight())
                if (verse != null) {
                    Text(
                        text = "${verse.category.icon} ${verse.themeTag}",
                        style = TextStyle(
                            color = ColorProvider(textMuted),
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            // Main Scripture Body Box
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .defaultWeight()
                    .background(quoteBoxBg)
                    .cornerRadius(12.dp)
                    .padding(10.dp)
            ) {
                if (verse != null) {
                    val contentText = verse.getTextForVersion(bibleVersion)
                    Text(
                        text = "\"$contentText\"",
                        maxLines = 4,
                        style = TextStyle(
                            color = ColorProvider(textWhite),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                } else {
                    Text(
                        text = "Ketuk untuk membuka ayat harian Anda.",
                        style = TextStyle(
                            color = ColorProvider(textMuted),
                            fontSize = 12.sp
                        )
                    )
                }
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            // Footer with Scripture Reference & Actions
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = verse?.reference ?: "Alkitab",
                    style = TextStyle(
                        color = ColorProvider(goldAccent),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = GlanceModifier.defaultWeight())

                // Quick Shuffle / Refresh button
                Box(
                    modifier = GlanceModifier
                        .clickable(actionRunCallback<RefreshWidgetAction>())
                        .cornerRadius(8.dp)
                        .background(Color(0xFF334155))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "🔄 Ganti",
                        style = TextStyle(
                            color = ColorProvider(textWhite),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.width(6.dp))

                // Quick Bookmark button
                if (verse != null) {
                    val paramKey = ActionParameters.Key<Long>("verse_id")
                    Box(
                        modifier = GlanceModifier
                            .clickable(
                                actionRunCallback<BookmarkWidgetAction>(
                                    actionParametersOf(paramKey to verse.id)
                                )
                            )
                            .cornerRadius(8.dp)
                            .background(if (verse.isBookmarked) Color(0xFF6B4A0E) else Color(0xFF334155))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (verse.isBookmarked) "⭐ Tersimpan" else "☆ Simpan",
                            style = TextStyle(
                                color = ColorProvider(if (verse.isBookmarked) goldAccent else textWhite),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

class RefreshWidgetAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val app = context.applicationContext as? VerseReminderApp ?: return
        val randomVerse = app.verseRepository.getRandomVerseByCategory(com.versereminder.app.domain.model.VerseCategory.ALL)
        if (randomVerse != null) {
            val todayStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
            app.settingsRepository.saveDailyVerseCache(todayStr, randomVerse.id)
        }
        VerseWidget().update(context, glanceId)
    }
}

class BookmarkWidgetAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val app = context.applicationContext as? VerseReminderApp ?: return
        val paramKey = ActionParameters.Key<Long>("verse_id")
        val verseId = parameters[paramKey] ?: return
        app.verseRepository.toggleBookmark(verseId)
        VerseWidget().update(context, glanceId)
    }
}
