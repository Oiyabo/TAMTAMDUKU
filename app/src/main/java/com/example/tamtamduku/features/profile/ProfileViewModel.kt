package com.example.tamtamduku.features.profile

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
import com.example.tamtamduku.core.util.SessionManager

import android.net.Uri
import com.example.tamtamduku.domain.model.UserAddress

data class ProfileUiState(
    val name: String = "",
    val role: String = "Pelanggan",
    val email: String = "",
    val address: String = "",
    val addressList: List<UserAddress> = emptyList(),
    val phone: String = "",
    val favoriteWorkers: List<String> = emptyList(),
    val settings: UserSettings = UserSettings(),
    val profileUrl: String = "",
    val isUploadingImage: Boolean = false
)

class ProfileViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: WorkerRepository = WorkerRepository()
) : AndroidViewModel(application) {
    
    private val sessionManager = SessionManager(application)
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    private var currentUserProfile: User? = null
    private var profileJob: kotlinx.coroutines.Job? = null

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        val uid = sessionManager.getUserId() ?: return
        profileJob?.cancel()
        profileJob = viewModelScope.launch {
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
                            settings = account.settings,
                            profileUrl = account.profileUrl
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

    fun updateProfile(name: String, email: String, address: String, profileUrl: String = _uiState.value.profileUrl) {
        _uiState.update { it.copy(name = name, email = email, address = address, profileUrl = profileUrl) }
        saveChangesToFirebase()
    }
    
    fun uploadProfileImage(uri: Uri) {
        val uid = sessionManager.getUserId() ?: return
        _uiState.update { it.copy(isUploadingImage = true) }
        val imagePath = "profile_images/$uid.jpg"
        
        repository.uploadImageToStorage(uri, imagePath) { downloadUrl ->
            if (downloadUrl != null) {
                _uiState.update { it.copy(profileUrl = downloadUrl, isUploadingImage = false) }
                saveChangesToFirebase()
            } else {
                _uiState.update { it.copy(isUploadingImage = false) }
            }
        }
    }

    fun updatePushNotificationSetting(enabled: Boolean) {
        val currentSettings = _uiState.value.settings
        _uiState.update { it.copy(settings = currentSettings.copy(pushNotification = enabled)) }
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
            settings = currentState.settings,
            profileUrl = currentState.profileUrl
        ) ?: User(
            id = uid,
            name = currentState.name,
            email = currentState.email,
            address = currentState.address,
            addressList = currentState.addressList,
            phone = currentState.phone,
            favoriteWorkers = currentState.favoriteWorkers,
            settings = currentState.settings,
            profileUrl = currentState.profileUrl
        )
        repository.updateUserProfile(updatedUser) { success ->
            if (success) {
                currentUserProfile = updatedUser
            }
        }
    }
}
