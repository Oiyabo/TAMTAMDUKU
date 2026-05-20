package com.example.tamtamduku.data.model

data class Transaction(
    val workerName: String,
    val date: String,
    val time: String,
    val status: String,
    val icon: String,
    val iconBgColor: String,
    val iconColor: String
)

data class TransactionGroup(
    val date: String,
    val items: List<Transaction>
)
