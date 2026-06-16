package com.example.tamtamduku.presentation.auth.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.local.SessionManager
import com.example.tamtamduku.domain.model.User
import com.example.tamtamduku.data.network.ApiClient
import com.example.tamtamduku.data.network.LoginRequest
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val userAccount: User? = null,
    val loginError: String? = null,
    val registrationSuccess: Boolean = false,
    val isLoggedIn: Boolean = false
)

class AuthViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: WorkerRepository = WorkerRepository()
) : AndroidViewModel(application) {
    
    private val sessionManager = SessionManager(application)
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkSession()
        fetchAccount()
    }

    fun checkSession() {
        if (sessionManager.isLoggedIn() && sessionManager.getUserId() != null) {
            _uiState.update { it.copy(isLoggedIn = true) }
        }
    }

    private fun fetchAccount() {
        val uid = sessionManager.getUserId() ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getUserProfile(uid).collect { account ->
                _uiState.update { it.copy(
                    isLoading = false,
                    userAccount = account
                ) }
            }
        }
    }

    fun login(emailInput: String, passwordInput: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loginError = null) }
            try {
                // Resolve email to username if input looks like an email (contains '@')
                val resolvedUsername = if (emailInput.contains("@")) {
                    try {
                        val filterResponse = ApiClient.dummyJsonApi.filterUsers(key = "email", value = emailInput)
                        if (filterResponse.users.isNotEmpty()) {
                            filterResponse.users.first().username
                        } else {
                            emailInput // Fallback to raw input if no user is found
                        }
                    } catch (e: Exception) {
                        emailInput // Fallback to raw input on API failure
                    }
                } else {
                    emailInput
                }

                // Call login API with resolved username
                val response = ApiClient.dummyJsonApi.login(
                    LoginRequest(username = resolvedUsername, password = passwordInput)
                )
                sessionManager.saveToken(response.accessToken, response.id.toString())
                
                _uiState.update { it.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    userAccount = User(id = response.id.toString(), name = "${response.firstName} ${response.lastName}", email = response.email)
                ) }
                // Re-trigger fetchAccount to get Firestore data
                fetchAccount()
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    loginError = "Email/Username atau Password Salah! (Gagal login ke server)"
                ) }
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        sessionManager.clearSession()
        _uiState.update { it.copy(isLoggedIn = false, userAccount = null) }
        onLogout()
    }

    fun register(name: String, email: String, phone: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Simulating API call for registration
            val newUserId = System.currentTimeMillis().toString()
            val newUser = User(
                id = newUserId,
                name = name,
                email = email,
                phone = phone,
                address = "",
                favoriteWorkers = emptyList(),
                settings = com.example.tamtamduku.domain.model.UserSettings()
            )
            repository.updateUserProfile(newUser) { success ->
                _uiState.update { it.copy(
                    isLoading = false,
                    registrationSuccess = true,
                    userAccount = newUser
                ) }
                onSuccess()
            }
        }
    }
}
