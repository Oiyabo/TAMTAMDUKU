package com.example.tamtamduku.data.model

import java.util.UUID

data class UserAddress(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val fullAddress: String = "",
    val isDefault: Boolean = false
)

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val addressList: List<UserAddress> = emptyList(),
    val favoriteWorkers: List<String> = emptyList(),
    val settings: UserSettings = UserSettings()
)

data class UserSettings(
    val pushNotification: Boolean = true
)
