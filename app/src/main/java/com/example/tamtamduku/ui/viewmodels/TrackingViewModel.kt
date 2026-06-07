package com.example.tamtamduku.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.model.TrackingPekerjaan
import com.example.tamtamduku.data.model.Transaction
import com.example.tamtamduku.data.model.TransactionGroup
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TrackingUiState(
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val trackingItems: List<TrackingPekerjaan> = emptyList(),
    val transactionGroups: List<TransactionGroup> = emptyList(),
    val errorMessage: String? = null
)

class TrackingViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackingUiState())
    val uiState: StateFlow<TrackingUiState> = _uiState.asStateFlow()

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fetchTransactionData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchTransactionData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getWorkers().collect { workers ->
                val statuses = listOf("Dikerjakan", "Selesai", "Batal")
                
                // Generate mocked tracking items
                val mockTracking = listOf(
                    TrackingPekerjaan("Jeno Lee", "20 Mei 2026", "10:00", "Dikerjakan", "Work"),
                    TrackingPekerjaan("Mark Lee", "21 Mei 2026", "14:00", "Selesai", "Construction")
                )

                // Generate mocked transactions based on workers
                val mockTransactions = workers.mapIndexed { index, worker ->
                    Transaction(
                        invoiceNumber = "#INV-25050${index + 1}",
                        workerName = worker.nama,
                        workerProfession = worker.pekerjaan,
                        date = "3 Mei 2025",
                        time = "09:00",
                        price = worker.baseSalary,
                        status = statuses[index % statuses.size],
                        icon = "Work",
                        iconColor = "#FF7A00",
                        iconBgColor = "#FFF4E5"
                    )
                }

                val groups = mockTransactions.groupBy { it.date }.map { (date, items) ->
                    TransactionGroup(date, items)
                }

                _uiState.update { it.copy(
                    isLoading = false,
                    transactions = mockTransactions,
                    trackingItems = mockTracking,
                    transactionGroups = groups
                ) }
            }
        }
    }

    fun getTransactionByInvoice(invoiceId: String): Transaction? {
        return _uiState.value.transactions.find { it.invoiceNumber == invoiceId }
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
