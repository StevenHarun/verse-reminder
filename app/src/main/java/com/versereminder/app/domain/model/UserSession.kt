package com.versereminder.app.domain.model

enum class AuthProvider(val displayName: String) {
    EMAIL("Email & Kata Sandi"),
    GOOGLE("Google Account"),
    GUEST("Mode Tamu (Tanpa Akun)")
}

data class UserSession(
    val userId: String = "",
    val email: String = "",
    val displayName: String = "Tamu",
    val authProvider: AuthProvider = AuthProvider.GUEST,
    val isLoggedIn: Boolean = false,
    val isGuest: Boolean = true
)
