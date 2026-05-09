package com.example.tamtamduku.data.remote

import com.example.tamtamduku.data.model.VocaWorker
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        joinDate = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE),
        baseSalary = salary,
        skills = skills,
        rating = rating
    )
}
