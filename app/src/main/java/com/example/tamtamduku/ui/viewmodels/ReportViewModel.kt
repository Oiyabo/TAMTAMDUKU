package com.example.tamtamduku.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.model.Report
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {
    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getReports().collect {
                _reports.value = it
            }
        }
    }

    fun addReport(category: String, description: String) {
        val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())
        val newId = "ID RE #${System.currentTimeMillis().toString().takeLast(4)}"
        val newReport = Report(
            id = newId,
            category = category,
            description = description,
            date = currentDate,
            status = "Menunggu"
        )
        // Sync to Firebase
        repository.addReport(newReport)
    }
}
