package com.example.tamtamduku.domain.model

data class ChatList(
    val id: String = "",
    val roomId: String = "",
    val workerId: String = "",
    val userId: String = "",
    val lastMessage: String = "",
    val unreadCount: Int = 0,
    val lastUpdated: String = ""
)

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val time: String = ""
)
