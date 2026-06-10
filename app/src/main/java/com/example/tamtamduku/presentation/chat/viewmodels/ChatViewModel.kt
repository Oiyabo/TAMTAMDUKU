package com.example.tamtamduku.presentation.chat.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.domain.model.ChatList
import com.example.tamtamduku.domain.model.ChatMessage
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatUiItem(
    val id: String,
    val roomId: String,
    val name: String,
    val lastMessage: String,
    val unreadCount: Int,
    val time: String,
    val workerId: String
)

@RequiresApi(Build.VERSION_CODES.O)
class ChatViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {

    private val _chats = MutableStateFlow<List<ChatUiItem>>(emptyList())
    val chats: StateFlow<List<ChatUiItem>> = _chats.asStateFlow()

    private val _chatMessages = MutableStateFlow<Map<String, List<ChatMessage>>>(emptyMap())
    val chatMessages: StateFlow<Map<String, List<ChatMessage>>> = _chatMessages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchChats()
    }

    private fun fetchChats() {
        viewModelScope.launch {
            _isLoading.value = true
            combine(
                repository.getChatLists(),
                repository.getWorkers(),
                repository.getChatRooms()
            ) { chatLists, workers, chatRooms ->
                _chatMessages.value = chatRooms
                
                chatLists.map { chatList ->
                    val worker = workers.find { it.id == chatList.workerId }
                    ChatUiItem(
                        id = chatList.id,
                        roomId = chatList.roomId,
                        name = worker?.nama ?: "Unknown Worker",
                        lastMessage = chatList.lastMessage,
                        unreadCount = chatList.unreadCount,
                        time = chatList.lastUpdated,
                        workerId = chatList.workerId
                    )
                }
            }.collect { mappedChats ->
                _chats.value = mappedChats
                _isLoading.value = false
            }
        }
    }

    fun getMessagesForRoom(roomId: String): List<ChatMessage> {
        return _chatMessages.value[roomId] ?: emptyList()
    }

    fun getChatByName(name: String): ChatUiItem? {
        return _chats.value.find { it.name == name }
    }

    fun markAsRead(workerName: String) {
        _chats.value = _chats.value.map {
            if (it.name == workerName) it.copy(unreadCount = 0) else it
        }
    }

    fun sendMessage(workerName: String, text: String, time: String = "Now") {
        val chatList = _chats.value.find { it.name == workerName }
        if (chatList != null) {
            val roomId = chatList.roomId
            val currentRooms = _chatMessages.value.toMutableMap()
            val currentMessages = currentRooms[roomId]?.toMutableList() ?: mutableListOf()
            currentMessages.add(ChatMessage(id = System.currentTimeMillis().toString(), senderId = "usr_8a7b6c5d", text = text, time = time))
            currentRooms[roomId] = currentMessages
            _chatMessages.value = currentRooms
            
            _chats.value = _chats.value.map {
                if (it.roomId == roomId) it.copy(lastMessage = text, time = time) else it
            }
            
            // Persist to Firebase
            repository.sendMessage(roomId, chatList.id, text, time)
        } else {
            // New chat
            val roomId = "room_${System.currentTimeMillis()}"
            val newMessage = ChatMessage(id = System.currentTimeMillis().toString(), senderId = "usr_8a7b6c5d", text = text, time = time)
            val currentRooms = _chatMessages.value.toMutableMap()
            currentRooms[roomId] = listOf(newMessage)
            _chatMessages.value = currentRooms

            val newChatId = System.currentTimeMillis().toString()
            val newChat = ChatUiItem(
                id = newChatId,
                roomId = roomId,
                name = workerName,
                lastMessage = text,
                unreadCount = 0,
                time = time,
                workerId = ""
            )
            _chats.value = _chats.value + newChat
            
            // Persist to Firebase
            repository.sendMessage(roomId, newChatId, text, time)
        }
    }
}
