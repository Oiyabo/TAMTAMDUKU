package com.example.tamtamduku.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tamtamduku.data.model.VocaWorker
import com.example.tamtamduku.data.model.VocaChat
import com.example.tamtamduku.data.model.TrackingPekerjaan
import com.example.tamtamduku.data.model.UserAccount
import com.example.tamtamduku.data.model.TransactionGroup
import com.example.tamtamduku.data.model.Review
import com.example.tamtamduku.data.remote.RetrofitClient
import com.example.tamtamduku.data.remote.toDomainModel
import com.example.tamtamduku.data.remote.WorkerDto
import com.example.tamtamduku.data.remote.TrackingPekerjaanDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import com.google.gson.Gson

class WorkerRepository {
    private val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWorkers(): Flow<List<VocaWorker>> = flow {
        val domainWorkers = try {
            val snapshot = db.collection("workers").get().await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    val jobTitle = doc.getString("jobTitle") ?: ""
                    val name = doc.getString("name") ?: ""
                    val workTypes = doc.get("workTypes") as? List<String> ?: emptyList()
                    val description = doc.getString("description") ?: ""
                    val city = doc.getString("city") ?: ""
                    val startDate = doc.getString("startDate") ?: ""
                    val salary = doc.getDouble("salary") ?: 0.0
                    val skills = doc.get("skills") as? List<String> ?: emptyList()
                    val rating = doc.getDouble("rating") ?: 0.0

                    WorkerDto(jobTitle, name, workTypes, description, city, startDate, salary, skills, rating).toDomainModel()
                } catch (e: Exception) {
                    null
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
        emit(domainWorkers)
    }

    fun getChats(): Flow<List<VocaChat>> = flow {
        val chats = try {
            val snapshot = db.collection("chats").get().await()
            val gson = Gson()
            snapshot.documents.mapNotNull { doc ->
                try {
                    val jsonString = gson.toJson(doc.data)
                    gson.fromJson(jsonString, com.example.tamtamduku.data.model.VocaChat::class.java)
                } catch (e: Exception) {
                    null
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
        emit(chats)
    }

    fun getTrackingPekerjaan(): Flow<List<TrackingPekerjaan>> = flow {
        val domainTracking = listOf(
            TrackingPekerjaan(
                workerName = "Bang Jeno",
                date = "21 Mei 2024",
                time = "10:00",
                status = "Sedang Berjalan",
                iconType = "Construction"
            )
        )
        emit(domainTracking)
    }

    fun getUserAccount(): Flow<UserAccount?> = flow {
        val account = try {
            val doc = db.collection("user_account").document("tamtamduku").get().await()
            if (doc.exists()) {
                val nama = doc.getString("nama") ?: ""
                val email = doc.getString("email") ?: ""
                val password = doc.getString("password") ?: ""
                UserAccount(nama, email, password)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
        emit(account)
    }

    fun getTransactions(): Flow<List<TransactionGroup>> = flow {
        val domainTransactions = try {
            val snapshot = db.collection("transactions").get().await()
            val gson = Gson()
            snapshot.documents.mapNotNull { doc ->
                try {
                    val jsonString = gson.toJson(doc.data)
                    gson.fromJson(jsonString, com.example.tamtamduku.data.remote.TransactionGroupDto::class.java).toDomainModel()
                } catch (e: Exception) {
                    null
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
        emit(domainTransactions)
    }

    fun getReviews(): Flow<List<Review>> = flow {
        emit(emptyList<Review>())
    }
}
