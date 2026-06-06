package com.example.tamtamduku.data.model

data class Transaction(
    val invoiceNumber: String,
    val workerName: String,
    val workerProfession: String,
    val date: String,
    val price: Double,
    val status: String // "Selesai", "Dibatalkan", "Dikerjakan"
)
