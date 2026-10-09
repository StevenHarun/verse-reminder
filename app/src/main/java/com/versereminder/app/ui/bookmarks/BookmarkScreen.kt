package com.versereminder.app.ui.bookmarks

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import com.versereminder.app.ui.components.SmoothScrollToTopButton
import com.versereminder.app.ui.components.smoothScrollbar
import com.versereminder.app.ui.components.verticalFadingEdges
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.versereminder.app.VerseReminderApp
import com.versereminder.app.domain.model.Verse
import com.versereminder.app.domain.model.VerseCategory
import com.versereminder.app.ui.components.VerseCard
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel
) {
    val bookmarks by viewModel.bookmarkedVerses.collectAsState()
    val bibleVersion by viewModel.bibleVersion.collectAsState()
    val fontSizeScale by viewModel.fontSizeScale.collectAsState()
    val fontType by viewModel.fontType.collectAsState()

    val context = LocalContext.current
    val app = context.applicationContext as? VerseReminderApp
    val isPlaying by app?.ttsManager?.isPlaying?.collectAsState() ?: remember { mutableStateOf(false) }
    val playingVerseId by app?.ttsManager?.playingVerseId?.collectAsState() ?: remember { mutableStateOf(null) }

    var searchQuery by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var deletingVerse by remember { mutableStateOf<Verse?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf<VerseCategory?>(null) }
    var filterOnlyCustom by remember { mutableStateOf(false) }

    val filteredBookmarks = bookmarks.filter { verse ->
        val matchesQuery = searchQuery.isBlank() ||
                verse.reference.contains(searchQuery, ignoreCase = true) ||
                verse.textId.contains(searchQuery, ignoreCase = true) ||
                verse.textEn.contains(searchQuery, ignoreCase = true)

        val matchesCategory = selectedCategoryFilter == null || verse.category == selectedCategoryFilter
        val matchesCustom = !filterOnlyCustom || verse.isCustom

        matchesQuery && matchesCategory && matchesCustom
    }

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
            // Figma Header: Eyebrow + Noto Serif Title + Add Custom Verse & Search Action Buttons
            item(key = "bookmark_header", contentType = "header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Koleksi pribadi",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            ),
                            color = FigmaGold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Ayat tersimpan",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp,
                            color = FigmaWhiteText
                        )
                    }

                    // Action Buttons: + (Add Custom Verse) and Search
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Add Custom Verse Button (+)
                        Surface(
                            onClick = { showAddDialog = true },
                            shape = CircleShape,
                            color = FigmaDarkSurface,
                            border = BorderStroke(1.2.dp, FigmaGold),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Tambah Ayat Pribadi",
                                    tint = FigmaGold,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        // Search Button
                        Surface(
                            onClick = { showSearchBar = !showSearchBar },
                            shape = CircleShape,
                            color = FigmaDarkSurface,
                            border = BorderStroke(1.dp, FigmaDarkBorder),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Cari",
                                    tint = if (showSearchBar) FigmaGold else FigmaSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Search Bar if expanded
            if (showSearchBar) {
                item(key = "bookmark_search_bar", contentType = "search_bar") {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari ayat tersimpan...", color = FigmaMuted) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // SECTION 1: COLLECTION SUMMARY CARD (Figma: "[Count] ayat" | "Tersedia offline" | "Tersinkron")
            item(key = "bookmark_summary_card", contentType = "summary_card") {
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
                        Column {
                            Text(
                                text = "${bookmarks.size} ayat",
                                fontFamily = FontFamily.Serif,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = FigmaCreamVerse
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Tersedia untuk dibaca offline",
                                style = MaterialTheme.typography.bodySmall,
                                color = FigmaSecondary
                            )
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
                                    imageVector = Icons.Default.CloudDone,
                                    contentDescription = null,
                                    tint = FigmaGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
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

            // SECTION 2: CATEGORY FILTER CHIPS
            item(key = "bookmark_filter_chips", contentType = "filter_chips") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Filter Kategori",
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
                        val isAllSelected = selectedCategoryFilter == null && !filterOnlyCustom
                        FilterChip(
                            selected = isAllSelected,
                            onClick = {
                                selectedCategoryFilter = null
                                filterOnlyCustom = false
                            },
                            label = { Text("Semua", fontSize = 11.sp, fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Normal) },
                            shape = RoundedCornerShape(10.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FigmaGold,
                                selectedLabelColor = Color(0xFF090B10),
                                containerColor = FigmaDarkSurface,
                                labelColor = FigmaSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isAllSelected,
                                borderColor = if (isAllSelected) FigmaGold else FigmaDarkBorder
                            )
                        )

                        // Filter: Ayat Pribadi
                        FilterChip(
                            selected = filterOnlyCustom,
                            onClick = {
                                filterOnlyCustom = !filterOnlyCustom
                                if (filterOnlyCustom) {
                                    selectedCategoryFilter = null
                                }
                            },
                            label = { Text("✍️ Ayat Pribadi", fontSize = 11.sp, fontWeight = if (filterOnlyCustom) FontWeight.Bold else FontWeight.Normal) },
                            shape = RoundedCornerShape(10.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FigmaGold,
                                selectedLabelColor = Color(0xFF090B10),
                                containerColor = FigmaDarkSurface,
                                labelColor = FigmaSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = filterOnlyCustom,
                                borderColor = if (filterOnlyCustom) FigmaGold else FigmaDarkBorder
                            )
                        )

                        VerseCategory.entries.forEach { category ->
                            val isSelected = selectedCategoryFilter == category && !filterOnlyCustom
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    filterOnlyCustom = false
                                    selectedCategoryFilter = if (isSelected) null else category
                                },
                                label = {
                                    Text(
                                        text = "${category.icon} ${category.displayName}",
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

            // SECTION 3: SAVED VERSES LIST
            if (filteredBookmarks.isEmpty()) {
                item(key = "bookmark_empty_state", contentType = "empty_state") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val emptyText = when {
                            searchQuery.isNotBlank() -> "Tidak ada ayat yang cocok dengan kata kunci."
                            filterOnlyCustom -> "Belum ada ayat pribadi yang Anda tambahkan.\nTekan tombol '+' di kanan atas untuk menulis ayat Anda sendiri."
                            else -> "Belum ada ayat tersimpan.\nTekan tombol '+' di atas untuk menambah ayat sendiri atau simpan dari beranda."
                        }
                        Text(
                            text = emptyText,
                            color = FigmaMuted,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                items(
                    items = filteredBookmarks,
                    key = { it.id },
                    contentType = { "verse_card" }
                ) { verse ->
                    VerseCard(
                        verse = verse,
                        bibleVersion = bibleVersion,
                        fontSizeScale = fontSizeScale,
                        fontType = fontType,
                        isPlayingAudio = isPlaying && playingVerseId == verse.id,
                        onPlayAudioClick = { app?.ttsManager?.toggleSpeak(verse, bibleVersion) },
                        onDeleteClick = if (verse.isCustom) {
                            { deletingVerse = verse }
                        } else null,
                        onBookmarkClick = { viewModel.toggleBookmark(verse.id) }
                    )
                }
            }
        }
        SmoothScrollToTopButton(listState = listState)
    }
}

    // DIALOG: ADD CUSTOM VERSE
    if (showAddDialog) {
        AddCustomVerseDialog(
            onDismiss = { showAddDialog = false },
            onSave = { ref, textId, textEn, category ->
                viewModel.addCustomVerse(
                    reference = ref,
                    textId = textId,
                    textEn = textEn,
                    category = category
                )
                showAddDialog = false
            }
        )
    }

    // DIALOG: CONFIRM DELETE CUSTOM VERSE
    if (deletingVerse != null) {
        AlertDialog(
            onDismissRequest = { deletingVerse = null },
            containerColor = FigmaDarkSurface,
            title = {
                Text(
                    text = "Hapus Ayat Pribadi?",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = FigmaCreamVerse
                )
            },
            text = {
                Text(
                    text = "Ayat \"${deletingVerse!!.reference}\" akan dihapus secara permanen dari koleksi dan database Anda.",
                    color = FigmaSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCustomVerse(deletingVerse!!.id)
                        deletingVerse = null
                    }
                ) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingVerse = null }) {
                    Text("Batal", color = FigmaSecondary)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddCustomVerseDialog(
    onDismiss: () -> Unit,
    onSave: (reference: String, textId: String, textEn: String, category: VerseCategory) -> Unit
) {
    var reference by remember { mutableStateOf("") }
    var textId by remember { mutableStateOf("") }
    var textEn by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(VerseCategory.HOPE) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = FigmaDarkSurface,
        title = {
            Column {
                Text(
                    text = "Tambah Ayat Pribadi",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = FigmaCreamVerse
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Simpan firman favorit Anda ke koleksi pribadi",
                    fontSize = 12.sp,
                    color = FigmaSecondary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Reference Field
                OutlinedTextField(
                    value = reference,
                    onValueChange = {
                        reference = it
                        errorMessage = null
                    },
                    label = { Text("Referensi Kitab") },
                    placeholder = { Text("Contoh: Yohanes 3:16 atau Mazmur 91:1-2") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // 2. Verse Text Field (ID)
                OutlinedTextField(
                    value = textId,
                    onValueChange = {
                        textId = it
                        errorMessage = null
                    },
                    label = { Text("Isi Ayat (Bahasa Indonesia)") },
                    placeholder = { Text("Ketik atau tempel firman...") },
                    minLines = 3,
                    maxLines = 6,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // 3. Category Selector Chips
                Text(
                    text = "Kategori Renungan:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FigmaGoldText
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        VerseCategory.HOPE to "Pengharapan",
                        VerseCategory.PEACE to "Damai",
                        VerseCategory.STRENGTH to "Kekuatan",
                        VerseCategory.LOVE to "Kasih",
                        VerseCategory.FAITH to "Iman",
                        VerseCategory.PRAYER to "Doa",
                        VerseCategory.WISDOM to "Hikmat"
                    ).forEach { (cat, label) ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = { Text(label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            shape = RoundedCornerShape(8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FigmaGold,
                                selectedLabelColor = Color(0xFF090B10),
                                containerColor = FigmaDarkCardAlt,
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

                // 4. Optional English Text
                OutlinedTextField(
                    value = textEn,
                    onValueChange = { textEn = it },
                    label = { Text("Teks Inggris (Opsional)") },
                    placeholder = { Text("Optional English translation...") },
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (reference.isBlank()) {
                        errorMessage = "Referensi nats Alkitab wajib diisi"
                        return@Button
                    }
                    if (textId.isBlank()) {
                        errorMessage = "Isi ayat wajib diisi"
                        return@Button
                    }
                    onSave(reference.trim(), textId.trim(), textEn.trim(), selectedCategory)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FigmaGold,
                    contentColor = Color(0xFF090B10)
                )
            ) {
                Text("Simpan Ayat", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = FigmaSecondary)
            }
        }
    )
}
