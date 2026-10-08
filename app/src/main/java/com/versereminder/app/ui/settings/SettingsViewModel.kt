package com.versereminder.app.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.versereminder.app.data.repository.AuthRepository
import com.versereminder.app.data.repository.ScheduleRepository
import com.versereminder.app.data.repository.SettingsRepository
import com.versereminder.app.data.repository.VerseRepository
import com.versereminder.app.domain.model.AppFontType
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.ThemeMode
import com.versereminder.app.domain.model.UserSession
import com.versereminder.app.domain.model.Verse
import com.versereminder.app.domain.model.VerseCategory
import com.versereminder.app.scheduler.AlarmScheduler
import com.versereminder.app.scheduler.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val scheduleRepository: ScheduleRepository,
    private val verseRepository: VerseRepository,
    private val authRepository: AuthRepository,
    private val alarmScheduler: AlarmScheduler,
    private val context: Context
) : ViewModel() {

    val userSession: StateFlow<UserSession> = authRepository.userSessionFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSession())

    val bibleVersion: StateFlow<BibleVersion> = settingsRepository.bibleVersionFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BibleVersion.TB)

    val themeMode: StateFlow<ThemeMode> = settingsRepository.themeModeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

    val notificationEnabled: StateFlow<Boolean> = settingsRepository.notificationEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val notificationSound: StateFlow<Boolean> = settingsRepository.notificationSoundFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val notificationVibrate: StateFlow<Boolean> = settingsRepository.notificationVibrateFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val previewMode: StateFlow<String> = settingsRepository.previewModeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "FULL")

    val fontSizeScale: StateFlow<Float> = settingsRepository.fontSizeScaleFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1.0f)

    val fontType: StateFlow<AppFontType> = settingsRepository.fontTypeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppFontType.SERIF)

    private val _sampleVerse = MutableStateFlow<Verse?>(null)
    val sampleVerse: StateFlow<Verse?> = _sampleVerse.asStateFlow()

    init {
        loadSampleVerse()
    }

    private fun loadSampleVerse() {
        viewModelScope.launch {
            val verse = verseRepository.getRandomVerseByCategory(VerseCategory.ALL)
            _sampleVerse.value = verse
        }
    }

    fun setBibleVersion(version: BibleVersion) {
        viewModelScope.launch {
            settingsRepository.setBibleVersion(version)
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(mode)
        }
    }

    fun setNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationEnabled(enabled)
            // Immediately synchronize system alarms with notification setting
            val activeSchedules = scheduleRepository.getActiveSchedules()
            if (enabled) {
                for (schedule in activeSchedules) {
                    alarmScheduler.schedule(schedule)
                }
            } else {
                for (schedule in activeSchedules) {
                    alarmScheduler.cancel(schedule.id)
                }
            }
        }
    }

    fun setNotificationSound(sound: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationSound(sound)
        }
    }

    fun setNotificationVibrate(vibrate: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationVibrate(vibrate)
        }
    }

    fun setPreviewMode(mode: String) {
        viewModelScope.launch {
            settingsRepository.setPreviewMode(mode)
        }
    }

    fun setFontSizeScale(scale: Float) {
        viewModelScope.launch {
            settingsRepository.setFontSizeScale(scale)
        }
    }

    fun setFontType(type: AppFontType) {
        viewModelScope.launch {
            settingsRepository.setFontType(type)
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onSuccess()
        }
    }

    fun sendTestNotification() {
        viewModelScope.launch {
            val verse = verseRepository.getRandomVerseByCategory(VerseCategory.ALL)
            if (verse != null) {
                val version = settingsRepository.bibleVersionFlow.first()
                val preview = settingsRepository.previewModeFlow.first()
                val sound = settingsRepository.notificationSoundFlow.first()
                val vibrate = settingsRepository.notificationVibrateFlow.first()
                val helper = NotificationHelper(context)
                helper.showVerseNotification(
                    notificationId = 9999,
                    title = "Uji Coba Pengingat Ayat",
                    verse = verse,
                    bibleVersion = version,
                    previewMode = preview,
                    soundEnabled = sound,
                    vibrateEnabled = vibrate
                )
            }
        }
    }

    class Factory(
        private val settingsRepository: SettingsRepository,
        private val scheduleRepository: ScheduleRepository,
        private val verseRepository: VerseRepository,
        private val authRepository: AuthRepository,
        private val alarmScheduler: AlarmScheduler,
        private val context: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(
                settingsRepository,
                scheduleRepository,
                verseRepository,
                authRepository,
                alarmScheduler,
                context
            ) as T
        }
    }
}
