package com.example.tamtamduku.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tamtamduku.data.model.VocaWorker
import com.example.tamtamduku.data.model.VocaChat
import com.example.tamtamduku.data.model.TrackingPekerjaan
import com.example.tamtamduku.data.model.UserAccount
import com.example.tamtamduku.data.remote.RetrofitClient
import com.example.tamtamduku.data.remote.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WorkerRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getWorkers(): Flow<List<VocaWorker>> = flow {
        try {
            val response = RetrofitClient.apiService.getWorkers()
            val domainWorkers = response.map { it.toDomainModel() }
            emit(domainWorkers)
        } catch (_: Exception) {
            emit(emptyList())
        }
    }

    fun getChats(): Flow<List<VocaChat>> = flow {
        try {
            val response = RetrofitClient.chatApiService.getChat()
            emit(response.chats)
        } catch (_: Exception) {
            emit(emptyList())
        }
    }

    fun getTrackingPekerjaan(): Flow<List<TrackingPekerjaan>> = flow {
        try {
            val response = RetrofitClient.apiService.getTrackingPekerjaan()
            val domainTracking = response.map { it.toDomainModel() }
            emit(domainTracking)
        } catch (_: Exception) {
            emit(emptyList())
        }
    }

    fun getUserAccount(): Flow<UserAccount?> = flow {
        try {
            val response = RetrofitClient.apiService.getUserAccount()
            emit(response)
        } catch (_: Exception) {
            emit(null)
        }
    }
}
