package com.example.tamtamduku.data.remote

import com.google.gson.annotations.SerializedName

data class PaymentRequest(
    val orderId: String,
    val grossAmount: Int,
    val userId: String? = null
)

data class PaymentResponse(
    val token: String,
    @SerializedName("redirect_url")
    val redirectUrl: String? = null
)
