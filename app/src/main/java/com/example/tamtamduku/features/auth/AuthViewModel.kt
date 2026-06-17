package com.example.tamtamduku.features.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.core.util.SessionManager
import com.example.tamtamduku.domain.model.User
import com.example.tamtamduku.domain.model.UserSettings
import com.example.tamtamduku.data.remote.ApiClient
import com.example.tamtamduku.data.remote.LoginRequest
import com.example.tamtamduku.data.repository.WorkerRepository
import com.google.firebase.messaging.FirebaseMessaging
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
            
            repository.getUserByEmail(emailInput) { localUser ->
                if (localUser != null && localUser.password.isNotEmpty() && localUser.password == passwordInput) {
                    // Local login success
                    sessionManager.saveToken("local_token_${localUser.id}", localUser.id)
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true, userAccount = localUser) }
                    fetchAccount()
                    
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val token = task.result
                            repository.updateUserFcmToken(localUser.id, token)
                        }
                    }
                    onSuccess()
                } else {
                    // Fallback to DummyJSON
                    viewModelScope.launch {
                        try {
                            val resolvedUsername = if (emailInput.contains("@")) {
                                try {
                                    val filterResponse = ApiClient.dummyJsonApi.filterUsers(key = "email", value = emailInput)
                                    if (filterResponse.users.isNotEmpty()) {
                                        filterResponse.users.first().username
                                    } else {
                                        emailInput
                                    }
                                } catch (_: Exception) {
                                    emailInput
                                }
                            } else {
                                emailInput
                            }

                            val response = ApiClient.dummyJsonApi.login(
                                LoginRequest(username = resolvedUsername, password = passwordInput)
                            )
                            sessionManager.saveToken(response.accessToken, response.id.toString())
                            
                            val newUser = User(
                                id = response.id.toString(),
                                name = "${response.firstName} ${response.lastName}",
                                email = response.email,
                                profileUrl = response.image
                            )

                            _uiState.update { it.copy(
                                isLoading = false,
                                isLoggedIn = true,
                                userAccount = newUser
                            ) }

                            repository.checkAndCreateUser(newUser) {
                                fetchAccount()
                                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val token = task.result
                                        repository.updateUserFcmToken(response.id.toString(), token)
                                    }
                                }
                                onSuccess()
                            }
                        } catch (_: Exception) {
                            _uiState.update { it.copy(
                                isLoading = false,
                                loginError = "Email/Username atau Password Salah! (Gagal login ke server)"
                            ) }
                        }
                    }
                }
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        sessionManager.clearSession()
        _uiState.update { it.copy(isLoggedIn = false, userAccount = null) }
        onLogout()
    }

    fun register(name: String, email: String, phone: String, passwordInput: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Simulating API call for registration
            val newUserId = System.currentTimeMillis().toString()
            val newUser = User(
                id = newUserId,
                name = name,
                email = email,
                phone = phone,
                password = passwordInput,
                address = "",
                favoriteWorkers = emptyList(),
                settings = UserSettings()
            )
            repository.updateUserProfile(newUser) { _ ->
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
