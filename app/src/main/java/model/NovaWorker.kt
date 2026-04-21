package model

import java.time.LocalDate

data class NovaWorker (
    val pekerjaan: String,
    val nama: String,
    val workType: List<String>,
    val deskripsi: String,
    val lokasi: String,
    val joinDate: LocalDate,
    val baseSalary: Double,
    val skills: List<String>,
    val rating: Double,
)
