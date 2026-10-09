package com.versereminder.app.ui.history

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import com.versereminder.app.ui.components.SmoothScrollToTopButton
import com.versereminder.app.ui.components.smoothScrollbar
import com.versereminder.app.ui.components.verticalFadingEdges
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.versereminder.app.data.local.dao.HistoryWithVerse
import com.versereminder.app.domain.model.AppFontType
import com.versereminder.app.domain.model.BibleVersion
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel
) {
    val historyItems by viewModel.historyList.collectAsState()
    val bibleVersion by viewModel.bibleVersion.collectAsState()
    val fontSizeScale by viewModel.fontSizeScale.collectAsState()
    val fontType by viewModel.fontType.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalFadingEdges(topFadeHeight = 16.dp, bottomFadeHeight = 24.dp)
                    .smoothScrollbar(state = listState),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
            // Figma Header: Eyebrow + Noto Serif Title + Action Button
            item(key = "history_header", contentType = "header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Jejak perenungan",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            ),
                            color = FigmaGold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Riwayat bacaan",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp,
                            color = FigmaWhiteText
                        )
                    }

                    if (historyItems.isNotEmpty()) {
                        Surface(
                            onClick = { showClearDialog = true },
                            shape = CircleShape,
                            color = FigmaDarkSurface,
                            border = BorderStroke(1.dp, FigmaDarkBorder),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.DeleteSweep,
                                    contentDescription = "Hapus Riwayat",
                                    tint = FigmaGold,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // SECTION 1: OFFLINE AVAILABILITY SUMMARY CARD (Figma Spec)
            item(key = "history_summary_card", contentType = "summary_card") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = FigmaDarkCardAlt),
                    border = BorderStroke(1.2.dp, FigmaDarkBorderGold)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = FigmaDarkSurface,
                                border = BorderStroke(1.dp, FigmaDarkBorderGold),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.CloudDone,
                                        contentDescription = null,
                                        tint = FigmaGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Riwayat tersedia offline",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FigmaCreamVerse
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (historyItems.isEmpty()) "Siap mencatat ayat perenungan" else "${historyItems.size} bacaan tersimpan rapi",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FigmaSecondary
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = FigmaDarkSurface,
                            border = BorderStroke(1.dp, FigmaDarkBorderGold)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = FigmaGold,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Tersinkron",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = FigmaGoldText
                                )
                            }
                        }
                    }
                }
            }

            // SECTION 2: TIMELINE LIST
            if (historyItems.isEmpty()) {
                item(key = "history_empty_state", contentType = "empty_state") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FigmaDarkSurface),
                        border = BorderStroke(1.dp, FigmaDarkBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(36.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = FigmaGold.copy(alpha = 0.6f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Belum Ada Riwayat Bacaan",
                                fontFamily = FontFamily.Serif,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = FigmaCreamVerse
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Setiap ayat yang ditampilkan dan dibaca akan tersimpan di sini secara berurutan sesuai lini masa.",
                                style = MaterialTheme.typography.bodySmall,
                                color = FigmaMuted,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                item(key = "history_timeline_header", contentType = "timeline_header") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(FigmaGold, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LINI MASA PERENUNGAN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = FigmaGoldText
                        )
                    }
                }

                itemsIndexed(
                    items = historyItems,
                    key = { _, item -> item.historyId },
                    contentType = { _, _ -> "timeline_item" }
                ) { index, item ->
                    FigmaHistoryTimelineItem(
                        item = item,
                        bibleVersion = bibleVersion,
                        fontSizeScale = fontSizeScale,
                        fontType = fontType,
                        isLast = index == historyItems.lastIndex
                    )
                }
            }
        }
        SmoothScrollToTopButton(listState = listState)
    }
}

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            containerColor = FigmaDarkSurface,
            title = {
                Text(
                    text = "Hapus Semua Riwayat?",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = FigmaCreamVerse
                )
            },
            text = {
                Text(
                    text = "Seluruh catatan riwayat ayat yang pernah dibaca akan dihapus dari perangkat.",
                    color = FigmaSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearHistory()
                        showClearDialog = false
                    }
                ) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Batal", color = FigmaSecondary)
                }
            }
        )
    }
}

@Composable
private fun FigmaHistoryTimelineItem(
    item: HistoryWithVerse,
    bibleVersion: BibleVersion,
    fontSizeScale: Float,
    fontType: AppFontType,
    isLast: Boolean
) {
    val context = LocalContext.current
    val verseText = if (bibleVersion.isIndonesian) item.textId else item.textEn
    val dateFormatted = SimpleDateFormat("dd MMM • HH:mm", Locale("id", "ID")).format(Date(item.displayedAt))

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Left timeline track: Node & Line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(24.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(FigmaGold, CircleShape)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f, fill = true)
                        .background(FigmaDarkBorderGold.copy(alpha = 0.6f))
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Right Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = FigmaDarkSurface),
            border = BorderStroke(1.dp, FigmaDarkBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header: Time & Source badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateFormatted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = FigmaMuted
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = FigmaDarkCardAlt,
                        border = BorderStroke(0.8.dp, FigmaDarkBorderGold)
                    ) {
                        Text(
                            text = item.source.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FigmaGoldText,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Reference in gold serif
                Text(
                    text = item.reference.uppercase(),
                    fontFamily = FontFamily.Serif,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = FigmaGoldText
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Verse quote in warm cream
                Text(
                    text = "\"$verseText\"",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = fontType.fontFamily,
                        fontSize = (15 * fontSizeScale).sp,
                        lineHeight = (22 * fontSizeScale).sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    ),
                    color = FigmaCreamVerse
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Actions: Copy & Share
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Ayat Alkitab", "📖 ${item.reference}\n\n\"$verseText\"")
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Ayat disalin", Toast.LENGTH_SHORT).show()
                        },
                        shape = CircleShape,
                        color = FigmaDarkCardAlt,
                        border = BorderStroke(0.8.dp, FigmaDarkBorder),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Salin",
                                tint = FigmaSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Surface(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Ayat Alkitab: ${item.reference}")
                                putExtra(Intent.EXTRA_TEXT, "📖 ${item.reference}\n\n\"$verseText\"\n\n— Dibagikan dari Verse Reminder")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Bagikan"))
                        },
                        shape = CircleShape,
                        color = FigmaDarkCardAlt,
                        border = BorderStroke(0.8.dp, FigmaDarkBorder),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Bagikan",
                                tint = FigmaSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
