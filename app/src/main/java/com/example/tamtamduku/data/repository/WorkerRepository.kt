package com.example.tamtamduku.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tamtamduku.data.model.VocaWorker
import com.example.tamtamduku.data.model.VocaChat
import com.example.tamtamduku.data.model.TrackingPekerjaan
import com.example.tamtamduku.data.model.UserAccount

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
        val mockMarketplaceWorkers = listOf(
            VocaWorker("Teknisi AC", "Budi Santoso", listOf("Perbaikan", "Instalasi"), "Berpengalaman lebih dari 5 tahun menangani berbagai merek AC.", "Jakarta", "2021-05-10", 150000.0, listOf("Cuci AC", "Isi Freon", "Bongkar Pasang"), 4.8),
            VocaWorker("Data Analyst", "M. Faris Adithya", listOf("Analisis Data", "Konsultasi Bisnis"), "Membantu Anda menganalisis data untuk pengambilan keputusan strategis perusahaan.", "Jakarta", "2019-02-10", 250000.0, listOf("Python", "SQL", "Tableau"), 4.9),
            VocaWorker("Jasa Kebersihan", "Siti Rahma", listOf("Cleaning Service", "Setrika"), "Siap membuat rumah Anda bersih berkilau dan wangi.", "Surabaya", "2022-01-20", 80000.0, listOf("Sapu & Pel", "Cuci Piring", "Setrika"), 4.9),
            VocaWorker("Montir Listrik", "Bambang Listrik", listOf("Instalasi Listrik", "Servis Elektronik"), "Melayani perbaikan korsleting dan pemasangan panel listrik rumah.", "Yogyakarta", "2019-08-05", 100000.0, listOf("Korsleting", "Pasang Lampu", "Panel Listrik"), 4.7),
            VocaWorker("Web Developer", "Kanemoto", listOf("Pembuatan Website", "Maintenance Web"), "Mengembangkan website perusahaan yang responsif, cepat, dan profesional.", "Bandung", "2020-06-12", 300000.0, listOf("React", "Kotlin", "Firebase"), 4.8)
        )

        val domainWorkers = try {
            val snapshot = db.collection("workers").get().await()
            val list = snapshot.documents.mapNotNull { doc ->
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
            if (list.isEmpty()) mockMarketplaceWorkers else list
        } catch (_: Exception) {
            mockMarketplaceWorkers
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
        val domainTracking = try {
            val snapshot = db.collection("tracking_pekerjaan").get().await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    val workerName = doc.getString("worker_name") ?: ""
                    val date = doc.getString("date") ?: ""
                    val time = doc.getString("time") ?: ""
                    val status = doc.getString("status") ?: ""
                    val iconType = doc.getString("icon_type") ?: ""

                    TrackingPekerjaanDto(workerName, date, time, status, iconType).toDomainModel()
                } catch (e: Exception) {
                    null
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
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



    fun getReviews(): Flow<List<Review>> = flow {
        emit(emptyList<Review>())
    }
}
