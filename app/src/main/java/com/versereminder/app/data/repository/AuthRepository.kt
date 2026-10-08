package com.versereminder.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.versereminder.app.domain.model.AuthProvider
import com.versereminder.app.domain.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.UUID

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_auth_session")

class AuthRepository(private val context: Context) {

    private object Keys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_DISPLAY_NAME = stringPreferencesKey("user_display_name")
        val AUTH_PROVIDER = stringPreferencesKey("auth_provider")
        val IS_GUEST = booleanPreferencesKey("is_guest")
    }

    val userSessionFlow: Flow<UserSession> = context.authDataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs ->
        val isLoggedIn = prefs[Keys.IS_LOGGED_IN] ?: false
        val isGuest = prefs[Keys.IS_GUEST] ?: false
        val userId = prefs[Keys.USER_ID] ?: ""
        val email = prefs[Keys.USER_EMAIL] ?: ""
        val name = prefs[Keys.USER_DISPLAY_NAME] ?: if (isGuest) "Tamu" else "Pengguna"
        val providerStr = prefs[Keys.AUTH_PROVIDER] ?: AuthProvider.GUEST.name
        val provider = try {
            AuthProvider.valueOf(providerStr)
        } catch (_: Exception) {
            AuthProvider.GUEST
        }

        UserSession(
            userId = userId,
            email = email,
            displayName = name,
            authProvider = provider,
            isLoggedIn = isLoggedIn,
            isGuest = isGuest
        )
    }

    suspend fun loginWithEmail(email: String, name: String = ""): UserSession {
        val cleanEmail = email.trim()
        val displayName = if (name.isNotBlank()) name else cleanEmail.substringBefore("@").replaceFirstChar { it.uppercase() }
        val userId = UUID.randomUUID().toString()

        context.authDataStore.edit { prefs ->
            prefs[Keys.IS_LOGGED_IN] = true
            prefs[Keys.IS_GUEST] = false
            prefs[Keys.USER_ID] = userId
            prefs[Keys.USER_EMAIL] = cleanEmail
            prefs[Keys.USER_DISPLAY_NAME] = displayName
            prefs[Keys.AUTH_PROVIDER] = AuthProvider.EMAIL.name
        }

        return UserSession(
            userId = userId,
            email = cleanEmail,
            displayName = displayName,
            authProvider = AuthProvider.EMAIL,
            isLoggedIn = true,
            isGuest = false
        )
    }

    suspend fun loginWithGoogle(email: String = "steven.samba@gmail.com", name: String = "Steven"): UserSession {
        val userId = UUID.randomUUID().toString()

        context.authDataStore.edit { prefs ->
            prefs[Keys.IS_LOGGED_IN] = true
            prefs[Keys.IS_GUEST] = false
            prefs[Keys.USER_ID] = userId
            prefs[Keys.USER_EMAIL] = email
            prefs[Keys.USER_DISPLAY_NAME] = name
            prefs[Keys.AUTH_PROVIDER] = AuthProvider.GOOGLE.name
        }

        return UserSession(
            userId = userId,
            email = email,
            displayName = name,
            authProvider = AuthProvider.GOOGLE,
            isLoggedIn = true,
            isGuest = false
        )
    }

    suspend fun continueAsGuest() {
        context.authDataStore.edit { prefs ->
            prefs[Keys.IS_LOGGED_IN] = false
            prefs[Keys.IS_GUEST] = true
            prefs[Keys.USER_ID] = "guest_" + UUID.randomUUID().toString().take(8)
            prefs[Keys.USER_EMAIL] = ""
            prefs[Keys.USER_DISPLAY_NAME] = "Tamu"
            prefs[Keys.AUTH_PROVIDER] = AuthProvider.GUEST.name
        }
    }

    suspend fun logout() {
        context.authDataStore.edit { prefs ->
            prefs[Keys.IS_LOGGED_IN] = false
            prefs[Keys.IS_GUEST] = true
            prefs[Keys.USER_ID] = ""
            prefs[Keys.USER_EMAIL] = ""
            prefs[Keys.USER_DISPLAY_NAME] = "Tamu"
            prefs[Keys.AUTH_PROVIDER] = AuthProvider.GUEST.name
        }
    }
}
