package com.example.tamtamduku.presentation.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tamtamduku.R
import java.util.Locale

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
            label = { Text(label, color = Color.Gray, fontWeight = FontWeight.Medium) },
            placeholder = { Text(placeholder, color = Color.Gray) },
            singleLine = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFFFF8C00),
                    modifier = Modifier.size(20.dp)
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFFFF8C00),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedLabelColor = Color(0xFFFF8C00),
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                errorContainerColor = Color.White
            )
        )
        
        if (filteredItems.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                containerColor = MaterialTheme.colorScheme.background
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
        label = { Text(stringResource(R.string.keyword), color = Color.Gray, fontWeight = FontWeight.Medium) },
        placeholder = { Text(stringResource(R.string.database_instan_delivery), color = Color.Gray) },
        singleLine = true,
        trailingIcon = {
            IconButton(
                onClick = onAdd,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Keyword",
                    tint = Color(0xFFFF8C00),
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onAdd() }),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color(0xFFFF8C00),
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedLabelColor = Color(0xFFFF8C00),
            unfocusedLabelColor = Color.Gray,
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
                inactiveTrackColor = Color(0xFFFDE8E0),
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
                color = MaterialTheme.colorScheme.onBackground,
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
            .height(40.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp)),
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