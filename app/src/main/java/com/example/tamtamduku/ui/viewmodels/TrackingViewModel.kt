package com.example.tamtamduku.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.model.Transaction
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TrackingUiState(
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val errorMessage: String? = null
)

class TrackingViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackingUiState())
    val uiState: StateFlow<TrackingUiState> = _uiState.asStateFlow()

    init {
        fetchTransactionData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchTransactionData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getWorkers().collect { workers ->
                val statuses = listOf("Dikerjakan", "Selesai", "Batal")
                
                // Generate mocked transactions based on workers
                val mockTransactions = workers.mapIndexed { index, worker ->
                    Transaction(
                        invoiceNumber = "#INV-25050${index + 1}",
                        workerName = worker.nama,
                        workerProfession = worker.pekerjaan,
                        date = "3 Mei 2025",
                        price = worker.baseSalary,
                        status = statuses[index % statuses.size]
                    )
                }

                _uiState.update { it.copy(
                    isLoading = false,
                    transactions = mockTransactions
                ) }
            }
        }
    }

    fun getTransactionByInvoice(invoiceId: String): Transaction? {
        return _uiState.value.transactions.find { it.invoiceNumber == invoiceId }
    }

    fun addTransaction(transaction: Transaction) {
        _uiState.update { it.copy(transactions = listOf(transaction) + it.transactions) }
    }
}
