package com.example.tamtamduku.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val favoriteWorkers: List<String> = emptyList(),
    val settings: UserSettings = UserSettings()
)

data class UserSettings(
    val pushNotification: Boolean = true
)
