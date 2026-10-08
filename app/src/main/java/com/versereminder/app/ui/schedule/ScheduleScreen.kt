package com.versereminder.app.ui.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.versereminder.app.domain.model.Schedule
import com.versereminder.app.domain.model.VerseCategory
import com.versereminder.app.ui.components.TimePickerDialog
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
fun ScheduleScreen(
    viewModel: ScheduleViewModel
) {
    val schedules by viewModel.schedules.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editingSchedule by remember { mutableStateOf<Schedule?>(null) }

    val activeCount = schedules.count { it.isEnabled }

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
            // Figma Header: Eyebrow + Noto Serif Title + Calendar Action Button
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
                            text = "Ritme harian",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            ),
                            color = FigmaGold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Jadwal pengingat",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp,
                            color = FigmaWhiteText
                        )
                    }

                    // Calendar Action Button
                    Surface(
                        onClick = {
                            editingSchedule = null
                            showDialog = true
                        },
                        shape = CircleShape,
                        color = FigmaDarkSurface,
                        border = BorderStroke(1.dp, FigmaDarkBorder),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Kalender",
                                tint = FigmaSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // SECTION 1: PENGINGAT DEFAULT (Figma: "Pengingat default" | "3 aktif")
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pengingat default",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp),
                            color = FigmaCreamVerse,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "$activeCount aktif",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = FigmaMuted
                        )
                    }

                    // Render Default Schedules (ID 1, 2, 3) or top 3 schedules
                    val defaultSchedules = schedules.take(3)
                    defaultSchedules.forEach { schedule ->
                        val (icon, periodLabel, subtext) = when {
                            schedule.hour in 4..10 -> Triple(Icons.Default.WbTwilight, "PAGI", "Mulai hari dengan firman")
                            schedule.hour in 11..14 -> Triple(Icons.Default.WbSunny, "SIANG", "Jeda singkat untuk menguatkan")
                            else -> Triple(Icons.Default.DarkMode, "MALAM", "Tenang sebelum beristirahat")
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = FigmaDarkSurface),
                            border = BorderStroke(1.dp, FigmaDarkBorder)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Time Icon with Gold Accent
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = FigmaGold.copy(alpha = 0.15f),
                                        modifier = Modifier.size(38.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = null,
                                                tint = FigmaGoldText,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = schedule.formattedTime,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = FigmaWhiteText
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = periodLabel,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = FigmaGold
                                            )
                                        }
                                        Text(
                                            text = subtext,
                                            fontSize = 10.sp,
                                            color = FigmaMuted
                                        )
                                    }
                                }

                                Switch(
                                    checked = schedule.isEnabled,
                                    onCheckedChange = { isChecked -> viewModel.toggleSchedule(schedule, isChecked) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color(0xFF090B10),
                                        checkedTrackColor = FigmaGold,
                                        uncheckedThumbColor = FigmaSecondary,
                                        uncheckedTrackColor = FigmaDarkBorder
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // SECTION 2: TAMBAH WAKTU KHUSUS (Figma: "Tambah waktu khusus" | "TimePicker")
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tambah waktu khusus",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp),
                            color = FigmaCreamVerse,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "TimePicker",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = FigmaMuted
                        )
                    }

                    // Figma Custom TimePicker Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                editingSchedule = null
                                showDialog = true
                            },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = FigmaDarkCardAlt),
                        border = BorderStroke(1.2.dp, FigmaDarkBorderGold)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Waktu khusus",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = FigmaGold
                                    )
                                    Text(
                                        text = "Atur Jam Pengingat Baru",
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 18.sp,
                                        color = FigmaCreamVerse
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = FigmaGold.copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, FigmaGold.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = "Setiap hari",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = FigmaGoldText,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    editingSchedule = null
                                    showDialog = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = FigmaGold,
                                    contentColor = Color(0xFF090B10)
                                )
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Buka Pemilih Waktu (TimePicker)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // SECTION 3: ADDITIONAL SCHEDULES LIST
            val customSchedules = schedules.drop(3)
            if (customSchedules.isNotEmpty()) {
                item {
                    Text(
                        text = "Jadwal Tambahan (${customSchedules.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp),
                        color = FigmaCreamVerse,
                        fontWeight = FontWeight.Medium
                    )
                }

                items(customSchedules, key = { it.id }) { schedule ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = FigmaDarkSurface),
                        border = BorderStroke(1.dp, FigmaDarkBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = schedule.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FigmaWhiteText
                                )
                                Text(
                                    text = "${schedule.formattedTime} • ${schedule.category.displayName}",
                                    fontSize = 11.sp,
                                    color = FigmaGoldText
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Switch(
                                    checked = schedule.isEnabled,
                                    onCheckedChange = { isChecked -> viewModel.toggleSchedule(schedule, isChecked) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color(0xFF090B10),
                                        checkedTrackColor = FigmaGold
                                    )
                                )
                                IconButton(
                                    onClick = {
                                        editingSchedule = schedule
                                        showDialog = true
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = FigmaSecondary, modifier = Modifier.size(16.dp))
                                }
                                IconButton(
                                    onClick = { viewModel.deleteSchedule(schedule.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        ScheduleDialog(
            schedule = editingSchedule,
            onDismiss = {
                showDialog = false
                editingSchedule = null
            },
            onSave = { title, hour, minute, category ->
                if (editingSchedule != null) {
                    viewModel.updateSchedule(editingSchedule!!, title, hour, minute, category)
                } else {
                    viewModel.addSchedule(title, hour, minute, category)
                }
                showDialog = false
                editingSchedule = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleDialog(
    schedule: Schedule?,
    onDismiss: () -> Unit,
    onSave: (title: String, hour: Int, minute: Int, category: VerseCategory) -> Unit
) {
    var title by remember { mutableStateOf(schedule?.title ?: "") }
    var hour by remember { mutableIntStateOf(schedule?.hour ?: 8) }
    var minute by remember { mutableIntStateOf(schedule?.minute ?: 30) }
    var category by remember { mutableStateOf(schedule?.category ?: VerseCategory.ALL) }
    var showTimePicker by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = FigmaDarkSurface,
        title = {
            Text(
                text = if (schedule != null) "Edit Jadwal" else "Tambah Jadwal Baru",
                color = FigmaWhiteText,
                fontFamily = FontFamily.Serif
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nama Pengingat", color = FigmaSecondary) },
                    placeholder = { Text("Contoh: Renungan Doa", color = FigmaMuted) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Time button
                OutlinedButton(
                    onClick = { showTimePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, FigmaGold)
                ) {
                    Text(
                        text = "Waktu: ${String.format("%02d:%02d", hour, minute)}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = FigmaGoldText
                    )
                }

                // Category selector
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = "${category.icon}  ${category.displayName}",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Kategori Ayat", color = FigmaSecondary) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false },
                        modifier = Modifier.background(FigmaDarkSurface)
                    ) {
                        VerseCategory.entries.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text("${cat.icon}  ${cat.displayName}", color = FigmaWhiteText) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val finalTitle = if (title.isBlank()) "Pengingat Firman" else title
                    onSave(finalTitle, hour, minute, category)
                }
            ) {
                Text("Simpan", color = FigmaGold, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = FigmaSecondary)
            }
        }
    )

    if (showTimePicker) {
        TimePickerDialog(
            initialHour = hour,
            initialMinute = minute,
            onTimeSelected = { h, m ->
                hour = h
                minute = m
                showTimePicker = false
            },
            onDismissRequest = { showTimePicker = false }
        )
    }
}
