package com.versereminder.app.ui.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.versereminder.app.domain.model.AppFontType
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.ThemeMode
import com.versereminder.app.ui.components.VerseCard
import com.versereminder.app.ui.theme.FigmaCreamVerse
import com.versereminder.app.ui.theme.FigmaDarkBorder
import com.versereminder.app.ui.theme.FigmaDarkBorderGold
import com.versereminder.app.ui.theme.FigmaDarkCardAlt
import com.versereminder.app.ui.theme.FigmaDarkSurface
import com.versereminder.app.ui.theme.FigmaGold
import com.versereminder.app.ui.theme.FigmaGoldText
import com.versereminder.app.ui.theme.FigmaLightCardBg
import com.versereminder.app.ui.theme.FigmaLightSubtext
import com.versereminder.app.ui.theme.FigmaLightText
import com.versereminder.app.ui.theme.FigmaMuted
import com.versereminder.app.ui.theme.FigmaSecondary
import com.versereminder.app.ui.theme.FigmaWhiteText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateToAuth: () -> Unit = {}
) {
    val userSession by viewModel.userSession.collectAsState()
    val bibleVersion by viewModel.bibleVersion.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val notificationEnabled by viewModel.notificationEnabled.collectAsState()
    val notificationSound by viewModel.notificationSound.collectAsState()
    val notificationVibrate by viewModel.notificationVibrate.collectAsState()
    val previewMode by viewModel.previewMode.collectAsState()
    val fontSizeScale by viewModel.fontSizeScale.collectAsState()
    val fontType by viewModel.fontType.collectAsState()
    val sampleVerse by viewModel.sampleVerse.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

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
            // Figma Header: Eyebrow + Noto Serif Title + Action Button
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
                            text = "Personalisasi",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            ),
                            color = FigmaGold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Setelan",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp,
                            color = FigmaWhiteText
                        )
                    }

                    // Circle Help Button
                    Surface(
                        onClick = { showHelpDialog = true },
                        shape = CircleShape,
                        color = FigmaDarkSurface,
                        border = BorderStroke(1.dp, FigmaDarkBorder),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                                contentDescription = "Bantuan",
                                tint = FigmaSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // SECTION 1: VERSI ALKITAB (Figma: "Versi Alkitab" | "Untuk semua ayat")
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Versi Alkitab",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp),
                            color = FigmaCreamVerse,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Untuk semua ayat",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = FigmaMuted
                        )
                    }

                    // Primary 2-Option Selector Card (TB & WEB as in Figma)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FigmaDarkSurface),
                        border = BorderStroke(1.dp, FigmaDarkBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // TB Option
                            val isTb = bibleVersion == BibleVersion.TB
                            Surface(
                                onClick = { viewModel.setBibleVersion(BibleVersion.TB) },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isTb) FigmaGold else Color.Transparent,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 14.dp)
                                ) {
                                    Text(
                                        text = "TB",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = if (isTb) Color(0xFF090B10) else FigmaSecondary
                                    )
                                    Text(
                                        text = "Terjemahan Baru (Indonesia)",
                                        fontSize = 10.sp,
                                        color = if (isTb) Color(0xFF090B10) else FigmaMuted
                                    )
                                }
                            }

                            // WEB Option
                            val isWeb = bibleVersion == BibleVersion.WEB
                            Surface(
                                onClick = { viewModel.setBibleVersion(BibleVersion.WEB) },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isWeb) FigmaGold else Color.Transparent,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 14.dp)
                                ) {
                                    Text(
                                        text = "WEB",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = if (isWeb) Color(0xFF090B10) else FigmaSecondary
                                    )
                                    Text(
                                        text = "World English Bible",
                                        fontSize = 10.sp,
                                        color = if (isWeb) Color(0xFF090B10) else FigmaMuted
                                    )
                                }
                            }
                        }
                    }

                    // Expanded Translation Chips (TB2, BIS, TSI, KJV, NIV, ESV)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            BibleVersion.TB2 to "TB2",
                            BibleVersion.BIS to "BIS",
                            BibleVersion.TSI to "TSI",
                            BibleVersion.KJV to "KJV",
                            BibleVersion.NIV to "NIV",
                            BibleVersion.ESV to "ESV"
                        ).forEach { (ver, label) ->
                            val isSelected = bibleVersion == ver
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setBibleVersion(ver) },
                                label = { Text(label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                shape = RoundedCornerShape(8.dp),
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

            // SECTION 2: TAMPILAN - MODE SIANG & MALAM (Figma: "Tampilan" | "Mengikuti pilihan Anda")
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tampilan",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp),
                            color = FigmaCreamVerse,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Mengikuti pilihan Anda",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = FigmaMuted
                        )
                    }

                    // Two Side-by-Side Cards (Terang vs Gelap) matching Figma layout
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Card 1: TERANG (Day Mode)
                        val isLight = themeMode == ThemeMode.LIGHT
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setThemeMode(ThemeMode.LIGHT) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = FigmaLightCardBg),
                            border = BorderStroke(
                                width = if (isLight) 1.8.dp else 1.dp,
                                color = if (isLight) Color(0xFF805B10) else FigmaDarkBorder
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WbSunny,
                                        contentDescription = "Mode Terang",
                                        tint = Color(0xFF5A503D),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    if (isLight) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Color(0xFF805B10),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "Terang",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FigmaLightText
                                )
                                Text(
                                    text = if (isLight) "Sedang digunakan" else "Tidak aktif",
                                    fontSize = 10.sp,
                                    color = if (isLight) Color(0xFF805B10) else FigmaLightSubtext
                                )
                            }
                        }

                        // Card 2: GELAP (Night Mode)
                        val isDark = themeMode == ThemeMode.DARK
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setThemeMode(ThemeMode.DARK) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = FigmaDarkCardAlt),
                            border = BorderStroke(
                                width = if (isDark) 1.8.dp else 1.dp,
                                color = if (isDark) FigmaGold else FigmaDarkBorder
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DarkMode,
                                        contentDescription = "Mode Gelap",
                                        tint = FigmaGoldText,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    if (isDark) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = FigmaGold,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "Gelap",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FigmaCreamVerse
                                )
                                Text(
                                    text = if (isDark) "Sedang digunakan" else "Tidak aktif",
                                    fontSize = 10.sp,
                                    color = if (isDark) FigmaGold else FigmaMuted
                                )
                            }
                        }
                    }

                    // System Option Pill
                    val isSystem = themeMode == ThemeMode.SYSTEM
                    Surface(
                        onClick = { viewModel.setThemeMode(ThemeMode.SYSTEM) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSystem) FigmaGold.copy(alpha = 0.15f) else FigmaDarkSurface,
                        border = BorderStroke(1.dp, if (isSystem) FigmaGold else FigmaDarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Mengikuti Pengaturan Sistem Perangkat",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isSystem) FigmaGoldText else FigmaSecondary
                            )
                            if (isSystem) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = FigmaGold,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // SECTION 3: FORMAT NOTIFIKASI (Figma: "Format notifikasi" | "Pratinjau langsung")
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Format notifikasi",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp),
                            color = FigmaCreamVerse,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Pratinjau langsung",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = FigmaMuted
                        )
                    }

                    // 3 Figma Notification Cards
                    listOf(
                        Triple("FULL", "Ayat + referensi", "«TUHAN adalah gembalaku...» • Mazmur 23:1"),
                        Triple("REF_ONLY", "Referensi saja", "Buka Mazmur 23:1 di Verse Reminder"),
                        Triple("SHORT", "Ringkas", "Ayat hari ini siap dibaca")
                    ).forEach { (code, title, desc) ->
                        val isSelected = previewMode == code
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.setPreviewMode(code) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) FigmaDarkCardAlt else FigmaDarkSurface
                            ),
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) FigmaDarkBorderGold else FigmaDarkBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Choice Icon
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) FigmaGold else Color(0xFF202636),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = when (code) {
                                                "FULL" -> Icons.Default.FormatQuote
                                                "REF_ONLY" -> Icons.AutoMirrored.Filled.MenuBook
                                                else -> Icons.Default.Notifications
                                            },
                                            contentDescription = null,
                                            tint = if (isSelected) Color(0xFF090B10) else FigmaSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = title,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = if (isSelected) FigmaCreamVerse else FigmaWhiteText
                                    )
                                    Text(
                                        text = desc,
                                        fontSize = 10.sp,
                                        color = FigmaMuted
                                    )
                                }

                                RadioButton(
                                    selected = isSelected,
                                    onClick = { viewModel.setPreviewMode(code) },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = FigmaGold,
                                        unselectedColor = FigmaMuted
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // SECTION 4: TIPOGRAFI & UKURAN TULISAN
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FormatSize,
                                    contentDescription = null,
                                    tint = FigmaGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Ukuran Tulisan",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = FigmaWhiteText
                                )
                            }

                            val label = when {
                                fontSizeScale <= 0.88f -> "Kecil"
                                fontSizeScale <= 1.05f -> "Standar"
                                fontSizeScale <= 1.25f -> "Besar"
                                else -> "Ekstra Besar"
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = FigmaGold.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FigmaGoldText,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Slider(
                            value = fontSizeScale,
                            onValueChange = { viewModel.setFontSizeScale(it) },
                            valueRange = 0.85f..1.40f,
                            steps = 2
                        )

                        HorizontalDivider(color = FigmaDarkBorder)

                        Text(
                            text = "Jenis Huruf (Font)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FigmaWhiteText
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            AppFontType.entries.forEach { item ->
                                val isSelected = fontType == item
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.setFontType(item) },
                                    label = { Text(item.displayName, fontSize = 10.sp) },
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

                        // Live preview
                        sampleVerse?.let { verse ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Pratinjau Ayat:",
                                fontSize = 10.sp,
                                color = FigmaMuted
                            )
                            VerseCard(
                                verse = verse,
                                bibleVersion = bibleVersion,
                                fontSizeScale = fontSizeScale,
                                fontType = fontType,
                                onBookmarkClick = {}
                            )
                        }
                    }
                }
            }

            // SECTION 5: SESI AKUN & PENGATURAN TAMBAHAN
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Profile row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = FigmaGold.copy(alpha = 0.2f),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (userSession.isGuest) Icons.Default.Person else Icons.Default.AccountCircle,
                                        contentDescription = null,
                                        tint = FigmaGoldText,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = userSession.displayName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = FigmaWhiteText
                                )
                                Text(
                                    text = if (userSession.email.isNotBlank()) userSession.email else "Mode Tamu (Offline)",
                                    fontSize = 11.sp,
                                    color = FigmaMuted
                                )
                            }

                            if (!userSession.isGuest) {
                                IconButton(onClick = { showLogoutDialog = true }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Logout,
                                        contentDescription = "Keluar",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            } else {
                                OutlinedButton(
                                    onClick = onNavigateToAuth,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, FigmaGold)
                                ) {
                                    Text("Masuk", fontSize = 11.sp, color = FigmaGold)
                                }
                            }
                        }

                        HorizontalDivider(color = FigmaDarkBorder)

                        // Notification Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = FigmaSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Pengingat Terjadwal",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = FigmaWhiteText
                                    )
                                    Text(
                                        text = "Sinkronisasi otomatis dengan alarm perangkat",
                                        fontSize = 10.sp,
                                        color = FigmaMuted
                                    )
                                }
                            }
                            Switch(
                                checked = notificationEnabled,
                                onCheckedChange = { viewModel.setNotificationEnabled(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF090B10),
                                    checkedTrackColor = FigmaGold
                                )
                            )
                        }

                        HorizontalDivider(color = FigmaDarkBorder)

                        // Sound Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = null,
                                    tint = FigmaSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Suara Notifikasi",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = FigmaWhiteText
                                    )
                                    Text(
                                        text = "Bunyikan nada saat jadwal pengingat tiba",
                                        fontSize = 10.sp,
                                        color = FigmaMuted
                                    )
                                }
                            }
                            Switch(
                                checked = notificationSound,
                                onCheckedChange = { viewModel.setNotificationSound(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF090B10),
                                    checkedTrackColor = FigmaGold
                                )
                            )
                        }

                        HorizontalDivider(color = FigmaDarkBorder)

                        // Vibrate Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Vibration,
                                    contentDescription = null,
                                    tint = FigmaSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Getaran Notifikasi",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = FigmaWhiteText
                                    )
                                    Text(
                                        text = "Getarkan perangkat saat notifikasi muncul",
                                        fontSize = 10.sp,
                                        color = FigmaMuted
                                    )
                                }
                            }
                            Switch(
                                checked = notificationVibrate,
                                onCheckedChange = { viewModel.setNotificationVibrate(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF090B10),
                                    checkedTrackColor = FigmaGold
                                )
                            )
                        }

                        // Send Test Notification Button
                        Button(
                            onClick = { viewModel.sendTestNotification() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FigmaDarkCardAlt,
                                contentColor = FigmaGoldText
                            ),
                            border = BorderStroke(1.dp, FigmaDarkBorderGold)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Kirim Notifikasi Uji Coba", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Keluar dari Akun?", color = FigmaWhiteText) },
            text = { Text("Anda akan beralih ke Mode Tamu. Seluruh ayat tersimpan tetap aman di perangkat Anda.", color = FigmaSecondary) },
            containerColor = FigmaDarkSurface,
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout(onSuccess = onNavigateToAuth)
                    }
                ) {
                    Text("Keluar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal", color = FigmaSecondary)
                }
            }
        )
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("Tentang Verse Reminder", color = FigmaWhiteText) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Verse Reminder v1.0.0", fontWeight = FontWeight.Bold, color = FigmaGold)
                    Text(
                        "Aplikasi pengingat firman Alkitab harian melalui notifikasi dan widget home screen. Didesain secara eksklusif mengikuti Figma Verse Reminder 1.",
                        color = FigmaSecondary,
                        fontSize = 12.sp
                    )
                }
            },
            containerColor = FigmaDarkSurface,
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("Tutup", color = FigmaGold)
                }
            }
        )
    }
}
