package com.example.tamtamduku.core.util

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.Builder().setLanguage("in").setRegion("ID").build())
    return format.format(amount).replace("Rp", "Rp ")
}

fun formatShortPrice(amount: Double): String {
    val df = java.text.DecimalFormat("#.#")
    return when {
        amount >= 1_000_000_000 -> "${df.format(amount / 1_000_000_000)} milyar"
        amount >= 1_000_000 -> "${df.format(amount / 1_000_000)}jt"
        amount >= 1_000 -> "${df.format(amount / 1_000)}rb"
        else -> amount.toInt().toString()
    }
}
