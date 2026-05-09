package com.example.tamtamduku.data.model

data class ChatResponse(
    val chats: List<VocaChat>
)

data class Messages(
    val text: String,
    val isFromMe: Boolean,
    val time: String
)

data class VocaChat(
    val id: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int,
    val messages: List<Messages>
)
