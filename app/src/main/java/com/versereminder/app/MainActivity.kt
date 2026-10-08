package com.versereminder.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import com.versereminder.app.domain.model.ThemeMode
import com.versereminder.app.ui.navigation.VerseNavGraph
import com.versereminder.app.ui.theme.VerseReminderTheme

class MainActivity : ComponentActivity() {

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
            // Permission granted or denied handled gracefully
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val app = application as VerseReminderApp

        setContent {
            val themeMode by app.settingsRepository.themeModeFlow.collectAsState(initial = ThemeMode.SYSTEM)

            VerseReminderTheme(themeMode = themeMode) {
                VerseNavGraph(app = app)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check and request notification permission safely after activity is started
        checkNotificationPermission()
    }

    private fun checkNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
