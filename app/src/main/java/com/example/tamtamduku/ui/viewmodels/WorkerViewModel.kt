package com.example.tamtamduku.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.repository.WorkerRepository
import com.example.tamtamduku.ui.screens.search.SearchUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class WorkerViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadWorkers()
    }

    private fun loadWorkers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            combine(repository.getWorkers(), repository.getUsers()) { allWorkers, users ->
                val user = users.firstOrNull()
                val favoriteIds = user?.favoriteWorkers ?: emptyList()
                val workers = allWorkers.take(5)
                val workTypes = listOf("Semua Pekerjaan") + workers.map { it.pekerjaan }.distinct().sorted()
                val locations = listOf("Semua Lokasi") + workers.map { it.lokasi }.distinct().sorted()
                _uiState.update { it.copy(
                    isLoading = false,
                    workers = workers,
                    filteredWorkers = workers,
                    workTypes = workTypes,
                    locations = locations,
                    favoriteWorkerIds = favoriteIds
                ) }
            }.collect()
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onWorkTypeChange(workType: String) {
        _uiState.update { it.copy(selectedWorkType = workType) }
        applyFilters()
    }

    fun onLocationChange(location: String) {
        _uiState.update { it.copy(selectedLocation = location) }
        applyFilters()
    }

    fun onDateChange(date: Long?) {
        _uiState.update { it.copy(selectedDate = date) }
        applyFilters()
    }

    fun onKategoriChange(kategori: String) {
        _uiState.update { it.copy(selectedKategori = kategori) }
        applyFilters()
    }

    fun onMinGajiChange(value: String) {
        _uiState.update { it.copy(minGaji = value) }
        applyFilters()
    }

    fun onMaxGajiChange(value: String) {
        _uiState.update { it.copy(maxGaji = value) }
        applyFilters()
    }

    fun onMinRateChange(value: String) {
        _uiState.update { it.copy(minRate = value) }
        applyFilters()
    }

    fun onMaxRateChange(value: String) {
        _uiState.update { it.copy(maxRate = value) }
        applyFilters()
    }

    fun onSkillInputChange(value: String) {
        _uiState.update { it.copy(skillInput = value) }
    }

    fun onAddSkill() {
        _uiState.update { 
            if (it.skillInput.isNotBlank() && !it.skills.contains(it.skillInput)) {
                it.copy(
                    skills = it.skills + it.skillInput.trim(),
                    skillInput = ""
                )
            } else it
        }
        applyFilters()
    }

    fun onRemoveSkill(skill: String) {
        _uiState.update { it.copy(skills = it.skills - skill) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val filtered = state.workers.filter { worker ->
            val matchesQuery = state.searchQuery.isEmpty() ||
                    worker.nama.contains(state.searchQuery, ignoreCase = true) ||
                    worker.pekerjaan.contains(state.searchQuery, ignoreCase = true)

            val matchesType = state.selectedWorkType == "Semua Pekerjaan" ||
                    worker.pekerjaan == state.selectedWorkType

            val matchesLocation = state.selectedLocation == "Semua Lokasi" ||
                    worker.lokasi == state.selectedLocation

            val matchesDate = if (state.selectedDate != null) {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDateStr = sdf.format(Date(state.selectedDate))
                worker.tanggalGabung >= selectedDateStr
            } else true

            val minGajiVal = state.minGaji.toDoubleOrNull()
            val matchesMinGaji = minGajiVal == null || worker.baseSalary >= minGajiVal

            val maxGajiVal = state.maxGaji.toDoubleOrNull()
            val matchesMaxGaji = maxGajiVal == null || worker.baseSalary <= maxGajiVal

            val minRateVal = state.minRate.toDoubleOrNull()
            val matchesMinRate = minRateVal == null || worker.reviewSummary.averageRating >= minRateVal

            val maxRateVal = state.maxRate.toDoubleOrNull()
            val matchesMaxRate = maxRateVal == null || worker.reviewSummary.averageRating <= maxRateVal
            
            val matchesSkills = state.skills.isEmpty() || state.skills.all { s -> 
                worker.skills.any { ws -> ws.contains(s, ignoreCase = true) }
            }

            val matchesKategori = state.selectedKategori.isEmpty() || 
                worker.kategori.any { k -> k.contains(state.selectedKategori, ignoreCase = true) }

            matchesQuery && matchesType && matchesLocation && matchesDate &&
                    matchesMinGaji && matchesMaxGaji && matchesMinRate && matchesMaxRate && matchesSkills && matchesKategori
        }
        _uiState.update { it.copy(filteredWorkers = filtered) }
    }

    fun onResetFilter() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                selectedWorkType = "Semua Pekerjaan",
                selectedLocation = "Semua Lokasi",
                selectedKategori = "",
                selectedDate = null,
                skills = emptyList(),
                skillInput = "",
                minGaji = "",
                maxGaji = "",
                minRate = "",
                maxRate = "",
                filteredWorkers = it.workers
            )
        }
    }

    fun isAnyFilterActive(): Boolean {
        val s = _uiState.value
        return s.searchQuery.isNotEmpty() ||
                s.selectedWorkType != "Semua Pekerjaan" ||
                s.selectedLocation != "Semua Lokasi" ||
                s.selectedKategori.isNotEmpty() ||
                s.selectedDate != null ||
                s.skills.isNotEmpty() ||
                s.minGaji.isNotEmpty() ||
                s.maxGaji.isNotEmpty() ||
                s.minRate.isNotEmpty() ||
                s.maxRate.isNotEmpty()
    }
}
