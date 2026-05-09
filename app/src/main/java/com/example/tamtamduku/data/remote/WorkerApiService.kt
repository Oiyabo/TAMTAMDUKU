package com.example.tamtamduku.data.remote

import retrofit2.http.GET

interface WorkerApiService {
    @GET("Oiyabo/1ddd47fed8ecd8b5d753c6fc02d54122/raw/")
    suspend fun getWorkers(): List<WorkerDto>
}
