package com.versereminder.app.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.versereminder.app.VerseReminderApp
import com.versereminder.app.media.VerseImageGenerator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.ThemeMode
import com.versereminder.app.domain.model.VerseCategory
import com.versereminder.app.ui.components.VerseCard
import com.versereminder.app.ui.components.VerseCardShimmer
import com.versereminder.app.ui.theme.FigmaCreamVerse
import com.versereminder.app.ui.theme.FigmaDarkBorder
import com.versereminder.app.ui.theme.FigmaDarkBorderGold
import com.versereminder.app.ui.theme.FigmaDarkCardAlt
import com.versereminder.app.ui.theme.FigmaDarkSurface
import com.versereminder.app.ui.theme.FigmaGold
import com.versereminder.app.ui.theme.FigmaGoldText
import com.versereminder.app.ui.theme.FigmaMuted
import com.versereminder.app.ui.theme.FigmaSecondary
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import com.versereminder.app.ui.readingplan.ReadingPlanDetailDialog
import com.versereminder.app.ui.readingplan.ReadingPlanHomeBanner
import com.versereminder.app.ui.readingplan.ReadingPlanSelectionDialog
import com.versereminder.app.ui.readingplan.ReadingPlanViewModel
import com.versereminder.app.ui.theme.FigmaWhiteText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToSchedule: () -> Unit,
    readingPlanViewModel: ReadingPlanViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = ReadingPlanViewModel.Factory(
            readingPlanRepository = (LocalContext.current.applicationContext as VerseReminderApp).readingPlanRepository,
            settingsRepository = (LocalContext.current.applicationContext as VerseReminderApp).settingsRepository
        )
    )
) {
    val dailyVerse by viewModel.dailyVerse.collectAsState()
    val bibleVersion by viewModel.bibleVersion.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categoryVerses by viewModel.categoryVerses.collectAsState()
    val fontSizeScale by viewModel.fontSizeScale.collectAsState()
    val fontType by viewModel.fontType.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    val context = LocalContext.current
    val app = context.applicationContext as? VerseReminderApp
    val isPlaying by app?.ttsManager?.isPlaying?.collectAsState() ?: remember { mutableStateOf(false) }
    val playingVerseId by app?.ttsManager?.playingVerseId?.collectAsState() ?: remember { mutableStateOf(null) }
    var showDailyShareDialog by remember { mutableStateOf(false) }

    // Reading Plan states
    val plans = readingPlanViewModel.allPlans
    val progressMap by readingPlanViewModel.progressMap.collectAsState()
    val selectedPlanId by readingPlanViewModel.selectedPlanId.collectAsState()
    val selectedDayNumber by readingPlanViewModel.selectedDay.collectAsState()
    val activePlan = plans.find { it.id == selectedPlanId } ?: plans.first()
    val activeProgress = progressMap[activePlan.id]
    var showPlanSelectorDialog by remember { mutableStateOf(false) }
    var showPlanDetailDialog by remember { mutableStateOf(false) }

    // Dynamic Date & Greeting matching Figma ("Kamis • 1 Oktober", "Selamat pagi, Maria")
    val todayDateFormatted = SimpleDateFormat("EEEE • d MMMM", Locale("id", "ID")).format(Date())
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greetingGreeting = when (currentHour) {
        in 4..11 -> "Selamat pagi"
        in 12..14 -> "Selamat siang"
        in 15..18 -> "Selamat sore"
        else -> "Selamat malam"
    }

    val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

    // 360-degree rotation animation for refresh
    var refreshRotation by remember { mutableFloatStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = refreshRotation,
        animationSpec = tween(durationMillis = 650, easing = LinearOutSlowInEasing),
        label = "refresh_rotation"
    )

    // Bible translation dropdown state on the home screen
    var showVersionMenu by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // FIGMA HEADER: Atmospheric Eyebrow + Noto Serif Title + Action Buttons (Theme Toggle & Bell)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = todayDateFormatted,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            ),
                            color = FigmaGold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$greetingGreeting, Teman",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp,
                            color = FigmaWhiteText
                        )
                    }

                    // Header Action Buttons: Day/Night Toggle & Bell
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // 1. Day / Night Quick Toggle Button
                        Surface(
                            onClick = { viewModel.toggleThemeMode() },
                            shape = CircleShape,
                            color = FigmaDarkSurface,
                            border = BorderStroke(1.dp, FigmaDarkBorder),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (themeMode == ThemeMode.DARK) Icons.Default.WbSunny else Icons.Default.DarkMode,
                                    contentDescription = "Ganti Tema",
                                    tint = FigmaGoldText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // 2. Bell Notification Action Button
                        Surface(
                            onClick = onNavigateToSchedule,
                            shape = CircleShape,
                            color = FigmaDarkSurface,
                            border = BorderStroke(1.dp, FigmaDarkBorder),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Pengingat",
                                    tint = FigmaSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // SEARCH BAR COMPONENT
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Cari firman, tema, atau kitab...",
                            color = FigmaMuted,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Cari",
                            tint = FigmaGold,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.clearSearch() }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Hapus",
                                    tint = FigmaMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FigmaDarkSurface,
                        unfocusedContainerColor = FigmaDarkSurface,
                        focusedBorderColor = FigmaGold,
                        unfocusedBorderColor = FigmaDarkBorder,
                        focusedTextColor = FigmaWhiteText,
                        unfocusedTextColor = FigmaWhiteText,
                        cursorColor = FigmaGold
                    ),
                    singleLine = true
                )
            }

            if (searchQuery.isNotBlank()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "HASIL PENCARIAN FIRMAN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = FigmaGold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${searchResults.size} ayat ditemukan",
                            fontSize = 12.sp,
                            color = FigmaSecondary
                        )
                    }
                }

                if (searchResults.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = FigmaDarkSurface),
                            border = BorderStroke(1.dp, FigmaDarkBorder)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "🕊️", fontSize = 32.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tidak ada ayat yang cocok",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FigmaWhiteText
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Coba gunakan kata kunci lain seperti 'damai', 'kasih', atau nama kitab.",
                                    fontSize = 12.sp,
                                    color = FigmaSecondary,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(
                        items = searchResults,
                        key = { it.id }
                    ) { verse ->
                        VerseCard(
                            verse = verse,
                            bibleVersion = bibleVersion,
                            fontSizeScale = fontSizeScale,
                            fontType = fontType,
                            isPlayingAudio = isPlaying && playingVerseId == verse.id,
                            onPlayAudioClick = { app?.ttsManager?.toggleSpeak(verse, bibleVersion) },
                            onDeleteClick = if (verse.isCustom) {
                                { viewModel.deleteCustomVerse(verse.id) }
                            } else null,
                            onBookmarkClick = { viewModel.toggleBookmark(verse.id) }
                        )
                    }
                }
            } else {
                // SECTION 1: DAILY VERSE (Figma: "Ayat hari ini" | "TB • 06:00")
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                        Text(
                            text = "Ayat hari ini",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp),
                            color = FigmaCreamVerse,
                            fontWeight = FontWeight.Medium
                        )

                        // Interactive Translation Switcher Pill (e.g. "TB • 06:00")
                        Box {
                            Surface(
                                onClick = { showVersionMenu = true },
                                shape = RoundedCornerShape(8.dp),
                                color = FigmaDarkSurface,
                                border = BorderStroke(1.dp, FigmaDarkBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${bibleVersion.code} • 06:00 ▾",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = FigmaGoldText,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Dropdown Menu to change Bible translation directly
                            DropdownMenu(
                                expanded = showVersionMenu,
                                onDismissRequest = { showVersionMenu = false },
                                modifier = Modifier.background(FigmaDarkSurface)
                            ) {
                                BibleVersion.entries.forEach { version ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "${version.code} — ${version.title}",
                                                color = if (bibleVersion == version) FigmaGold else FigmaWhiteText,
                                                fontWeight = if (bibleVersion == version) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 12.sp
                                            )
                                        },
                                        onClick = {
                                            viewModel.setBibleVersion(version)
                                            showVersionMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Figma Daily Verse Card
                    if (dailyVerse != null) {
                        val verse = dailyVerse!!
                        val verseText = verse.getTextForVersion(bibleVersion)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = FigmaDarkSurface),
                            border = BorderStroke(1.2.dp, FigmaDarkBorderGold)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                // Verse Metadata: Theme Badge + Day count
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = FigmaGold.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, FigmaGold.copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = verse.themeTag.uppercase(),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = FigmaGoldText,
                                            letterSpacing = 0.6.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }

                                    Text(
                                        text = "Hari $dayOfYear",
                                        fontSize = 10.sp,
                                        color = FigmaMuted
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Verse Copy in Noto Serif
                                Text(
                                    text = "«${verseText.trim('«', '»', '"')}»",
                                    fontFamily = fontType.fontFamily,
                                    fontSize = (21 * fontSizeScale).sp,
                                    lineHeight = (30 * fontSizeScale).sp,
                                    color = FigmaCreamVerse
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Reference in Gold
                                Text(
                                    text = verse.reference.uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = FigmaGoldText,
                                    letterSpacing = 0.5.sp
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                // Verse Actions: "Ayat lain" (Refresh) on Left, Bookmark Gold Button on Right
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Left: Ayat lain (Shuffle)
                                    Surface(
                                        onClick = {
                                            refreshRotation += 360f
                                            viewModel.refreshDailyVerse()
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color.Transparent,
                                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Refresh,
                                                contentDescription = "Acak",
                                                tint = FigmaSecondary,
                                                modifier = Modifier
                                                    .size(16.dp)
                                                    .rotate(animatedRotation)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Ayat lain",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = FigmaSecondary
                                            )
                                        }
                                    }

                                    // Right Actions: Share, Copy, and Gold Bookmark Button
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = {
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                val clip = ClipData.newPlainText("Ayat Alkitab", "📖 ${verse.reference}\n\n\"$verseText\"")
                                                clipboard.setPrimaryClip(clip)
                                                Toast.makeText(context, "Ayat disalin", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.size(34.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "Salin",
                                                tint = FigmaSecondary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }

                                        // Audio Text-to-Speech (TTS) Button
                                        val isThisPlaying = isPlaying && playingVerseId == verse.id
                                        IconButton(
                                            onClick = {
                                                app?.ttsManager?.toggleSpeak(verse, bibleVersion)
                                            },
                                            modifier = Modifier.size(34.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isThisPlaying) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                                contentDescription = if (isThisPlaying) "Hentikan audio" else "Dengarkan audio",
                                                tint = if (isThisPlaying) FigmaGold else FigmaSecondary,
                                                modifier = Modifier.size(17.dp)
                                            )
                                        }

                                        // Share Button (Opens Visual & Text Share Dialog)
                                        IconButton(
                                            onClick = {
                                                showDailyShareDialog = true
                                            },
                                            modifier = Modifier.size(34.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Share,
                                                contentDescription = "Bagikan",
                                                tint = FigmaSecondary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }

                                        // Gold Bookmark Action Button
                                        Surface(
                                            onClick = { viewModel.toggleBookmark(verse.id) },
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (verse.isBookmarked) FigmaGold else FigmaDarkCardAlt,
                                            border = BorderStroke(1.dp, if (verse.isBookmarked) FigmaGold else FigmaDarkBorder),
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = if (verse.isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                                    contentDescription = "Simpan",
                                                    tint = if (verse.isBookmarked) Color(0xFF090B10) else FigmaGoldText,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        VerseCardShimmer()
                    }
                }
            }

            // SECTION 1.5: RENCANA BACAAN HARIAN
            item {
                ReadingPlanHomeBanner(
                    activePlan = activePlan,
                    progress = activeProgress,
                    onOpenPlanDetail = { showPlanDetailDialog = true },
                    onOpenPlanSelector = { showPlanSelectorDialog = true }
                )
            }

            // SECTION 2: TEMA RENUNGAN (Figma: "Tema renungan" -> "Semua", "Pengharapan", "Damai", "Kekuatan")
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Tema renungan",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp),
                        color = FigmaCreamVerse,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            VerseCategory.ALL to "Semua",
                            VerseCategory.HOPE to "Pengharapan",
                            VerseCategory.PEACE to "Damai",
                            VerseCategory.STRENGTH to "Kekuatan",
                            VerseCategory.LOVE to "Kasih",
                            VerseCategory.FAITH to "Iman",
                            VerseCategory.PRAYER to "Doa",
                            VerseCategory.WISDOM to "Hikmat"
                        ).forEach { (category, label) ->
                            val isSelected = selectedCategory == category
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.selectCategory(category) },
                                label = {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = FigmaGold,
                                    selectedLabelColor = Color(0xFF090B10),
                                    containerColor = FigmaDarkSurface,
                                    labelColor = FigmaSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) FigmaGold else FigmaDarkBorder
                                )
                            )
                        }
                    }
                }
            }

            // SECTION 3: WIDGET PREVIEW (Figma: "Widget layar utama" | "Pratinjau interaktif")
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Widget layar utama",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp),
                            color = FigmaCreamVerse,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Pratinjau interaktif",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = FigmaMuted
                        )
                    }

                    // Interactive Widget Preview Card matching Figma
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF101522)),
                        border = BorderStroke(1.dp, FigmaDarkBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Header: Verse Reminder • Time
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Verse Reminder",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FigmaGold
                                )
                                Text(
                                    text = "06:42",
                                    fontSize = 10.sp,
                                    color = FigmaMuted
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "«TUHAN adalah kekuatanku dan perisaiku.»",
                                fontFamily = FontFamily.Serif,
                                fontSize = 14.sp,
                                color = FigmaCreamVerse
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "MAZMUR 28:7",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = FigmaGoldText
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Widget Actions: "Ganti" & "Simpan"
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    onClick = { Toast.makeText(context, "Mengacak ayat widget...", Toast.LENGTH_SHORT).show() },
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF202636),
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Shuffle,
                                            contentDescription = null,
                                            tint = FigmaSecondary,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Ganti", fontSize = 10.sp, color = FigmaSecondary)
                                    }
                                }

                                Surface(
                                    onClick = { Toast.makeText(context, "Ayat disimpan ke koleksi", Toast.LENGTH_SHORT).show() },
                                    shape = RoundedCornerShape(8.dp),
                                    color = FigmaGold
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Simpan", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF090B10))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 4: BROWSE VERSES LIST (Available at all times)
            if (categoryVerses.isNotEmpty()) {
                item {
                    Text(
                        text = if (selectedCategory == VerseCategory.ALL) "Daftar Ayat Inspiratif" else "Ayat Kategori: ${selectedCategory.displayName}",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp),
                        color = FigmaCreamVerse,
                        fontWeight = FontWeight.Medium
                    )
                }
                items(
                    items = categoryVerses,
                    key = { it.id }
                ) { verse ->
                    VerseCard(
                        verse = verse,
                        bibleVersion = bibleVersion,
                        fontSizeScale = fontSizeScale,
                        fontType = fontType,
                        isPlayingAudio = isPlaying && playingVerseId == verse.id,
                        onPlayAudioClick = { app?.ttsManager?.toggleSpeak(verse, bibleVersion) },
                        onDeleteClick = if (verse.isCustom) {
                            { viewModel.deleteCustomVerse(verse.id) }
                        } else null,
                        onBookmarkClick = { viewModel.toggleBookmark(verse.id) }
                    )
                }
            }
        }
    }
}

    // Modal Dialog: Bagikan Firman Hari Ini (Story / Post Image & Text)
    if (showDailyShareDialog && dailyVerse != null) {
        val verse = dailyVerse!!
        val verseText = verse.getTextForVersion(bibleVersion)
        AlertDialog(
            onDismissRequest = { showDailyShareDialog = false },
            containerColor = FigmaDarkSurface,
            title = {
                Column {
                    Text(
                        text = "Bagikan Firman Hari Ini",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = FigmaCreamVerse
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${verse.reference} (${bibleVersion.code})",
                        fontSize = 12.sp,
                        color = FigmaGold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Option 1: WhatsApp / IG Story (9:16)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            showDailyShareDialog = false
                            VerseImageGenerator.shareVerseCard(context, verse, bibleVersion, isStoryFormat = true)
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = FigmaDarkCardAlt),
                        border = BorderStroke(1.dp, FigmaGold.copy(alpha = 0.35f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = FigmaGold.copy(alpha = 0.18f),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = null,
                                        tint = FigmaGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Gambar Cerita (Story 9:16)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = FigmaCreamVerse
                                )
                                Text(
                                    text = "Format vertikal pas untuk WhatsApp & IG Story",
                                    fontSize = 10.sp,
                                    color = FigmaMuted
                                )
                            }
                        }
                    }

                    // Option 2: Instagram Feed / Chat (1:1)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            showDailyShareDialog = false
                            VerseImageGenerator.shareVerseCard(context, verse, bibleVersion, isStoryFormat = false)
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = FigmaDarkCardAlt),
                        border = BorderStroke(1.dp, FigmaGold.copy(alpha = 0.35f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = FigmaGold.copy(alpha = 0.18f),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.CropSquare,
                                        contentDescription = null,
                                        tint = FigmaGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Gambar Persegi (Feed 1:1)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = FigmaCreamVerse
                                )
                                Text(
                                    text = "Format persegi untuk postingan atau chat",
                                    fontSize = 10.sp,
                                    color = FigmaMuted
                                )
                            }
                        }
                    }

                    // Option 3: Plain text share
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            showDailyShareDialog = false
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Ayat Alkitab: ${verse.reference}")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "📖 ${verse.reference} (${bibleVersion.code})\n\n\"$verseText\"\n\n🌿 Bersyukur untuk hari ini — Dikirim melalui Verse Reminder"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Bagikan Firman Tuhan"))
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = FigmaDarkCardAlt),
                        border = BorderStroke(1.dp, FigmaGold.copy(alpha = 0.35f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = FigmaGold.copy(alpha = 0.18f),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = null,
                                        tint = FigmaGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Bagikan Teks Firman",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = FigmaCreamVerse
                                )
                                Text(
                                    text = "Kirim kutipan ayat dan referensi dalam bentuk teks",
                                    fontSize = 10.sp,
                                    color = FigmaMuted
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showDailyShareDialog = false }) {
                    Text("Tutup", color = FigmaSecondary)
                }
            }
        )
    }

    if (showPlanSelectorDialog) {
        ReadingPlanSelectionDialog(
            plans = plans,
            progressMap = progressMap,
            activePlanId = activePlan.id,
            onSelectPlan = { planId ->
                readingPlanViewModel.selectPlan(planId)
                showPlanSelectorDialog = false
                showPlanDetailDialog = true
            },
            onDismiss = { showPlanSelectorDialog = false }
        )
    }

    if (showPlanDetailDialog) {
        ReadingPlanDetailDialog(
            plan = activePlan,
            progress = activeProgress,
            selectedDayNumber = selectedDayNumber,
            bibleVersion = bibleVersion,
            fontSizeScale = fontSizeScale,
            fontType = fontType,
            onSelectDay = { dayNum -> readingPlanViewModel.selectDay(dayNum) },
            onToggleCompletion = { dayNum -> readingPlanViewModel.toggleDayCompletion(activePlan.id, dayNum) },
            onResetProgress = { readingPlanViewModel.resetPlan(activePlan.id) },
            onDismiss = { showPlanDetailDialog = false }
        )
    }
}
