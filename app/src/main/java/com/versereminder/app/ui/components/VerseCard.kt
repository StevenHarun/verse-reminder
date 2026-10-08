package com.versereminder.app.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.vector.ImageVector
import com.versereminder.app.media.VerseImageGenerator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.versereminder.app.domain.model.AppFontType
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.Verse
import com.versereminder.app.ui.theme.SacredGold
import com.versereminder.app.ui.theme.StarActiveGold

@Composable
fun VerseCard(
    verse: Verse,
    bibleVersion: BibleVersion = BibleVersion.TB,
    fontSizeScale: Float = 1.0f,
    fontType: AppFontType = AppFontType.SERIF,
    modifier: Modifier = Modifier,
    isDailyHighlight: Boolean = false,
    isPlayingAudio: Boolean = false,
    onPlayAudioClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
    onBookmarkClick: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val verseText = verse.getTextForVersion(bibleVersion)

    var showShareDialog by remember { mutableStateOf(false) }

    // Bouncy scale animation for bookmark action
    var bookmarkTrigger by remember { mutableStateOf(false) }
    val bookmarkScale by animateFloatAsState(
        targetValue = if (bookmarkTrigger) 1.35f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = { bookmarkTrigger = false },
        label = "bookmark_spring"
    )

    val bookmarkColor by animateColorAsState(
        targetValue = if (verse.isBookmarked) StarActiveGold else if (isDailyHighlight) Color(0xFFE2E8F0) else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "bookmark_color"
    )

    // Highlight Card Gradient (Spiritual Dawn / Twilight Indigo)
    val cardBrush = if (isDailyHighlight) {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF1E2E4A),
                Color(0xFF283F66),
                Color(0xFF1B283E)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
        )
    }

    val cardBorder = if (isDailyHighlight) {
        BorderStroke(
            width = 1.2.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    SacredGold.copy(alpha = 0.7f),
                    Color(0xFF60A5FA).copy(alpha = 0.3f),
                    Color.Transparent
                )
            )
        )
    } else {
        BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        border = cardBorder,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDailyHighlight) 6.dp else 1.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardBrush)
                .padding(22.dp)
        ) {
            // Elegant Scripture Watermark Quotation Mark in the background
            Text(
                text = "“",
                fontSize = 110.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = if (isDailyHighlight) SacredGold.copy(alpha = 0.10f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = (-28).dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                // Header Row: Category Badge + Bible Version pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isDailyHighlight) {
                            SacredGold.copy(alpha = 0.22f)
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "${verse.category.icon}  ${verse.themeTag}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.4.sp
                                ),
                                color = if (isDailyHighlight) SacredGold else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (verse.isCustom) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SacredGold.copy(alpha = 0.2f),
                                border = BorderStroke(0.8.dp, SacredGold)
                            ) {
                                Text(
                                    text = "PRIBADI",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SacredGold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = if (isDailyHighlight) Color.White.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                        ) {
                            Text(
                                text = bibleVersion.code,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isDailyHighlight) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Smooth animated scripture content on changes
                AnimatedContent(
                    targetState = verseText,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(250))
                    },
                    label = "verse_text_transition"
                ) { text ->
                    val baseSp = if (isDailyHighlight) 19f else 16f
                    val currentSp = (baseSp * fontSizeScale).sp
                    val lineSp = (currentSp.value * 1.55f).sp

                    Text(
                        text = "\"$text\"",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = fontType.fontFamily,
                            fontSize = currentSp,
                            lineHeight = lineSp,
                            letterSpacing = 0.2.sp
                        ),
                        color = if (isDailyHighlight) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Footer Row: Reference + Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = verse.reference,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.3.sp
                            ),
                            color = if (isDailyHighlight) SacredGold else MaterialTheme.colorScheme.primary
                        )
                        if (isDailyHighlight) {
                            Text(
                                text = "Ayat Hari Ini",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Copy Button with ripple
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText(
                                    "Ayat Alkitab",
                                    "📖 ${verse.reference}\n\n\"$verseText\"\n\n— Verse Reminder"
                                )
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Ayat disalin ke papan klip ✨", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isDailyHighlight) Color.White.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Salin ayat",
                                tint = if (isDailyHighlight) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        // Audio Text-to-Speech (TTS) Button
                        if (onPlayAudioClick != null) {
                            IconButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onPlayAudioClick()
                                },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isPlayingAudio) SacredGold.copy(alpha = 0.35f)
                                        else if (isDailyHighlight) Color.White.copy(alpha = 0.12f)
                                        else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                                    )
                            ) {
                                Icon(
                                    imageVector = if (isPlayingAudio) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = if (isPlayingAudio) "Hentikan audio" else "Dengarkan audio ayat",
                                    tint = if (isPlayingAudio) SacredGold else if (isDailyHighlight) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Visual & Text Share Button (Opens Modal with Story & Post Image Options)
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                showShareDialog = true
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isDailyHighlight) Color.White.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Bagikan ayat",
                                tint = if (isDailyHighlight) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        // Bookmark Button with Spring Pop Animation
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                bookmarkTrigger = true
                                onBookmarkClick()
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .scale(bookmarkScale)
                                .clip(CircleShape)
                                .background(if (isDailyHighlight) Color.White.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = if (verse.isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = if (verse.isBookmarked) "Tersimpan di bookmark" else "Simpan ke bookmark",
                                tint = bookmarkColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Delete button for user's own custom verses
                        if (verse.isCustom && onDeleteClick != null) {
                            IconButton(
                                onClick = onDeleteClick,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Hapus ayat pribadi",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Dialog: Bagikan Firman (Visual Story / Feed Image & Text)
    if (showShareDialog) {
        AlertDialog(
            onDismissRequest = { showShareDialog = false },
            containerColor = Color(0xFF12151F),
            title = {
                Column {
                    Text(
                        text = "Bagikan Firman Tuhan",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFFEADBB6)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${verse.reference} (${bibleVersion.code})",
                        fontSize = 12.sp,
                        color = SacredGold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Option 1: WhatsApp / IG Story (9:16)
                    ShareOptionCard(
                        icon = Icons.Default.Image,
                        title = "Gambar Cerita (Story 9:16)",
                        subtitle = "Format vertikal untuk WhatsApp & IG Story",
                        onClick = {
                            showShareDialog = false
                            VerseImageGenerator.shareVerseCard(context, verse, bibleVersion, isStoryFormat = true)
                        }
                    )

                    // Option 2: Instagram Feed / Chat (1:1)
                    ShareOptionCard(
                        icon = Icons.Default.CropSquare,
                        title = "Gambar Persegi (Feed 1:1)",
                        subtitle = "Format persegi untuk postingan atau chat",
                        onClick = {
                            showShareDialog = false
                            VerseImageGenerator.shareVerseCard(context, verse, bibleVersion, isStoryFormat = false)
                        }
                    )

                    // Option 3: Plain text share
                    ShareOptionCard(
                        icon = Icons.Default.Share,
                        title = "Bagikan Teks Firman",
                        subtitle = "Kirim kutipan ayat dan referensi dalam bentuk teks",
                        onClick = {
                            showShareDialog = false
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Ayat Alkitab: ${verse.reference}")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "📖 ${verse.reference} (${bibleVersion.code})\n\n\"$verseText\"\n\n🌿 Bersyukur untuk hari ini — Dikirim melalui Verse Reminder"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Bagikan Firman Tuhan"))
                        }
                    )
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showShareDialog = false }) {
                    Text("Tutup", color = Color(0xFF8A93A6))
                }
            }
        )
    }
}

@Composable
private fun ShareOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2232)),
        border = BorderStroke(1.dp, SacredGold.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = SacredGold.copy(alpha = 0.18f),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = SacredGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFFEADBB6)
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = Color(0xFF8A93A6)
                )
            }
        }
    }
}
