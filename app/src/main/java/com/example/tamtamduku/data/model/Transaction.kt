package com.example.tamtamduku.data.model

data class Transaction(
    val invoiceNumber: String,
    val workerName: String,
    val workerProfession: String,
    val date: String,
    val time: String = "10:00",
    val price: Double,
    val status: String, // "Selesai", "Dibatalkan", "Dikerjakan"
    val icon: String = "Work",
    val iconColor: String = "#FF7A00",
    val iconBgColor: String = "#FFF4E5"
)
