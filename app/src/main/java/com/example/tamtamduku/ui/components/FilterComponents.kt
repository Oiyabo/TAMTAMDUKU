package com.example.tamtamduku.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CompactField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        modifier = modifier.fillMaxWidth(),
        readOnly = readOnly,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        prefix = prefix,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(
    label: String,
    selectedValue: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<Pair<String, String>>,
    onItemSelected: (String) -> Unit,
    onValueChange: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = onValueChange,
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            readOnly = false,
            singleLine = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(12.dp)
        )
//        val filteredItems = items.filter { it.first.contains(selectedValue, ignoreCase = true) }
        val filteredItems = if (selectedValue == "Semua Pekerjaan" || selectedValue == "Semua Lokasi" || selectedValue.isEmpty()) {
            items
        } else {
            items.filter { it.first.contains(selectedValue, ignoreCase = true) }
        }
        if (filteredItems.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
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