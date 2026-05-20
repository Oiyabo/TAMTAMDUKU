package com.example.tamtamduku.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import model.AkunData

data class ProfileUiState(
    val name: String = "",
    val role: String = "Pelanggan",
    val email: String = ""
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        _uiState.value = ProfileUiState(
            name = AkunData.NAMA_DAFTAR.ifEmpty { "Rina Amelia" },
            email = AkunData.EMAIL_DAFTAR
        )
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        // Implement logout logic if needed
        onLogoutSuccess()
    }
}
