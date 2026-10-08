package com.versereminder.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.versereminder.app.domain.model.AppFontType
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class SettingsRepository(private val context: Context) {

    private object PreferencesKeys {
        val BIBLE_VERSION = stringPreferencesKey("bible_version")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
        val NOTIFICATION_SOUND = booleanPreferencesKey("notification_sound")
        val NOTIFICATION_VIBRATE = booleanPreferencesKey("notification_vibrate")
        val PREVIEW_MODE = stringPreferencesKey("preview_mode")
        val LAST_DAILY_VERSE_ID = longPreferencesKey("last_daily_verse_id")
        val LAST_DAILY_VERSE_DATE = stringPreferencesKey("last_daily_verse_date")
        val FONT_SIZE_SCALE = floatPreferencesKey("font_size_scale")
        val FONT_FAMILY_TYPE = stringPreferencesKey("font_family_type")
    }

    private val safeData: Flow<Preferences> = context.dataStore.data.catch { emit(emptyPreferences()) }

    val bibleVersionFlow: Flow<BibleVersion> = safeData.map { preferences ->
        val code = preferences[PreferencesKeys.BIBLE_VERSION] ?: BibleVersion.TB.code
        BibleVersion.fromCode(code)
    }

    val themeModeFlow: Flow<ThemeMode> = safeData.map { preferences ->
        val modeStr = preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
        ThemeMode.fromString(modeStr)
    }

    val notificationEnabledFlow: Flow<Boolean> = safeData.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATION_ENABLED] ?: true
    }

    val notificationSoundFlow: Flow<Boolean> = safeData.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATION_SOUND] ?: true
    }

    val notificationVibrateFlow: Flow<Boolean> = safeData.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATION_VIBRATE] ?: true
    }

    val previewModeFlow: Flow<String> = safeData.map { preferences ->
        preferences[PreferencesKeys.PREVIEW_MODE] ?: "FULL"
    }

    val fontSizeScaleFlow: Flow<Float> = safeData.map { preferences ->
        preferences[PreferencesKeys.FONT_SIZE_SCALE] ?: 1.0f
    }

    val fontTypeFlow: Flow<AppFontType> = safeData.map { preferences ->
        val typeStr = preferences[PreferencesKeys.FONT_FAMILY_TYPE] ?: AppFontType.SERIF.name
        AppFontType.fromString(typeStr)
    }

    suspend fun setBibleVersion(version: BibleVersion) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.BIBLE_VERSION] = version.code
        }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode.name
        }
    }

    suspend fun setNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATION_ENABLED] = enabled
        }
    }

    suspend fun setNotificationSound(sound: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATION_SOUND] = sound
        }
    }

    suspend fun setNotificationVibrate(vibrate: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATION_VIBRATE] = vibrate
        }
    }

    suspend fun setPreviewMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PREVIEW_MODE] = mode
        }
    }

    suspend fun setFontSizeScale(scale: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SIZE_SCALE] = scale
        }
    }

    suspend fun setFontType(type: AppFontType) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_FAMILY_TYPE] = type.name
        }
    }

    suspend fun getDailyVerseCache(): Pair<String?, Long?> {
        return try {
            val preferences = context.dataStore.data.first()
            val date = preferences[PreferencesKeys.LAST_DAILY_VERSE_DATE]
            val verseId = preferences[PreferencesKeys.LAST_DAILY_VERSE_ID]
            Pair(date, verseId)
        } catch (e: Exception) {
            Pair(null, null)
        }
    }

    suspend fun saveDailyVerseCache(date: String, verseId: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_DAILY_VERSE_DATE] = date
            preferences[PreferencesKeys.LAST_DAILY_VERSE_ID] = verseId
        }
    }
}
