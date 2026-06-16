package com.example.tamtamduku.presentation.tracking.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.domain.model.Transaction
import com.example.tamtamduku.domain.model.TransactionGroup
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.example.tamtamduku.domain.model.TransactionData
import com.example.tamtamduku.domain.model.Tracking
import com.example.tamtamduku.domain.model.Receipt

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
                        profileUrl = worker?.profileUrl ?: "",
                        tracking = tx.tracking,
                        icon = if (tx.status == "Dikerjakan") "Construction" else "Work",
                        cancellationReason = tx.cancellationReason
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

    fun cancelTransaction(transactionId: String, reason: String) {
        viewModelScope.launch {
            repository.updateTransactionStatus(
                transactionId = transactionId,
                newStatus = "Batal",
                cancellationReason = reason,
                onSuccess = {
                    // Locally update the state
                    _uiState.update { currentState ->
                        val updatedTransactions = currentState.transactions.map {
                            if (it.id == transactionId) {
                                it.copy(
                                    status = "Batal",
                                    tracking = it.tracking?.copy(posisiSaatIni = "Batal"),
                                    cancellationReason = reason
                                )
                            } else {
                                it
                            }
                        }
                        currentState.copy(
                            transactions = updatedTransactions,
                            transactionGroups = deriveTransactionGroups(updatedTransactions)
                        )
                    }
                },
                onFailure = {
                    it.printStackTrace()
                }
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun processSuccessfulPayment(workerName: String, layanan: String, paymentMethod: String, price: Double, invoiceNumber: String, tanggal: String, jam: String) {
        viewModelScope.launch {
            val workers = repository.getWorkers().firstOrNull() ?: emptyList()
            val worker = workers.find { it.nama.equals(workerName, ignoreCase = true) }
            val workerId = worker?.id ?: ""

            val dateStr = if (tanggal.isNotBlank() && tanggal != " ") tanggal else {
                val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
                dateFormat.format(Date())
            }
            val timeStr = if (jam.isNotBlank() && jam != " ") jam else {
                val timeFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))
                "${timeFormat.format(Date())} WIB"
            }

            val transactionData = TransactionData(
                invoiceNumber = invoiceNumber,
                userId = "usr_8a7b6c5d", // Dummy logged-in user
                workerId = workerId,
                status = "Dikerjakan",
                date = dateStr,
                price = price,
                tracking = Tracking(
                    estimasiWaktu = "-",
                    posisiSaatIni = "Menunggu Konfirmasi",
                    iconType = "Wait"
                )
            )

            val receipt = Receipt(
                invoiceNumber = invoiceNumber,
                userId = "usr_8a7b6c5d",
                workerId = workerId,
                workerName = workerName,
                layanan = layanan,
                paymentMethod = paymentMethod,
                date = dateStr,
                time = timeStr,
                totalAmount = price
            )

            repository.createTransaction(
                transaction = transactionData,
                onSuccess = {
                    repository.createReceipt(
                        receipt = receipt,
                        onSuccess = {
                            // Successfully saved both
                        },
                        onFailure = {
                            it.printStackTrace()
                        }
                    )
                },
                onFailure = {
                    it.printStackTrace()
                }
            )
        }
    }
}
