package com.example.tamtamduku.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.local.SessionManager
import com.example.tamtamduku.data.model.UserAccount
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
    val userAccount: UserAccount? = null,
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
        if (sessionManager.isLoggedIn()) {
            _uiState.update { it.copy(isLoggedIn = true) }
        }
    }

    private fun fetchAccount() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getUserAccount().collect { account ->
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
                // DummyJSON API expects 'username' instead of email.
                val response = ApiClient.dummyJsonApi.login(
                    LoginRequest(username = emailInput, password = passwordInput)
                )
                sessionManager.saveToken(response.accessToken)
                
                _uiState.update { it.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    userAccount = UserAccount(name = "${response.firstName} ${response.lastName}", email = response.email, password = passwordInput)
                ) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    loginError = "Email atau Password Salah! (Gagal login ke server)"
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
            // In a real app, we would send this to the repository
            _uiState.update { it.copy(
                isLoading = false,
                registrationSuccess = true,
                userAccount = UserAccount(name = name, email = email, password = password)
            ) }
            onSuccess()
        }
    }
}
