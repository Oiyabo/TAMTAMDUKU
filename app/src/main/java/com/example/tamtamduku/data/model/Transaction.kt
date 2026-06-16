package com.example.tamtamduku.data.model

data class TransactionData(
    val id: String = "",
    val invoiceNumber: String = "",
    val userId: String = "",
    val workerId: String = "",
    val status: String = "", // "Menunggu Konfirmasi", "Dikerjakan", "Menuju Lokasi", "Selesai", "Dibatalkan"
    val date: String = "",
    val price: Double = 0.0,
    val tracking: Tracking? = null,
    val cancellationReason: String = ""
)

data class Transaction(
    val id: String = "",
    val invoiceNumber: String = "",
    val workerId: String = "",
    val workerName: String = "",
    val workerProfession: String = "",
    val status: String = "",
    val date: String = "",
    val price: Double = 0.0,
    val profileUrl: String = "",
    val tracking: Tracking? = null,
    val icon: String = "Work",
    val iconColor: String = "#FF7A00",
    val iconBgColor: String = "#FFF4E5",
    val cancellationReason: String = ""
)

data class Tracking(
    val estimasiWaktu: String = "",
    val posisiSaatIni: String = "",
    val iconType: String = ""
)

data class Receipt(
    val id: String = "",
    val invoiceNumber: String = "",
    val userId: String = "",
    val workerId: String = "",
    val workerName: String = "",
    val layanan: String = "",
    val paymentMethod: String = "",
    val date: String = "",
    val time: String = "",
    val totalAmount: Double = 0.0
)
