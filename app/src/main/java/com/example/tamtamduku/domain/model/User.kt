package com.example.tamtamduku.domain.model

import java.util.UUID
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName

data class UserAddress(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val fullAddress: String = "",
    @get:PropertyName("isDefault")
    @set:PropertyName("isDefault")
    @SerializedName("isDefault", alternate = ["default"])
    var isDefault: Boolean = false
)

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val locations: List<String> = emptyList(),
    val addressList: List<UserAddress> = emptyList(),
    val favoriteWorkers: List<String> = emptyList(),
    val settings: UserSettings = UserSettings(),
    val profileUrl: String = ""
)

data class UserSettings(
    val pushNotification: Boolean = true
)
