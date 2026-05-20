package com.example.tamtamduku.data.remote

import com.example.tamtamduku.data.model.Transaction
import com.example.tamtamduku.data.model.TransactionGroup
import com.google.gson.annotations.SerializedName

data class TransactionGroupDto(
    @SerializedName("date")
    val date: String,
    @SerializedName("items")
    val items: List<TransactionDto>
)

data class TransactionDto(
    @SerializedName("workerName")
    val workerName: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("iconBgColor")
    val iconBgColor: String,
    @SerializedName("iconColor")
    val iconColor: String
)

fun TransactionGroupDto.toDomainModel(): TransactionGroup {
    return TransactionGroup(
        date = date,
        items = items.map { it.toDomainModel() }
    )
}

fun TransactionDto.toDomainModel(): Transaction {
    return Transaction(
        workerName = workerName,
        date = date,
        time = time,
        status = status,
        icon = icon,
        iconBgColor = iconBgColor,
        iconColor = iconColor
    )
}
