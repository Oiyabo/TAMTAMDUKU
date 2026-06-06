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
        val domainChats = listOf(
            com.example.tamtamduku.data.model.VocaChat(
                id = "1",
                name = "Bang Jeno",
                lastMessage = "Sudah saya Acc, pak. Saya langsung meluncur.",
                time = "20:30",
                unreadCount = 0,
                messages = listOf(
                    com.example.tamtamduku.data.model.Messages("Halo, apakah bapak bisa menerima pekerjan?", true, "20:30"),
                    com.example.tamtamduku.data.model.Messages("Iya, Saya ada waktu Pak.", false, "20:30"),
                    com.example.tamtamduku.data.model.Messages("Baik, Kalau Begitu Pak, Saya akan langsung Pesan", true, "20:30"),
                    com.example.tamtamduku.data.model.Messages("Baik, Pak. Saya tunggu.", false, "20:30"),
                    com.example.tamtamduku.data.model.Messages("Sudah saya Acc, pak. Saya langsung meluncur.", false, "20:30"),
                    com.example.tamtamduku.data.model.Messages("mantap.", true, "20:30")
                )
            ),
            com.example.tamtamduku.data.model.VocaChat(
                id = "2",
                name = "Bang Lijen",
                lastMessage = "Oke, siap laksanakan.",
                time = "14:15",
                unreadCount = 1,
                messages = listOf(com.example.tamtamduku.data.model.Messages("Oke, siap laksanakan.", false, "14:15"))
            ),
            com.example.tamtamduku.data.model.VocaChat(
                id = "3",
                name = "M. Faris Adithya",
                lastMessage = "Siap Pak, Akan Segera Saya Check",
                time = "02:30",
                unreadCount = 0,
                messages = listOf(com.example.tamtamduku.data.model.Messages("Siap Pak, Akan Segera Saya Check", false, "02:30"))
            ),
            com.example.tamtamduku.data.model.VocaChat(
                id = "4",
                name = "Keisha Aurel",
                lastMessage = "Terima kasih banyak!",
                time = "Kemarin",
                unreadCount = 0,
                messages = listOf(com.example.tamtamduku.data.model.Messages("Terima kasih banyak!", false, "Kemarin"))
            ),
            com.example.tamtamduku.data.model.VocaChat(
                id = "5",
                name = "Annisa Syifa",
                lastMessage = "Tolong konfirmasi jadwalnya ya.",
                time = "Senin",
                unreadCount = 2,
                messages = listOf(com.example.tamtamduku.data.model.Messages("Tolong konfirmasi jadwalnya ya.", false, "Senin"))
            ),
            com.example.tamtamduku.data.model.VocaChat(
                id = "6",
                name = "Maulana Ramadhan",
                lastMessage = "Baik pak, saya meluncur.",
                time = "Minggu",
                unreadCount = 0,
                messages = listOf(com.example.tamtamduku.data.model.Messages("Baik pak, saya meluncur.", false, "Minggu"))
            )
        )
        emit(domainChats)
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



    fun getReviews(): Flow<List<Review>> = flow {
        emit(emptyList<Review>())
    }
}
