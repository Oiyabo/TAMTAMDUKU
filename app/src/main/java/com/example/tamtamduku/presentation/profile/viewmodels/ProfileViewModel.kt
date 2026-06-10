package com.example.tamtamduku.presentation.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.domain.model.UserSettings
import com.example.tamtamduku.domain.model.User
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tamtamduku.data.local.SessionManager

import com.example.tamtamduku.domain.model.UserAddress

data class ProfileUiState(
    val name: String = "",
    val role: String = "Pelanggan",
    val email: String = "",
    val address: String = "",
    val addressList: List<UserAddress> = emptyList(),
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
                            addressList = account.addressList,
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

    fun addAddress(name: String, fullAddress: String, isDefault: Boolean) {
        var newList = _uiState.value.addressList.toMutableList()
        val isFirst = newList.isEmpty()
        val finalIsDefault = isDefault || isFirst
        
        if (finalIsDefault) {
            // Unset others
            newList = newList.map { it.copy(isDefault = false) }.toMutableList()
            _uiState.update { it.copy(address = fullAddress) }
        }
        
        val newAddress = UserAddress(
            name = name,
            fullAddress = fullAddress,
            isDefault = finalIsDefault
        )
        newList.add(newAddress)
        _uiState.update { it.copy(addressList = newList) }
        saveChangesToFirebase()
    }

    fun setDefaultAddress(id: String) {
        var selectedAddress = ""
        val newList = _uiState.value.addressList.map { 
            if (it.id == id) selectedAddress = it.fullAddress
            it.copy(isDefault = it.id == id)
        }
        _uiState.update { it.copy(addressList = newList, address = selectedAddress) }
        saveChangesToFirebase()
    }

    fun deleteAddress(id: String) {
        val newList = _uiState.value.addressList.filter { it.id != id }.toMutableList()
        // If we deleted the default, make the first one default
        if (newList.isNotEmpty() && newList.none { it.isDefault }) {
            newList[0] = newList[0].copy(isDefault = true)
            _uiState.update { it.copy(address = newList[0].fullAddress) }
        } else if (newList.isEmpty()) {
            _uiState.update { it.copy(address = "") }
        }
        _uiState.update { it.copy(addressList = newList) }
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
            addressList = currentState.addressList,
            phone = currentState.phone,
            favoriteWorkers = currentState.favoriteWorkers,
            settings = currentState.settings
        ) ?: User(
            id = uid,
            name = currentState.name,
            email = currentState.email,
            address = currentState.address,
            addressList = currentState.addressList,
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
