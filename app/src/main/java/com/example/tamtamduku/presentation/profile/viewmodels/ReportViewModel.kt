package com.example.tamtamduku.presentation.profile.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.domain.model.Report
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getReports().collect {
                _reports.value = it
            }
        }
    }

    fun addReport(category: String, description: String, imageUri: Uri?, onComplete: () -> Unit) {
        _isLoading.value = true
        val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())
        val newId = "ID RE #${System.currentTimeMillis().toString().takeLast(4)}"
        
        if (imageUri != null) {
            val imagePath = "report_images/$newId.jpg"
            repository.uploadImageToStorage(imageUri, imagePath) { downloadUrl ->
                val newReport = Report(
                    id = newId,
                    category = category,
                    description = description,
                    date = currentDate,
                    status = "Menunggu",
                    imageUrl = downloadUrl
                )
                repository.addReport(newReport)
                _isLoading.value = false
                onComplete()
            }
        } else {
            val newReport = Report(
                id = newId,
                category = category,
                description = description,
                date = currentDate,
                status = "Menunggu"
            )
            repository.addReport(newReport)
            _isLoading.value = false
            onComplete()
        }
    }
}
