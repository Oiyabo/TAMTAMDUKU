package com.example.tamtamduku.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.model.Review
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReviewUiState(
    val isLoading: Boolean = false,
    val reviews: List<Review> = emptyList(),
    val error: String? = null
)

class ReviewViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    init {
        fetchReviews()
    }

    fun fetchReviews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getReviews().collect { reviews ->
                _uiState.update { it.copy(
                    isLoading = false,
                    reviews = reviews
                ) }
            }
        }
    }
}
