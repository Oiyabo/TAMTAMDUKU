package com.example.tamtamduku.presentation.home.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.domain.model.VocaWorker
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val bestWorkers: List<VocaWorker> = emptyList(),
    val categories: List<CategoryItem> = emptyList(),
    val location: String = "Alamat Saya"
)

data class CategoryItem(
    val name: String,
    val iconName: String, // We'll map this to actual icons in the UI
    val workType: String
)

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
        setupCategories()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getWorkers().collect { workers ->
                // Sort by rating and take top 5
                val best = workers.sortedByDescending { it.reviewSummary.averageRating }.take(5)
                _uiState.update { it.copy(
                    isLoading = false,
                    bestWorkers = best
                ) }
            }
        }
    }

    private fun setupCategories() {
        val categories = listOf(
            CategoryItem("DA", "Assessment", "administration"),
            CategoryItem("Listrik", "Bolt", "Heavy Work"),
            CategoryItem("AC", "AcUnit", "Other Job"),
            CategoryItem("Dev", "Code", "Computer"),
            CategoryItem("Engr", "Engineering", "Heavy Work")
        )
        _uiState.update { it.copy(categories = categories) }
    }
}
