package com.example.tamtamduku.domain.model

data class Report(
    val id: String = "",
    val category: String = "",
    val description: String = "",
    val date: String = "",
    val status: String = "",
    val imageUrl: String? = null
)
