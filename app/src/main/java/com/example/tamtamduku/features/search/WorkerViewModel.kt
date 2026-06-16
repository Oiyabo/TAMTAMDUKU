package com.example.tamtamduku.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.repository.WorkerRepository
import com.example.tamtamduku.domain.model.WorkerReview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToLong

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
                val workTypes = listOf("Semua Pekerjaan") + allWorkers.map { it.pekerjaan }.distinct().sorted()
                val locations = listOf("Semua Lokasi") + allWorkers.map { it.lokasi }.distinct().sorted()
                val userLocations = user?.let {
                    val list = mutableListOf<String>()
                    if (it.address.isNotBlank()) list.add(it.address)
                    list.addAll(it.locations)
                    if (list.isEmpty()) listOf("Rumah - Jl. Mawar No. 1", "Kantor - Jl. Sudirman No. 10") else list.distinct()
                } ?: listOf("Rumah - Jl. Mawar No. 1", "Kantor - Jl. Sudirman No. 10")

                _uiState.update { it.copy(
                    isLoading = false,
                    workers = allWorkers,
                    filteredWorkers = allWorkers,
                    workTypes = workTypes,
                    locations = locations,
                    favoriteWorkerIds = favoriteIds,
                    userLocations = userLocations
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

    fun toggleFavorite(workerId: String) {
        viewModelScope.launch {
            val users = repository.getUsers().firstOrNull()
            val user = users?.firstOrNull()
            if (user != null) {
                val isFav = user.favoriteWorkers.contains(workerId)
                if (isFav) {
                    repository.removeFavoriteWorker(user.id, workerId)
                } else {
                    repository.addFavoriteWorker(user.id, workerId)
                }
            }
        }
    }

    fun submitReview(workerId: String, rating: Int, comment: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val users = repository.getUsers().firstOrNull()
            val user = users?.firstOrNull()
            val worker = _uiState.value.workers.find { it.id == workerId }
            
            if (user != null && worker != null) {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = sdf.format(Date())
                
                val review = WorkerReview(
                    id = "rev_${System.currentTimeMillis()}",
                    userId = user.id,
                    username = user.name.ifEmpty { "User" },
                    rating = rating,
                    comment = comment,
                    date = currentDate
                )
                
                val currentSummary = worker.reviewSummary
                val newTotalReviews = currentSummary.totalReviews + 1
                val newAverageRating = ((currentSummary.averageRating * currentSummary.totalReviews) + rating) / newTotalReviews
                
                // Format average rating to 1 decimal place max (e.g., 4.5)
                val roundedAverageRating = (newAverageRating * 10.0).roundToLong() / 10.0
                
                repository.submitWorkerReview(workerId, review, roundedAverageRating, newTotalReviews, onComplete)
            } else {
                onComplete(false)
            }
        }
    }
}
