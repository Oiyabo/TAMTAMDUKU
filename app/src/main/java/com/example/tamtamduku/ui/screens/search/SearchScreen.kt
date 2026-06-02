package com.example.tamtamduku.ui.screens.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tamtamduku.ui.components.CompactField
import com.example.tamtamduku.ui.components.FilterDropdown
import com.example.tamtamduku.ui.components.WorkerCard
import com.example.tamtamduku.ui.viewmodels.WorkerViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(
    viewModel: WorkerViewModel = viewModel(),
    onBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showFilterSheet by remember { mutableStateOf(false) }

    Surface(color = Color(0xFFFFF9F5)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.Black)
                    }
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = viewModel::onSearchQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        placeholder = { Text("Search", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    IconButton(
                        onClick = { showFilterSheet = true },
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filter",
                            tint = Color.Black
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.filteredWorkers) { worker ->
                        WorkerCard(worker = worker, onClick = { onNavigateToDetail(worker.nama) })
                    }
                    if (uiState.filteredWorkers.isEmpty() && !uiState.isLoading) {
                        item {
                            Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                Text("Tidak ada hasil yang cocok", color = MaterialTheme.colorScheme.outline)
                            }
                        }
                    }
                }
            }

            if (viewModel.isAnyFilterActive()) {
                ExtendedFloatingActionButton(
                    onClick = viewModel::onResetFilter,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    icon = { Icon(Icons.Default.Close, null) },
                    text = { Text("Reset Filter") }
                )
            }
        }

        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                FilterSheetContent(
                    uiState = uiState,
                    viewModel = viewModel,
                    onDismiss = { showFilterSheet = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterSheetContent(
    uiState: SearchUiState,
    viewModel: WorkerViewModel,
    onDismiss: () -> Unit
) {
    val showDatePicker = remember { mutableStateOf(false) }

    if (showDatePicker.value) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.selectedDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onDateChange(datePickerState.selectedDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    })
                    showDatePicker.value = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker.value = false }) { Text("Cancel") } }
        ) { DatePicker(state = datePickerState) }
    }

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text("Filter Pencarian", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            TextButton(onClick = viewModel::onResetFilter) { Text("Reset All") }
        }

        var workTypeExpanded by remember { mutableStateOf(false) }
        FilterDropdown(
            label = "Tipe Pekerjaan",
            selectedValue = uiState.selectedWorkType,
            expanded = workTypeExpanded,
            onExpandedChange = { workTypeExpanded = it },
            items = uiState.workTypes.map { it to it },
            onItemSelected = viewModel::onWorkTypeChange,
            onValueChange = viewModel::onWorkTypeChange
        )

        var locationExpanded by remember { mutableStateOf(false) }
        FilterDropdown(
            label = "Lokasi",
            selectedValue = uiState.selectedLocation,
            expanded = locationExpanded,
            onExpandedChange = { locationExpanded = it },
            items = uiState.locations.map { it to it },
            onItemSelected = viewModel::onLocationChange,
            onValueChange = viewModel::onLocationChange
        )

        CompactField(
            value = uiState.selectedDate?.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) ?: "",
            onValueChange = {},
            label = "Bekerja dari",
            readOnly = true,
            trailingIcon = { IconButton(onClick = { showDatePicker.value = true }) { Icon(Icons.Default.DateRange, null) } },
            modifier = Modifier.clickable { showDatePicker.value = true }
        )

        CompactField(
            value = uiState.skillInput,
            onValueChange = viewModel::onSkillInputChange,
            label = "Skill",
            placeholder = "Tambah skill...",
            trailingIcon = { IconButton(onClick = viewModel::onAddSkill) { Icon(Icons.Default.Add, null) } },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { viewModel.onAddSkill() })
        )

        if (uiState.skills.isNotEmpty()) {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), Arrangement.spacedBy(8.dp)) {
                uiState.skills.forEach { skill ->
                    InputChip(
                        selected = true,
                        onClick = { viewModel.onRemoveSkill(skill) },
                        label = { Text(skill) },
                        trailingIcon = { Icon(Icons.Default.Close, null, Modifier.size(16.dp)) }
                    )
                }
            }
        }

        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(16.dp)) {
            CompactField(
                value = uiState.minGaji,
                onValueChange = viewModel::onMinGajiChange,
                label = "Min Gaji",
                modifier = Modifier.weight(1f),
                prefix = { Text("Rp ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            CompactField(
                value = uiState.maxGaji,
                onValueChange = viewModel::onMaxGajiChange,
                label = "Max Gaji",
                modifier = Modifier.weight(1f),
                prefix = { Text("Rp ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }

        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(16.dp)) {
            CompactField(
                value = uiState.minRate,
                onValueChange = viewModel::onMinRateChange,
                label = "Min Rating",
                modifier = Modifier.weight(1f),
                leadingIcon = { Icon(Icons.Default.Star, null, Modifier.size(18.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            CompactField(
                value = uiState.maxRate,
                onValueChange = viewModel::onMaxRateChange,
                label = "Max Rating",
                modifier = Modifier.weight(1f),
                leadingIcon = { Icon(Icons.Default.Star, null, Modifier.size(18.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onDismiss() })
            )
        }

        Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            Text("Terapkan Filter")
        }
        Spacer(Modifier.height(24.dp))
    }
}
