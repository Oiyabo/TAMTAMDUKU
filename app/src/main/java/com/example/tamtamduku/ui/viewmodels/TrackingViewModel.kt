package com.example.tamtamduku.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.model.Transaction
import com.example.tamtamduku.data.model.TransactionGroup
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class TrackingUiState(
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val transactionGroups: List<TransactionGroup> = emptyList(),
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
            
            repository.getTransactions().combine(repository.getWorkers()) { txDataList, workers ->
                txDataList.map { tx ->
                    val worker = workers.find { it.id == tx.workerId }
                    Transaction(
                        id = tx.id,
                        invoiceNumber = tx.invoiceNumber,
                        workerId = tx.workerId,
                        workerName = worker?.nama ?: "Unknown Worker",
                        workerProfession = worker?.pekerjaan ?: "Unknown",
                        status = tx.status,
                        date = tx.date,
                        price = tx.price,
                        tracking = tx.tracking,
                        icon = if (tx.status == "Dikerjakan") "Construction" else "Work"
                    )
                }
            }.collect { mappedTransactions ->
                _uiState.update { it.copy(
                    isLoading = false,
                    transactions = mappedTransactions,
                    transactionGroups = deriveTransactionGroups(mappedTransactions)
                ) }
            }
        }
    }

    fun addTransaction(transaction: Transaction) {
        _uiState.update { currentState ->
            val updatedTransactions = currentState.transactions + transaction
            currentState.copy(
                transactions = updatedTransactions,
                transactionGroups = deriveTransactionGroups(updatedTransactions)
            )
        }
    }

    fun markAsSelesai(workerName: String) {
        _uiState.update { currentState ->
            val updatedTransactions = currentState.transactions.map {
                if (it.workerName == workerName && it.status == "Dikerjakan") {
                    it.copy(status = "Selesai")
                } else {
                    it
                }
            }
            currentState.copy(
                transactions = updatedTransactions,
                transactionGroups = deriveTransactionGroups(updatedTransactions)
            )
        }
    }

    private fun deriveTransactionGroups(transactions: List<Transaction>): List<TransactionGroup> {
        return transactions.groupBy { it.date }.map { (date, items) ->
            TransactionGroup(date, items)
        }
    }

    fun getTransactionByInvoice(invoiceId: String): Transaction? {
        return _uiState.value.transactions.find { it.invoiceNumber == invoiceId }
    }
}
