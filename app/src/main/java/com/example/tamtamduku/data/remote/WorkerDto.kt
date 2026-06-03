package com.example.tamtamduku.data.remote

import com.example.tamtamduku.data.model.VocaWorker

data class WorkerDto(
    val jobTitle: String,
    val name: String,
    val workTypes: List<String>,
    val description: String,
    val city: String,
    val startDate: String,
    val salary: Double,
    val skills: List<String>,
    val rating: Double
)

fun WorkerDto.toDomainModel(): VocaWorker {
    return VocaWorker(
        pekerjaan = jobTitle,
        nama = name,
        workType = workTypes,
        deskripsi = description,
        lokasi = city,
        joinDate = startDate,
        baseSalary = salary,
        skills = skills,
        rating = rating
    )
}
