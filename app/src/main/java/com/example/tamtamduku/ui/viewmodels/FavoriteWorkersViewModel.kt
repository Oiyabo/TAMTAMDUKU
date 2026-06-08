package com.example.tamtamduku.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamtamduku.data.model.VocaWorker
import com.example.tamtamduku.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class FavoriteWorkersViewModel(private val repository: WorkerRepository = WorkerRepository()) : ViewModel() {
    private val _favoriteWorkers = MutableStateFlow<List<VocaWorker>>(emptyList())
    val favoriteWorkers: StateFlow<List<VocaWorker>> = _favoriteWorkers.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadFavoriteWorkers()
    }

    private fun loadFavoriteWorkers() {
        viewModelScope.launch {
            _isLoading.value = true
            combine(repository.getUsers(), repository.getWorkers()) { users, workers ->
                val user = users.firstOrNull()
                val favoriteIds = user?.favoriteWorkers ?: emptyList()
                workers.filter { it.id in favoriteIds }
            }.collect { favWorkers ->
                _favoriteWorkers.value = favWorkers
                _isLoading.value = false
            }
        }
    }
}
