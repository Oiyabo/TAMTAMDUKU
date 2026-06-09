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

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tamtamduku.data.local.SessionManager

data class ProfileUiState(
    val name: String = "",
    val role: String = "Pelanggan",
    val email: String = "",
    val address: String = "",
    val phone: String = "",
    val favoriteWorkers: List<String> = emptyList(),
    val settings: UserSettings = UserSettings()
)

class ProfileViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: WorkerRepository = WorkerRepository()
) : AndroidViewModel(application) {
    
    private val sessionManager = SessionManager(application)
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    private var currentUserProfile: User? = null

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val uid = sessionManager.getUserId() ?: return
        viewModelScope.launch {
            repository.getUserProfile(uid).collect { account ->
                if (account != null) {
                    currentUserProfile = account
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

    fun updateAddress(newAddress: String) {
        _uiState.update { it.copy(address = newAddress) }
        saveChangesToFirebase()
    }

    fun deleteAddress() {
        _uiState.update { it.copy(address = "") }
        saveChangesToFirebase()
    }

    fun updateProfile(name: String, email: String, address: String) {
        _uiState.update { it.copy(name = name, email = email, address = address) }
        saveChangesToFirebase()
    }
    
    private fun saveChangesToFirebase() {
        val uid = sessionManager.getUserId() ?: return
        val currentState = _uiState.value
        val updatedUser = currentUserProfile?.copy(
            name = currentState.name,
            email = currentState.email,
            address = currentState.address,
            phone = currentState.phone,
            favoriteWorkers = currentState.favoriteWorkers,
            settings = currentState.settings
        ) ?: User(
            id = uid,
            name = currentState.name,
            email = currentState.email,
            address = currentState.address,
            phone = currentState.phone,
            favoriteWorkers = currentState.favoriteWorkers,
            settings = currentState.settings
        )
        repository.updateUserProfile(updatedUser) { success ->
            if (success) {
                currentUserProfile = updatedUser
            }
        }
    }
}
