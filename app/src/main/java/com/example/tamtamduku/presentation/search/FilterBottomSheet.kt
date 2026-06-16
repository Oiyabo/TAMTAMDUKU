package com.example.tamtamduku.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tamtamduku.R
import com.example.tamtamduku.presentation.search.components.CustomFilterDropdown
import com.example.tamtamduku.presentation.search.components.CustomKeywordInput
import com.example.tamtamduku.presentation.search.components.CustomRangeSliderSection
import com.example.tamtamduku.presentation.search.viewmodels.SearchUiState
import com.example.tamtamduku.presentation.search.viewmodels.WorkerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheetContent(
    uiState: SearchUiState,
    viewModel: WorkerViewModel,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF9F5))
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.filter),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = viewModel::onResetFilter,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B4B)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(stringResource(R.string.reset_all), color = MaterialTheme.colorScheme.background, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Dropdowns & Inputs
        var workTypeExpanded by remember { mutableStateOf(false) }
        CustomFilterDropdown(
            label = stringResource(R.string.tipe_pekerjaan),
            selectedValue = uiState.selectedWorkType,
            placeholder = "(Tutoring, programming)",
            expanded = workTypeExpanded,
            onExpandedChange = { workTypeExpanded = it },
            items = uiState.workTypes.map { it to it },
            onItemSelected = viewModel::onWorkTypeChange
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        var locationExpanded by remember { mutableStateOf(false) }
        CustomFilterDropdown(
            label = stringResource(R.string.lokasi),
            selectedValue = uiState.selectedLocation,
            placeholder = "(Jakarta, Surabaya)",
            expanded = locationExpanded,
            onExpandedChange = { locationExpanded = it },
            items = uiState.locations.map { it to it },
            onItemSelected = viewModel::onLocationChange
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Keyword Input (Skill)
        CustomKeywordInput(
            value = uiState.skillInput,
            onValueChange = viewModel::onSkillInputChange,
            onAdd = viewModel::onAddSkill
        )
        
        if (uiState.skills.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), 
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Gaji Range: Max 10 Juta, Step 50 Ribu (10.000.000 / 50.000 = 200 segments, so 199 steps)
        CustomRangeSliderSection(
            title = stringResource(R.string.gaji),
            minValueStr = uiState.minGaji,
            maxValueStr = uiState.maxGaji,
            onMinChange = viewModel::onMinGajiChange,
            onMaxChange = viewModel::onMaxGajiChange,
            minPlaceholder = "Min (Rp)",
            maxPlaceholder = "Max (Rp)",
            valueRange = 0f..10000000f,
            steps = 199
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Rating Range
        CustomRangeSliderSection(
            title = stringResource(R.string.rating),
            minValueStr = uiState.minRate,
            maxValueStr = uiState.maxRate,
            onMinChange = viewModel::onMinRateChange,
            onMaxChange = viewModel::onMaxRateChange,
            minPlaceholder = "Min (1-5)",
            maxPlaceholder = "Max (1-5)",
            valueRange = 1f..5f
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Apply Button
        Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = stringResource(R.string.terapkan),
                color = MaterialTheme.colorScheme.background,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}


