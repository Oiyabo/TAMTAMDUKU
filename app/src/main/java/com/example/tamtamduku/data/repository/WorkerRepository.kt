package com.example.tamtamduku.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tamtamduku.data.model.VocaWorker
import com.example.tamtamduku.data.model.VocaChat
import com.example.tamtamduku.data.model.TrackingPekerjaan
import com.example.tamtamduku.data.model.UserAccount
import com.example.tamtamduku.data.model.TransactionGroup
import com.example.tamtamduku.data.remote.RetrofitClient
import com.example.tamtamduku.data.remote.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WorkerRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getWorkers(): Flow<List<VocaWorker>> = flow {
        val domainWorkers = try {
            val response = RetrofitClient.apiService.getWorkers()
            response.map { it.toDomainModel() }
        } catch (_: Exception) {
            emptyList()
        }
        emit(domainWorkers)
    }

    fun getChats(): Flow<List<VocaChat>> = flow {
        val chats = try {
            val response = RetrofitClient.chatApiService.getChat()
            response.chats
        } catch (_: Exception) {
            emptyList()
        }
        emit(chats)
    }

    fun getTrackingPekerjaan(): Flow<List<TrackingPekerjaan>> = flow {
        val domainTracking = try {
            val response = RetrofitClient.apiService.getTrackingPekerjaan()
            response.map { it.toDomainModel() }
        } catch (_: Exception) {
            emptyList()
        }
        emit(domainTracking)
    }

    fun getUserAccount(): Flow<UserAccount?> = flow {
        val account = try {
            RetrofitClient.apiService.getUserAccount()
        } catch (_: Exception) {
            null
        }
        emit(account)
    }

    fun getTransactions(): Flow<List<TransactionGroup>> = flow {
        val domainTransactions = try {
            val response = RetrofitClient.apiService.getTransactions()
            response.map { it.toDomainModel() }
        } catch (_: Exception) {
            emptyList()
        }
        emit(domainTransactions)
    }
}
