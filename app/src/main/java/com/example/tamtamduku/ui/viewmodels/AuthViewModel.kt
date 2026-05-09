package com.example.tamtamduku.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun login(onSuccess: () -> Unit) {
        // TODO: Implement login logic
        onSuccess()
    }

    fun register(onSuccess: () -> Unit) {
        // TODO: Implement register logic
        onSuccess()
    }
}
