package com.example.tamtamduku.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.model.VocaChat
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {

    private val _chats = MutableStateFlow<List<VocaChat>>(emptyList())
    val chats: StateFlow<List<VocaChat>> = _chats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchChats()
    }

    fun fetchChats() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getChats().collect { chatList ->
                _chats.value = chatList
                _isLoading.value = false
            }
        }
    }

    fun getChatById(chatId: String): VocaChat? {
        return _chats.value.find { it.id == chatId }
    }

    fun getChatByName(name: String): VocaChat? {
        return _chats.value.find { it.name == name }
    }

    fun markAsRead(userName: String) {
        _chats.value = _chats.value.map {
            if (it.name == userName) it.copy(unreadCount = 0) else it
        }
    }

    fun sendMessage(userName: String, text: String, time: String = "Now") {
        _chats.value = _chats.value.map { chat ->
            if (chat.name == userName) {
                val newMessage = com.example.tamtamduku.data.model.Messages(text, true, time)
                chat.copy(
                    messages = chat.messages + newMessage,
                    lastMessage = text,
                    time = time
                )
            } else {
                chat
            }
        }
    }
}
