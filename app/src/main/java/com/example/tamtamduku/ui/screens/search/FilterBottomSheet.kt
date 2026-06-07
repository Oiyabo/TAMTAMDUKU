package com.example.tamtamduku.ui.screens.search

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
import com.example.tamtamduku.ui.viewmodels.WorkerViewModel

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
                text = "Filter",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = viewModel::onResetFilter,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B4B)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Reset All", color = Color.White, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Dropdowns & Inputs
        var workTypeExpanded by remember { mutableStateOf(false) }
        CustomFilterDropdown(
            label = "Tipe Pekerjaan",
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
            label = "Lokasi",
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
            title = "Gaji",
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
            title = "Rating",
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
                text = "Terapkan",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFilterDropdown(
    label: String,
    selectedValue: String,
    placeholder: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<Pair<String, String>>,
    onItemSelected: (String) -> Unit
) {
    val currentText = if (selectedValue == "Semua Pekerjaan" || selectedValue == "Semua Lokasi") "" else selectedValue
    val filteredItems = if (currentText.isEmpty()) items else items.filter { it.first.contains(currentText, ignoreCase = true) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            value = currentText,
            onValueChange = { 
                onItemSelected(it)
                onExpandedChange(true)
            },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            readOnly = false,
            label = { Text(label, color = Color.Black, fontWeight = FontWeight.Medium) },
            placeholder = { Text(placeholder, color = Color.Gray) },
            singleLine = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Transparent, CircleShape)
                        .border(1.dp, Color.Black, CircleShape)
                )
            },
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFEFEFEF),
                unfocusedContainerColor = Color(0xFFEFEFEF),
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                errorContainerColor = Color(0xFFEFEFEF)
            )
        )
        
        if (filteredItems.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                containerColor = Color.White
            ) {
                filteredItems.forEach { (text, value) ->
                    DropdownMenuItem(
                        text = { Text(text) },
                        onClick = {
                            onItemSelected(value)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CustomKeywordInput(
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Keyword", color = Color.Black, fontWeight = FontWeight.Medium) },
        placeholder = { Text("(Database, Instan Delivery)", color = Color.Gray) },
        singleLine = true,
        trailingIcon = {
            IconButton(
                onClick = onAdd,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Keyword",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onAdd() }),
        shape = RoundedCornerShape(4.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFEFEFEF),
            unfocusedContainerColor = Color(0xFFEFEFEF),
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomRangeSliderSection(
    title: String,
    minValueStr: String,
    maxValueStr: String,
    onMinChange: (String) -> Unit,
    onMaxChange: (String) -> Unit,
    minPlaceholder: String,
    maxPlaceholder: String,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0
) {
    val sliderMin = minValueStr.toFloatOrNull() ?: valueRange.start
    val sliderMax = maxValueStr.toFloatOrNull() ?: valueRange.endInclusive
    
    val clampedMin = sliderMin.coerceIn(valueRange)
    val clampedMax = sliderMax.coerceIn(valueRange)
    
    val finalMin = clampedMin.coerceAtMost(clampedMax)
    val finalMax = clampedMax.coerceAtLeast(clampedMin)
    
    var sliderPosition by remember(finalMin, finalMax) { mutableStateOf(finalMin..finalMax) }
    
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        RangeSlider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            onValueChangeFinished = {
                val minVal = if (valueRange.endInclusive > 10f) sliderPosition.start.toInt().toString() else String.format(Locale.US, "%.1f", sliderPosition.start)
                val maxVal = if (valueRange.endInclusive > 10f) sliderPosition.endInclusive.toInt().toString() else String.format(Locale.US, "%.1f", sliderPosition.endInclusive)
                onMinChange(minVal)
                onMaxChange(maxVal)
            },
            valueRange = valueRange,
            steps = steps,
            colors = SliderDefaults.colors(
                activeTrackColor = Color(0xFFFF8C00),
                inactiveTrackColor = Color(0xFFFFE0C2),
                thumbColor = Color(0xFFFF8C00),
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            ),
            startThumb = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color(0xFFFF8C00), CircleShape)
                )
            },
            endThumb = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color(0xFFFF8C00), CircleShape)
                )
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val displayMin = if (sliderPosition.start == finalMin) minValueStr else (if (valueRange.endInclusive > 10f) sliderPosition.start.toInt().toString() else String.format(Locale.US, "%.1f", sliderPosition.start))
            val displayMax = if (sliderPosition.endInclusive == finalMax) maxValueStr else (if (valueRange.endInclusive > 10f) sliderPosition.endInclusive.toInt().toString() else String.format(Locale.US, "%.1f", sliderPosition.endInclusive))
            
            CustomNumberInput(
                value = displayMin,
                onValueChange = onMinChange,
                placeholder = minPlaceholder,
                modifier = Modifier.weight(1f)
            )
            
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .width(60.dp),
                textAlign = TextAlign.Center
            )
            
            CustomNumberInput(
                value = displayMax,
                onValueChange = onMaxChange,
                placeholder = maxPlaceholder,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CustomNumberInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .background(Color(0xFFD9D9D9), CircleShape)
            .border(1.dp, Color.Black, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
    }
}
