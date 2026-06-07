package com.example.tamtamduku.util

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return format.format(amount).replace("Rp", "Rp ")
}
