package com.example.tamtamduku.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://gist.githubusercontent.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: WorkerApiService by lazy {
        retrofit.create(WorkerApiService::class.java)
    }

    val chatApiService: ChatApiService by lazy {
        retrofit.create(ChatApiService::class.java)
    }

    val paymentApiService: PaymentApiService by lazy {
        retrofit.create(PaymentApiService::class.java)
    }
}
