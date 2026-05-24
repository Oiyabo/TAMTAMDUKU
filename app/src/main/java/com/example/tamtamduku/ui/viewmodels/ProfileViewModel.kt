package com.example.tamtamduku.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val name: String = "",
    val role: String = "Pelanggan",
    val email: String = ""
)

class ProfileViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            repository.getUserAccount().collect { account ->
                if (account != null) {
                    _uiState.update { 
                        it.copy(
                            name = account.name ?: "",
                            email = account.email ?: ""
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
