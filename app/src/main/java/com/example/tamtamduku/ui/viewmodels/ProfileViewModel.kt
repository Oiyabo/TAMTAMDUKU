package com.example.tamtamduku.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.model.UserSettings
import com.example.tamtamduku.data.model.User
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val name: String = "",
    val role: String = "Pelanggan",
    val email: String = "",
    val address: String = "",
    val phone: String = "",
    val favoriteWorkers: List<String> = emptyList(),
    val settings: UserSettings = UserSettings()
)

class ProfileViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            repository.getUsers().collect { accounts ->
                val account = accounts.firstOrNull()
                if (account != null) {
                    _uiState.update { 
                        it.copy(
                            name = account.name,
                            email = account.email,
                            address = account.address,
                            phone = account.phone,
                            favoriteWorkers = account.favoriteWorkers,
                            settings = account.settings
                        ) 
                    }
                }
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        // Implement logout logic if needed
        onLogoutSuccess()
    }
}
