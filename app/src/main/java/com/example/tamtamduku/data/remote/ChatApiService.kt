package com.example.tamtamduku.data.remote

import com.example.tamtamduku.data.model.ChatResponse
import retrofit2.http.GET

interface ChatApiService {
    @GET("Oiyabo/d92c68b2b17a9a856470cd01b6cc8b83/raw/")
    suspend fun getChat(): ChatResponse
}
