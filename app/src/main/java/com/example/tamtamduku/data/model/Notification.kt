package com.example.tamtamduku.data.model

data class Notification(
    val id: String = "",
    val targetUserId: String = "", // "all" for broadcast
    val title: String = "",
    val body: String = "",
    val type: String = "", // "promo", "transaction", dll.
    val isRead: Boolean = false,
    val createdAt: String = ""
)
