package com.versereminder.app.ui.readingplan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.versereminder.app.VerseReminderApp
import com.versereminder.app.domain.model.AppFontType
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.ReadingPlan
import com.versereminder.app.domain.model.ReadingPlanProgress
import com.versereminder.app.ui.theme.FigmaCreamVerse
import com.versereminder.app.ui.theme.FigmaDarkBorder
import com.versereminder.app.ui.theme.FigmaDarkBorderGold
import com.versereminder.app.ui.theme.FigmaDarkCardAlt
import com.versereminder.app.ui.theme.FigmaDarkSurface
import com.versereminder.app.ui.theme.FigmaGold
import com.versereminder.app.ui.theme.FigmaGoldText
import com.versereminder.app.ui.theme.FigmaMuted
import com.versereminder.app.ui.theme.FigmaSecondary
import com.versereminder.app.ui.theme.FigmaWhiteText

/**
 * Banner Rencana Bacaan Harian di Layar Beranda
 */
@Composable
fun ReadingPlanHomeBanner(
    activePlan: ReadingPlan,
    progress: ReadingPlanProgress?,
    onOpenPlanDetail: () -> Unit,
    onOpenPlanSelector: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = progress?.completedDays?.size ?: 0
    val totalDays = activePlan.totalDays
    val percent = if (totalDays > 0) completedCount.toFloat() / totalDays.toFloat() else 0f
    val currentDay = progress?.currentDay ?: 1
    val isAllCompleted = completedCount >= totalDays

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = FigmaDarkSurface),
        border = BorderStroke(1.dp, FigmaDarkBorderGold)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Row: Emoji + Title + Selector Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(FigmaDarkCardAlt),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = activePlan.iconEmoji, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "RENCANA BACAAN HARIAN",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = FigmaGold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = activePlan.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FigmaWhiteText,
                            maxLines = 1
                        )
                    }
                }

                TextButton(
                    onClick = onOpenPlanSelector,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Ganti",
                        fontSize = 12.sp,
                        color = FigmaGoldText,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Subtitle / Description
            Text(
                text = activePlan.subtitle,
                fontSize = 13.sp,
                color = FigmaSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Progress Bar & Percentage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isAllCompleted) "🏆 Selesai Sepenuhnya!" else "Hari ke-$currentDay dari $totalDays",
                    fontSize = 12.sp,
                    color = if (isAllCompleted) FigmaGold else FigmaCreamVerse,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${(percent * 100).toInt()}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = FigmaGold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { percent },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = FigmaGold,
                trackColor = FigmaDarkCardAlt
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Button: "Lanjutkan Bacaan"
            Button(
                onClick = onOpenPlanDetail,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FigmaGold)
            ) {
                @Suppress("DEPRECATION")
                Icon(
                    imageVector = Icons.Filled.MenuBook,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isAllCompleted) "Buka Renungan" else "Lanjutkan Hari ke-$currentDay",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

/**
 * Dialog Pemilihan Paket Rencana Bacaan
 */
@Composable
fun ReadingPlanSelectionDialog(
    plans: List<ReadingPlan>,
    progressMap: Map<String, ReadingPlanProgress>,
    activePlanId: String,
    onSelectPlan: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp)),
            color = FigmaDarkSurface,
            border = BorderStroke(1.dp, FigmaDarkBorderGold)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "PILIH RENCANA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = FigmaGold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Rencana Bacaan Harian",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = FigmaWhiteText,
                            fontFamily = FontFamily.Serif
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Tutup",
                            tint = FigmaMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    plans.forEach { plan ->
                        val isSelected = plan.id == activePlanId
                        val progress = progressMap[plan.id]
                        val completedCount = progress?.completedDays?.size ?: 0
                        val percent = (completedCount.toFloat() / plan.totalDays.toFloat()).coerceIn(0f, 1f)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectPlan(plan.id)
                                    onDismiss()
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) FigmaDarkCardAlt else Color(0xFF131722)
                            ),
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) FigmaGold else FigmaDarkBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) FigmaGold.copy(alpha = 0.2f) else FigmaDarkSurface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = plan.iconEmoji, fontSize = 22.sp)
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = plan.title,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) FigmaGold else FigmaWhiteText
                                        )
                                        if (progress?.isCompleted == true) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(
                                                imageVector = Icons.Filled.CheckCircle,
                                                contentDescription = "Tamat",
                                                tint = FigmaGold,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = plan.subtitle,
                                        fontSize = 12.sp,
                                        color = FigmaSecondary,
                                        lineHeight = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        LinearProgressIndicator(
                                            progress = { percent },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(4.dp)
                                                .clip(RoundedCornerShape(2.dp)),
                                            color = FigmaGold,
                                            trackColor = FigmaDarkSurface
                                        )
                                        Text(
                                            text = "$completedCount/${plan.totalDays}",
                                            fontSize = 11.sp,
                                            color = FigmaMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Modal Detail Renungan Harian (Teks Ayat + TTS + Renungan + Doa + Tombol Selesai)
 */
@Composable
fun ReadingPlanDetailDialog(
    plan: ReadingPlan,
    progress: ReadingPlanProgress?,
    selectedDayNumber: Int,
    bibleVersion: BibleVersion,
    fontSizeScale: Float,
    fontType: AppFontType,
    onSelectDay: (Int) -> Unit,
    onToggleCompletion: (Int) -> Unit,
    onResetProgress: () -> Unit,
    onDismiss: () -> Unit
) {
    val day = plan.days.find { it.dayNumber == selectedDayNumber } ?: plan.days.first()
    val isCompleted = progress?.isDayCompleted(day.dayNumber) == true

    val context = LocalContext.current
    val app = context.applicationContext as? VerseReminderApp
    val isPlaying by app?.ttsManager?.isPlaying?.collectAsState() ?: remember { mutableStateOf(false) }
    val playingVerseId by app?.ttsManager?.playingVerseId?.collectAsState() ?: remember { mutableStateOf(null) }
    val thisDayAudioId = -1000L - day.dayNumber
    val isCurrentAudioPlaying = isPlaying && playingVerseId == thisDayAudioId

    val verseText = if (bibleVersion.isEnglish) day.verseTextEn else day.verseTextId

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxSize(0.92f)
                .clip(RoundedCornerShape(24.dp)),
            color = FigmaDarkSurface,
            border = BorderStroke(1.dp, FigmaDarkBorderGold)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header: Plan Title + Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = plan.iconEmoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = plan.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = FigmaGold,
                                maxLines = 1
                            )
                            Text(
                                text = "Hari ke-${day.dayNumber} dari ${plan.totalDays}",
                                fontSize = 12.sp,
                                color = FigmaSecondary
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Tutup",
                            tint = FigmaMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Horizontal Day Selector Pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    plan.days.forEach { d ->
                        val isDayActive = d.dayNumber == selectedDayNumber
                        val isDayDone = progress?.isDayCompleted(d.dayNumber) == true

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onSelectDay(d.dayNumber) },
                            color = when {
                                isDayActive -> FigmaGold
                                isDayDone -> FigmaDarkBorderGold
                                else -> FigmaDarkCardAlt
                            },
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (isDayActive) FigmaGold else FigmaDarkBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (isDayDone) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = if (isDayActive) Color.Black else FigmaGold,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                                Text(
                                    text = "Hari ${d.dayNumber}",
                                    fontSize = 12.sp,
                                    fontWeight = if (isDayActive) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isDayActive) Color.Black else FigmaCreamVerse
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable Content Area: Title + Verse Box + Devotional + Prayer
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Title of the day
                    Text(
                        text = day.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = FigmaWhiteText,
                        fontFamily = FontFamily.Serif
                    )

                    // Scripture Verse Card (With TTS Player)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FigmaDarkCardAlt),
                        border = BorderStroke(1.dp, FigmaDarkBorderGold)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = day.verseReference,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FigmaGold
                                )

                                IconButton(
                                    onClick = {
                                        val tempVerse = com.versereminder.app.domain.model.Verse(
                                            id = thisDayAudioId,
                                            book = day.verseReference.substringBefore(" "),
                                            chapter = 1,
                                            verse = 1,
                                            reference = day.verseReference,
                                            textId = day.verseTextId,
                                            textEn = day.verseTextEn,
                                            category = plan.category,
                                            themeTag = plan.title
                                        )
                                        app?.ttsManager?.toggleSpeak(tempVerse, bibleVersion)
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isCurrentAudioPlaying) Icons.Filled.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Audio Ayat",
                                        tint = if (isCurrentAudioPlaying) FigmaGold else FigmaMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "“$verseText”",
                                fontSize = (14 * fontSizeScale).sp,
                                fontFamily = fontType.fontFamily,
                                color = FigmaCreamVerse,
                                lineHeight = (22 * fontSizeScale).sp
                            )
                        }
                    }

                    // Devotional Section
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "💡 RENUNGAN HARI INI",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = FigmaGold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = day.devotionalText,
                            fontSize = 13.5.sp,
                            color = FigmaWhiteText,
                            lineHeight = 21.sp
                        )
                    }

                    // Prayer Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131722)),
                        border = BorderStroke(1.dp, FigmaDarkBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "🙏 DOA HARI INI",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FigmaGold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = day.prayerText,
                                fontSize = 13.sp,
                                color = FigmaCreamVerse,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Navigation Row: Previous Day + Toggle Completion + Next Day
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (day.dayNumber > 1) {
                        OutlinedButton(
                            onClick = { onSelectDay(day.dayNumber - 1) },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, FigmaDarkBorder),
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Sebelumnya",
                                tint = FigmaMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Button(
                        onClick = { onToggleCompletion(day.dayNumber) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCompleted) FigmaDarkCardAlt else FigmaGold
                        ),
                        border = if (isCompleted) BorderStroke(1.dp, FigmaGold) else null
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Filled.Check,
                            contentDescription = null,
                            tint = if (isCompleted) FigmaGold else Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isCompleted) "Selesai Dibaca ✓" else "Tandai Selesai",
                            color = if (isCompleted) FigmaGold else Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    if (day.dayNumber < plan.totalDays) {
                        OutlinedButton(
                            onClick = { onSelectDay(day.dayNumber + 1) },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, FigmaDarkBorder),
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Berikutnya",
                                tint = FigmaMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
