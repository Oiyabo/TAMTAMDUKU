package com.example.tamtamduku.presentation.search
import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R
import androidx.compose.material3.MaterialTheme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import com.example.tamtamduku.presentation.search.viewmodels.WorkerViewModel
import com.example.tamtamduku.presentation.search.components.*
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
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


