package com.versereminder.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.versereminder.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isRegisterMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEmailVerificationPending: Boolean = false,
    val verificationCode: String = "",
    val expectedVerificationCode: String = "741285",
    val verificationSuccessMessage: String? = null
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name, errorMessage = null)
    }

    fun onVerificationCodeChange(code: String) {
        if (code.length <= 6) {
            _uiState.value = _uiState.value.copy(verificationCode = code, errorMessage = null)
        }
    }

    fun toggleMode() {
        _uiState.value = _uiState.value.copy(
            isRegisterMode = !_uiState.value.isRegisterMode,
            errorMessage = null,
            isEmailVerificationPending = false
        )
    }

    fun cancelVerification() {
        _uiState.value = _uiState.value.copy(
            isEmailVerificationPending = false,
            errorMessage = null,
            verificationCode = ""
        )
    }

    fun resendVerificationCode() {
        // Generate a new 6-digit code
        val newCode = (100000..999999).random().toString()
        _uiState.value = _uiState.value.copy(
            expectedVerificationCode = newCode,
            verificationSuccessMessage = "Kode verifikasi baru telah dikirim ke ${_uiState.value.email} (Kode simulasi: $newCode)",
            errorMessage = null
        )
    }

    fun submitEmailAuth(onSuccess: () -> Unit) {
        val state = _uiState.value
        val cleanEmail = state.email.trim()

        if (cleanEmail.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Email wajib diisi")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            _uiState.value = state.copy(errorMessage = "Format email tidak valid")
            return
        }
        if (state.password.length < 6) {
            _uiState.value = state.copy(errorMessage = "Kata sandi minimal 6 karakter")
            return
        }

        // If user is REGISTERING: Trigger Email Verification step
        if (state.isRegisterMode) {
            val generatedCode = (100000..999999).random().toString()
            _uiState.value = state.copy(
                isEmailVerificationPending = true,
                expectedVerificationCode = generatedCode,
                verificationCode = "",
                errorMessage = null,
                verificationSuccessMessage = "Kode verifikasi dikirim ke $cleanEmail (Kode simulasi: $generatedCode)"
            )
            return
        }

        // If user is LOGGING IN (already verified previously)
        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)
            try {
                authRepository.loginWithEmail(cleanEmail, state.name)
                _uiState.value = state.copy(isLoading = false)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = state.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Terjadi kesalahan saat masuk")
            }
        }
    }

    fun verifyEmailCode(onSuccess: () -> Unit) {
        val state = _uiState.value
        val inputCode = state.verificationCode.trim()

        if (inputCode.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Masukkan kode verifikasi 6 digit")
            return
        }

        // Accept the generated code or universal test code 123456
        if (inputCode != state.expectedVerificationCode && inputCode != "123456") {
            _uiState.value = state.copy(errorMessage = "Kode verifikasi salah atau kadaluarsa")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)
            try {
                authRepository.loginWithEmail(state.email.trim(), state.name)
                _uiState.value = state.copy(isLoading = false, isEmailVerificationPending = false)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = state.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Gagal memverifikasi akun")
            }
        }
    }

    fun loginWithGoogleAccount(email: String, name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val cleanEmail = email.trim()
                val displayName = if (name.isNotBlank()) name else cleanEmail.substringBefore("@").replaceFirstChar { it.uppercase() }
                authRepository.loginWithGoogle(email = cleanEmail, name = displayName)
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Gagal masuk dengan Google")
            }
        }
    }

    fun continueAsGuest(onSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.continueAsGuest()
            onSuccess()
        }
    }

    class Factory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(authRepository) as T
        }
    }
}
