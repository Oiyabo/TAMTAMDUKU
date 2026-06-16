package com.example.tamtamduku.features.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.repository.WorkerRepository
import com.example.tamtamduku.domain.model.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getNotifications().collect { notifs ->
                // Sort descending by created_at time
                val sortedNotifs = notifs.sortedByDescending { it.createdAt }
                _notifications.value = sortedNotifs
                _isLoading.value = false
            }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            repository.markNotificationAsRead(notificationId)
        }
    }
}
