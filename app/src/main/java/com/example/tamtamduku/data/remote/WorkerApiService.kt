package com.example.tamtamduku.data.remote

import com.example.tamtamduku.data.model.ReviewResponse
import com.example.tamtamduku.data.model.UserAccount
import retrofit2.http.GET

interface WorkerApiService {
    @GET("Oiyabo/1ddd47fed8ecd8b5d753c6fc02d54122/raw/")
    suspend fun getWorkers(): List<WorkerDto>

    @GET("keishaara/90e0232ea3222284acd17e5b2f5cc986/raw/")
    suspend fun getTrackingPekerjaan(): List<TrackingPekerjaanDto>

    @GET("keishaara/7ce4702edb6b028d5653928e77267a25/raw/")
    suspend fun getUserAccount(): UserAccount

    @GET("MFarisAdithya/4a9d7b7f6beb4f1789d5e0a67b3b6215/raw/b335ae19c5d92c691011cc847c875ddc7877fb0d/gistfile1.txt")
    suspend fun getTransactions(): List<TransactionGroupDto>

    @GET("Annisa1019/333919c011e4004c2787e91d5752c00d/raw/") // Placeholder: Replace with actual gist for reviews
    suspend fun getReviews(): ReviewResponse
}
