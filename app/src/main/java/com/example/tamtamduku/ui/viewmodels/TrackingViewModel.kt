package com.example.tamtamduku.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.model.TrackingPekerjaan
import com.example.tamtamduku.data.model.TransactionGroup
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TrackingUiState(
    val isLoading: Boolean = false,
    val trackingItems: List<TrackingPekerjaan> = emptyList(),
    val transactionGroups: List<TransactionGroup> = emptyList(),
    val errorMessage: String? = null
)

class TrackingViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackingUiState())
    val uiState: StateFlow<TrackingUiState> = _uiState.asStateFlow()

    init {
        fetchTrackingData()
        fetchTransactionData()
    }

    fun fetchTrackingData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getTrackingPekerjaan().collect { items ->
                _uiState.update { it.copy(
                    isLoading = false,
                    trackingItems = items
                ) }
            }
        }
    }

    fun fetchTransactionData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getTransactions().collect { groups ->
                _uiState.update { it.copy(
                    isLoading = false,
                    transactionGroups = groups
                ) }
            }
        }
    }

    fun markAsSelesai(workerName: String) {
        _uiState.update { state ->
            state.copy(
                trackingItems = state.trackingItems.map {
                    if (it.workerName == workerName) it.copy(status = "Selesai") else it
                }
            )
        }
    }
}
