package com.example.tamtamduku.domain.model

data class TransactionGroup(
    val date: String,
    val items: List<Transaction>
)
