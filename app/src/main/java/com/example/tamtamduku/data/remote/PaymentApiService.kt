package com.example.tamtamduku.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface PaymentApiService {
    @POST
    suspend fun getSnapToken(
        @Url url: String,
        @Body request: PaymentRequest
    ): PaymentResponse
}
