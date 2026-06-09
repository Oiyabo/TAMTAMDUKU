package com.example.tamtamduku.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.tamtamduku.data.model.Report
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportViewModel : ViewModel() {
    private val _reports = MutableStateFlow<List<Report>>(listOf(
        Report(id = "ID RE #0001", category = "Pekerja", description = "Datang Terlambat", date = "23 April 2026", status = "Selesai")
    ))
    val reports: StateFlow<List<Report>> = _reports.asStateFlow()

    fun addReport(category: String, description: String) {
        val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())
        val newId = "ID RE #${String.format("%04d", _reports.value.size + 1)}"
        val newReport = Report(
            id = newId,
            category = category,
            description = description,
            date = currentDate,
            status = "Menunggu"
        )
        _reports.value = _reports.value + listOf(newReport)
    }
}
