package com.example.tamtamduku.data.model

data class VocaWorker(
    val id: String = "",
    val pekerjaan: String = "",
    val nama: String = "",
    val kategori: List<String> = emptyList(),
    val deskripsi: String = "",
    val lokasi: String = "",
    val tanggalGabung: String = "",
    val baseSalary: Double = 0.0,
    val skills: List<String> = emptyList(),
    val profileUrl: String = "",
    val rating: Double = 0.0,
    val workType: List<String> = emptyList(),
    val joinDate: String = "",
    val layanan: List<WorkerLayanan> = emptyList(),
    val portofolio: List<WorkerPortofolio> = emptyList(),
    val reviewSummary: ReviewSummary = ReviewSummary(),
    val ulasan: List<WorkerReview> = emptyList(),
    val pengalaman: List<WorkerPengalaman> = emptyList()
)

data class WorkerLayanan(
    val id: String = "",
    val namaLayanan: String = "",
    val harga: Double = 0.0
)

data class WorkerPortofolio(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val description: String = ""
)

data class ReviewSummary(
    val averageRating: Double = 0.0,
    val totalReviews: Int = 0
)

data class WorkerReview(
    val id: String = "",
    val userId: String = "",
    val username: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val date: String = ""
)

data class WorkerPengalaman(
    val id: String = "",
    val judul: String = "",
    val tempat: String = "",
    val tahun: String = ""
)
