package com.example.tamtamduku.ui.screens.search

import com.example.tamtamduku.data.model.VocaWorker

data class SearchUiState(
    val workers: List<VocaWorker> = emptyList(),
    val filteredWorkers: List<VocaWorker> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedWorkType: String = "Semua Pekerjaan",
    val selectedLocation: String = "Semua Lokasi",
    val selectedDate: Long? = null,
    val skills: List<String> = emptyList(),
    val skillInput: String = "",
    val minGaji: String = "",
    val maxGaji: String = "",
    val minRate: String = "",
    val maxRate: String = "",
    val workTypes: List<String> = listOf("Semua Pekerjaan"),
    val locations: List<String> = listOf("Semua Lokasi")
)
