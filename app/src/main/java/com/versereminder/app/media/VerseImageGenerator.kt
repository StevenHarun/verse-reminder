package com.versereminder.app.media

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.Verse
import java.io.File
import java.io.FileOutputStream

object VerseImageGenerator {

    private const val COLOR_BG_TOP = 0xFF080A10.toInt()
    private const val COLOR_BG_BOTTOM = 0xFF121622.toInt()
    private const val COLOR_CARD_BG = 0xFF151A26.toInt()
    private const val COLOR_GOLD = 0xFFD4AF37.toInt()
    private const val COLOR_GOLD_TEXT = 0xFFE5C158.toInt()
    private const val COLOR_CREAM_VERSE = 0xFFEADBB6.toInt()
    private const val COLOR_MUTED = 0xFF8A93A6.toInt()

    fun generateVerseCardBitmap(
        context: Context,
        verse: Verse,
        bibleVersion: BibleVersion,
        isStoryFormat: Boolean = true
    ): Bitmap {
        val width = 1080
        val height = if (isStoryFormat) 1920 else 1080
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 1. Background Gradient
        val bgPaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, 0f, height.toFloat(),
                COLOR_BG_TOP, COLOR_BG_BOTTOM,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // 2. Main Center Card Dimensions
        val cardMarginHorizontal = 72f
        val cardMarginVertical = if (isStoryFormat) 280f else 80f
        val cardRect = RectF(
            cardMarginHorizontal,
            cardMarginVertical,
            width - cardMarginHorizontal,
            height - cardMarginVertical
        )
        val cardCornerRadius = 44f

        // Draw Card Background
        val cardBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = COLOR_CARD_BG
        }
        canvas.drawRoundRect(cardRect, cardCornerRadius, cardCornerRadius, cardBgPaint)

        // Draw Gold Border Stroke
        val cardBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = COLOR_GOLD
            alpha = 180
            style = Paint.Style.STROKE
            strokeWidth = 3.5f
        }
        canvas.drawRoundRect(cardRect, cardCornerRadius, cardCornerRadius, cardBorderPaint)

        // 3. Watermark Quote Mark “ in background
        val watermarkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = COLOR_GOLD
            alpha = 24
            textSize = 280f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }
        canvas.drawText("“", cardRect.right - 180f, cardRect.top + 220f, watermarkPaint)

        // 4. Header: App Branding & Tag
        val brandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = COLOR_GOLD_TEXT
            textSize = 26f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            letterSpacing = 0.2f
        }
        canvas.drawText("❖ VERSE REMINDER", cardRect.left + 54f, cardRect.top + 76f, brandPaint)

        // Category Tag Pill
        val tagText = "${verse.category.icon}  ${verse.themeTag}"
        val tagTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = COLOR_GOLD
            textSize = 22f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }
        val tagWidth = tagTextPaint.measureText(tagText) + 36f
        val tagRect = RectF(
            cardRect.left + 54f,
            cardRect.top + 104f,
            cardRect.left + 54f + tagWidth,
            cardRect.top + 150f
        )
        val tagBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = COLOR_GOLD
            alpha = 36
        }
        canvas.drawRoundRect(tagRect, 14f, 14f, tagBgPaint)
        canvas.drawText(tagText, tagRect.left + 18f, tagRect.top + 32f, tagTextPaint)

        // 5. Verse Text Rendering (Serif, elegant line-wrapped)
        val verseText = "\"${verse.getTextForVersion(bibleVersion)}\""
        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = COLOR_CREAM_VERSE
            textSize = if (verseText.length > 200) 40f else if (verseText.length > 120) 46f else 52f
            typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
        }

        val textWidth = (cardRect.width() - 108f).toInt()
        val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(verseText, 0, verseText.length, textPaint, textWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(12f, 1.35f)
                .setIncludePad(true)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(
                verseText,
                textPaint,
                textWidth,
                Layout.Alignment.ALIGN_NORMAL,
                1.35f,
                12f,
                true
            )
        }

        // Vertically balance verse text in the card
        val availableHeight = cardRect.height() - 380f
        val contentY = cardRect.top + 200f + ((availableHeight - staticLayout.height) / 2f).coerceAtLeast(0f)

        canvas.save()
        canvas.translate(cardRect.left + 54f, contentY)
        staticLayout.draw(canvas)
        canvas.restore()

        // 6. Scripture Reference below the text
        val refY = contentY + staticLayout.height + 64f
        val refPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = COLOR_GOLD
            textSize = 34f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }
        val refString = "— ${verse.reference} (${bibleVersion.code})"
        canvas.drawText(refString, cardRect.left + 54f, refY, refPaint)

        // 7. Footer Devotional Caption
        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = COLOR_MUTED
            textSize = 24f
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
            textAlign = Paint.Align.CENTER
        }
        val footerText = if (isStoryFormat) {
            "« Firman-Mu itu pelita bagi kakiku dan terang bagi jalanku » • Mazmur 119:105"
        } else {
            "Dibagikan dari Verse Reminder"
        }
        canvas.drawText(footerText, width / 2f, cardRect.bottom - 46f, footerPaint)

        return bitmap
    }

    fun shareVerseCard(
        context: Context,
        verse: Verse,
        bibleVersion: BibleVersion,
        isStoryFormat: Boolean = true
    ) {
        val bitmap = generateVerseCardBitmap(context, verse, bibleVersion, isStoryFormat)
        val cacheFolder = File(context.cacheDir, "shared_verses")
        if (!cacheFolder.exists()) {
            cacheFolder.mkdirs()
        }

        val file = File(cacheFolder, "verse_${verse.id}_${System.currentTimeMillis()}.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        val contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            putExtra(Intent.EXTRA_SUBJECT, "Ayat Alkitab: ${verse.reference}")
            putExtra(
                Intent.EXTRA_TEXT,
                "📖 ${verse.reference} (${bibleVersion.code})\n\n\"${verse.getTextForVersion(bibleVersion)}\"\n\n— Dibagikan melalui Verse Reminder"
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Bagikan Gambar Ayat"))
    }
}
