package com.example.tamtamduku.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tamtamduku.data.model.VocaWorker
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
}
