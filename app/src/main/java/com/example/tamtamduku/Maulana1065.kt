package com.example.tamtamduku

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import model.NWGroup
import java.time.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterSearchScreen(nav: NavHostController) {
    val saved = nav.previousBackStackEntry?.savedStateHandle
    var minGaji by remember { mutableStateOf(saved?.get<Double?>("minGaji")?.toString() ?: "") }
    var maxGaji by remember { mutableStateOf(saved?.get<Double?>("maxGaji")?.toString() ?: "") }
    var minRating by remember { mutableStateOf(saved?.get<Double?>("minRating")?.toString() ?: "") }
    var maxRating by remember { mutableStateOf(saved?.get<Double?>("maxRating")?.toString() ?: "") }
    var namaQuery by remember { mutableStateOf(saved?.get<String>("nama") ?: "") }
    var selectedWorkType by remember { mutableStateOf(saved?.get<String>("workType") ?: "") }
    var selectedLocation by remember { mutableStateOf(saved?.get<String>("lokasi") ?: "") }
    var selectedDate by remember { mutableStateOf(saved?.get<Long?>("joinDate")?.let { LocalDate.ofEpochDay(it) }) }
    var skills by remember { mutableStateOf(saved?.get<List<String>>("skills") ?: listOf()) }
    var skillInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var workTypeExpanded by remember { mutableStateOf(false) }
    var locationExpanded by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = selectedDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli())
        DatePickerDialog(onDismissRequest = { showDatePicker = false },
            confirmButton = { TextButton(onClick = { selectedDate = state.selectedDateMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate() }; showDatePicker = false }) { Text("OK") } },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = state) }
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
            Text("Filter Pencarian", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            CompactField(namaQuery, { namaQuery = it }, "Nama Pekerja", placeholder = "Contoh: Budi")
            FilterDropdown("Tipe Pekerjaan", selectedWorkType, workTypeExpanded, { workTypeExpanded = it }, listOf("Semua Tipe" to "") + NWGroup.WorkType.map { it to it }) { selectedWorkType = it; workTypeExpanded = false }
            FilterDropdown("Lokasi", selectedLocation, locationExpanded, { locationExpanded = it }, listOf("Semua Lokasi" to "") + NWGroup.IndonesianCities.map { it to it }) { selectedLocation = it; locationExpanded = false }
            CompactField(selectedDate?.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) ?: "", {}, "Bekerja dari", readOnly = true, trailingIcon = { IconButton(onClick = { showDatePicker = true }) { Icon(Icons.Default.DateRange, null) } }, modifier = Modifier.clickable { showDatePicker = true })
            CompactField(skillInput, { skillInput = it }, "Skill", placeholder = "Tambah skill...", trailingIcon = { IconButton(onClick = { if (skillInput.isNotBlank()) { val t = skillInput.trim(); if (!skills.contains(t)) skills = skills + t; skillInput = "" } }) { Icon(Icons.Default.Add, null) } }, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), keyboardActions = KeyboardActions(onDone = { if (skillInput.isNotBlank()) { val t = skillInput.trim(); if (!skills.contains(t)) skills = skills + t; skillInput = "" } }))
            if (skills.isNotEmpty()) Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), Arrangement.spacedBy(8.dp)) { skills.forEach { skill -> InputChip(selected = true, onClick = { skills = skills - skill }, label = { Text(skill) }, trailingIcon = { Icon(Icons.Default.Close, null, Modifier.size(16.dp)) }) } }
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(16.dp)) {
                CompactField(minGaji, { if (it.isEmpty() || it.toDoubleOrNull() != null || it == ".") { minGaji = it; errorMessage = "" } }, "Min Gaji", Modifier.weight(1f), prefix = { Text("Rp ") }, keyboardType = KeyboardType.Decimal)
                CompactField(maxGaji, { if (it.isEmpty() || it.toDoubleOrNull() != null || it == ".") { maxGaji = it; errorMessage = "" } }, "Max Gaji", Modifier.weight(1f), prefix = { Text("Rp ") }, keyboardType = KeyboardType.Decimal)
            }
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(16.dp)) {
                CompactField(minRating, { val r = it.toDoubleOrNull(); if (it.isEmpty() || (r != null && r in 0.0..5.0) || it == ".") { minRating = it; errorMessage = "" } }, "Min Rating", Modifier.weight(1f), leadingIcon = { Icon(Icons.Default.Star, null, Modifier.size(18.dp)) }, keyboardType = KeyboardType.Decimal)
                CompactField(maxRating, { val r = it.toDoubleOrNull(); if (it.isEmpty() || (r != null && r in 0.0..5.0) || it == ".") { maxRating = it; errorMessage = "" } }, "Max Rating", Modifier.weight(1f), leadingIcon = { Icon(Icons.Default.Star, null, Modifier.size(18.dp)) }, keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done)
            }
            if (errorMessage.isNotEmpty()) Text(errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            Button(onClick = {
                val minG = minGaji.toDoubleOrNull(); val maxG = maxGaji.toDoubleOrNull(); val minR = minRating.toDoubleOrNull(); val maxR = maxRating.toDoubleOrNull()
                if (minG != null && maxG != null && minG > maxG) { errorMessage = "Min Gaji > Max Gaji"; return@Button }
                if (minR != null && maxR != null && minR > maxR) { errorMessage = "Min Rating > Max Rating"; return@Button }
                saved?.apply { set("nama", namaQuery); set("workType", selectedWorkType); set("lokasi", selectedLocation); set("joinDate", selectedDate?.toEpochDay()); set("skills", skills); set("minGaji", minG); set("maxGaji", maxG); set("minRating", minR); set("maxRating", maxR) }
                nav.popBackStack()
            }, Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp)) { Text("Terapkan Filter", style = MaterialTheme.typography.titleMedium) }
        }
    }
}

@Composable
fun CompactField(value: String, onValueChange: (String) -> Unit, label: String, modifier: Modifier = Modifier, placeholder: String? = null, readOnly: Boolean = false, trailingIcon: @Composable (() -> Unit)? = null, leadingIcon: @Composable (() -> Unit)? = null, prefix: @Composable (() -> Unit)? = null, keyboardType: KeyboardType = KeyboardType.Text, keyboardOptions: KeyboardOptions? = null, imeAction: ImeAction = ImeAction.Next, keyboardActions: KeyboardActions = KeyboardActions.Default) {
    OutlinedTextField(value = value, onValueChange = onValueChange, label = { Text(label) }, placeholder = if (placeholder != null) { { Text(placeholder) } } else null, modifier = modifier.fillMaxWidth(), readOnly = readOnly, singleLine = true, shape = RoundedCornerShape(12.dp), trailingIcon = trailingIcon, leadingIcon = leadingIcon, prefix = prefix, keyboardOptions = keyboardOptions ?: KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction), keyboardActions = keyboardActions)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(label: String, sel: String, exp: Boolean, onExp: (Boolean) -> Unit, items: List<Pair<String, String>>, onSel: (String) -> Unit) {
    ExposedDropdownMenuBox(expanded = exp, onExpandedChange = onExp) {
        OutlinedTextField(value = sel, onValueChange = {}, modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(), readOnly = true, label = { Text(label) }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = exp) }, shape = RoundedCornerShape(12.dp))
        ExposedDropdownMenu(expanded = exp, onDismissRequest = { onExp(false) }) { items.forEach { (t, v) -> DropdownMenuItem(text = { Text(t) }, onClick = { onSel(v) }) } }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(navCon: NavHostController) {
    val saved = navCon.currentBackStackEntry?.savedStateHandle
    val fN by saved?.getStateFlow("nama", "")?.collectAsState() ?: remember { mutableStateOf("") }
    val fW by saved?.getStateFlow("workType", "")?.collectAsState() ?: remember { mutableStateOf("") }
    val fL by saved?.getStateFlow("lokasi", "")?.collectAsState() ?: remember { mutableStateOf("") }
    val fD by saved?.getStateFlow<Long?>("joinDate", null)?.collectAsState() ?: remember { mutableStateOf(null) }
    val fS by saved?.getStateFlow("skills", listOf<String>())?.collectAsState() ?: remember { mutableStateOf(listOf()) }
    val minG by saved?.getStateFlow<Double?>("minGaji", null)?.collectAsState() ?: remember { mutableStateOf(null) }
    val maxG by saved?.getStateFlow<Double?>("maxGaji", null)?.collectAsState() ?: remember { mutableStateOf(null) }
    val minR by saved?.getStateFlow<Double?>("minRating", null)?.collectAsState() ?: remember { mutableStateOf(null) }
    val maxR by saved?.getStateFlow<Double?>("maxRating", null)?.collectAsState() ?: remember { mutableStateOf(null) }

    var searchText by remember { mutableStateOf("") }
    LaunchedEffect(fN) { searchText = fN }
    val isF = fN.isNotEmpty() || fW.isNotEmpty() || fL.isNotEmpty() || fD != null || fS.isNotEmpty() || minG != null || maxG != null || minR != null || maxR != null

    Surface(color = MaterialTheme.colorScheme.background) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navCon.navigate("home") { popUpTo(navCon.graph.startDestinationId); launchSingleTop = true; restoreState = true } }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.primary) }
                    OutlinedTextField(searchText, { searchText = it }, Modifier.weight(1f), placeholder = { Text("Cari nama...") }, singleLine = true, leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary) }, shape = RoundedCornerShape(24.dp))
                    IconButton(onClick = { saved?.set("nama", searchText); navCon.navigate("search/filterSearch") }) { Icon(Icons.Default.Info, null, tint = MaterialTheme.colorScheme.primary) }
                }
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val filtered = NWGroup.NWG.filter { w ->
                        w.nama.contains(searchText, true) && (fW.isEmpty() || w.workType.contains(fW)) && (fL.isEmpty() || w.lokasi.equals(fL, true)) &&
                        (fD == null || !w.joinDate.isBefore(LocalDate.ofEpochDay(fD!!))) && (fS.isEmpty() || w.skills.any { it in fS }) &&
                        (minG == null || w.baseSalary >= minG!!) && (maxG == null || w.baseSalary <= maxG!!) && (minR == null || w.rating >= minR!!) && (maxR == null || w.rating <= maxR!!)
                    }
                    items(filtered) { WorkerCard(it) }
                    if (filtered.isEmpty()) item { Box(Modifier.fillMaxWidth().padding(32.dp), Alignment.Center) { Text("Tidak ada hasil", color = MaterialTheme.colorScheme.outline) } }
                }
            }
            if (isF) ExtendedFloatingActionButton(
                text = { Text("Clear") },
                icon = { Icon(Icons.Default.Close, null) },
                onClick = { saved?.apply { set("nama", ""); set("workType", ""); set("lokasi", ""); set("joinDate", null); set("skills", emptyList<String>()); set("minGaji", null); set("maxGaji", null); set("minRating", null); set("maxRating", null) } },
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            )
        }
    }
}

@Composable
fun WorkerCard(w: model.NovaWorker) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text(w.nama, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, Modifier.size(16.dp), MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(4.dp))
                    Text(w.rating.toString(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(w.deskripsi, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, Modifier.size(14.dp), MaterialTheme.colorScheme.outline)
                Spacer(Modifier.width(4.dp))
                Text(w.lokasi, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@Composable
fun HomeScreen(nav: NavHostController) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(Modifier.fillMaxSize().padding(24.dp), Arrangement.Center, Alignment.CenterHorizontally) {
            Icon(Icons.Default.Build, null, Modifier.size(120.dp), MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(32.dp))
            Text("TamTamDuku", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            Text("Temukan pekerja profesional terbaik di sekitar Anda.", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(48.dp))
            Button({ nav.navigate("search") }, Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp)) { Text("Mulai Cari Pekerja", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        }
    }
}
