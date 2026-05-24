package com.example.tamtamduku.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.model.UserAccount
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
    val registrationSuccess: Boolean = false
)

class AuthViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        fetchAccount()
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
        val account = _uiState.value.userAccount
        if (account != null && emailInput == account.email && passwordInput == account.password) {
            _uiState.update { it.copy(loginError = null) }
            onSuccess()
        } else {
            _uiState.update { it.copy(loginError = "Email atau Password Salah!") }
        }
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
