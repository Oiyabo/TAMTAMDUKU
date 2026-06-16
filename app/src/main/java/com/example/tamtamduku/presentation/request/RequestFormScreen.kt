package com.example.tamtamduku.presentation.request

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tamtamduku.R
import com.example.tamtamduku.presentation.search.viewmodels.WorkerViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestFormScreen(
    navCon: NavHostController,
    viewModel: WorkerViewModel,
    workerName: String?
) {
    val uiState by viewModel.uiState.collectAsState()
    val worker = uiState.workers.find { it.nama.equals(workerName, ignoreCase = true) }
    val context = LocalContext.current

    val (kategori, setKategori) = remember { mutableStateOf("") }
    val (layanan, setLayanan) = remember { mutableStateOf("") }
    val (customHarga, setCustomHarga) = remember { mutableStateOf("") }
    val (selectedHarga, setSelectedHarga) = remember { mutableDoubleStateOf(0.0) }

    val (lokasi, setLokasi) = remember { mutableStateOf("") }
    val (tanggal, setTanggal) = remember { mutableStateOf("") }
    val (jam, setJam) = remember { mutableStateOf("") }
    val (catatan, setCatatan) = remember { mutableStateOf("") }

    val (showDatePicker, setShowDatePicker) = remember { mutableStateOf(false) }
    val (showTimePicker, setShowTimePicker) = remember { mutableStateOf(false) }
    val (showLocationPicker, setShowLocationPicker) = remember { mutableStateOf(false) }

    if (worker == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.pekerja_tidak_ditemukan), color = Color.Black)
        }
        return
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { setShowDatePicker(false) },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        setTanggal(sdf.format(Date(it)))
                    }
                    setShowDatePicker(false)
                }) {
                    Text("OK", color = Color(0xFFFF8C00))
                }
            },
            dismissButton = {
                TextButton(onClick = { setShowDatePicker(false) }) { Text("Batal", color = Color.Gray) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()
        AlertDialog(
            onDismissRequest = { setShowTimePicker(false) },
            confirmButton = {
                TextButton(onClick = {
                    val hour = timePickerState.hour.toString().padStart(2, '0')
                    val minute = timePickerState.minute.toString().padStart(2, '0')
                    setJam("$hour:$minute")
                    setShowTimePicker(false)
                }) {
                    Text("OK", color = Color(0xFFFF8C00))
                }
            },
            dismissButton = {
                TextButton(onClick = { setShowTimePicker(false) }) { Text("Batal", color = Color.Gray) }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    if (showLocationPicker) {
        val locationOptions = uiState.userLocations.ifEmpty { listOf("Lokasi Saya") }
        AlertDialog(
            onDismissRequest = { setShowLocationPicker(false) },
            title = { Text("Pilih Lokasi", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    locationOptions.forEachIndexed { index, loc ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    setLokasi(loc)
                                    setShowLocationPicker(false)
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Alamat ${index + 1}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = loc,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { setShowLocationPicker(false) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00))
                ) {
                    Text("Tutup", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            },
            containerColor = Color.White
        )
    }

    val bgColor = Color(0xFFFAF9F6)
    val primaryOrange = Color(0xFFFF8C00)

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.detail_permintaan),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navCon.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        if (kategori.isBlank() || layanan.isBlank() || tanggal.isBlank() || jam.isBlank()) {
                            Toast.makeText(context, "Kategori, Layanan, Tanggal, dan Jam wajib diisi", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val encodedWorkerName = Uri.encode(worker.nama)
                        val encodedLayanan = Uri.encode(layanan.ifEmpty { " " })
                        val encodedLokasi = Uri.encode(lokasi.ifEmpty { " " })
                        val encodedTanggal = Uri.encode(tanggal.ifEmpty { " " })
                        val encodedJam = Uri.encode(jam.ifEmpty { " " })
                        val encodedCatatan = Uri.encode(catatan.ifEmpty { " " })
                        val encodedKategori = Uri.encode(kategori.ifEmpty { " " })
                        val hargaToPass = if (layanan == "Custom") customHarga.ifEmpty { "0" } else selectedHarga.toLong().toString()
                        val encodedHarga = Uri.encode(hargaToPass)

                        navCon.navigate("request_confirmation/$encodedWorkerName/$encodedLayanan/$encodedLokasi/$encodedTanggal/$encodedJam/$encodedCatatan/$encodedKategori/$encodedHarga")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryOrange)
                ) {
                    Text(
                        text = stringResource(R.string.kirim_permintaan),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        },
        containerColor = bgColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Pekerja Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    AsyncImage(
                        model = worker.profileUrl.ifEmpty { "https://i.pravatar.cc/150?u=${worker.nama}" },
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.pekerja),
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = worker.nama,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = worker.pekerjaan,
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form Fields
            val kategoriOptions = worker.kategori.ifEmpty { listOf("Layanan Umum") }
            PremiumFormField(
                icon = Icons.Outlined.WorkOutline,
                placeholder = "Pilih Kategori",
                value = kategori,
                isDropdown = true,
                dropdownOptions = kategoriOptions,
                onValueChange = setKategori
            )

            Spacer(modifier = Modifier.height(16.dp))

            val layananOptions = worker.layanan.map { it.namaLayanan } + "Custom"
            PremiumFormField(
                icon = Icons.Outlined.WorkOutline,
                placeholder = "Pilih Layanan",
                value = layanan,
                isDropdown = true,
                dropdownOptions = layananOptions,
                onValueChange = { selectedLayanan ->
                    setLayanan(selectedLayanan)
                    val foundLayanan = worker.layanan.find { it.namaLayanan == selectedLayanan }
                    if (foundLayanan != null) {
                        setSelectedHarga(foundLayanan.harga)
                    } else if (selectedLayanan == "Custom") {
                        setSelectedHarga(0.0)
                    }
                }
            )

            if (layanan == "Custom") {
                Spacer(modifier = Modifier.height(16.dp))
                PremiumFormField(
                    icon = Icons.Outlined.WorkOutline,
                    placeholder = "Masukkan Harga Kustom",
                    value = customHarga,
                    onValueChange = setCustomHarga,
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            PremiumFormField(
                icon = Icons.Outlined.LocationOn,
                placeholder = "Pilih Lokasi Kerja",
                value = lokasi,
                onClick = { setShowLocationPicker(true) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                PremiumFormField(
                    icon = Icons.Outlined.CalendarMonth,
                    placeholder = "Tanggal",
                    value = tanggal,
                    onClick = { setShowDatePicker(true) },
                    modifier = Modifier.weight(1.3f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                PremiumFormField(
                    icon = Icons.Outlined.Schedule,
                    placeholder = "Jam",
                    value = jam,
                    onClick = { setShowTimePicker(true) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            PremiumFormField(
                icon = Icons.Outlined.NoteAlt,
                placeholder = "Tulis Catatan Pekerjaan...",
                value = catatan,
                onValueChange = setCatatan,
                isTextArea = true
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumFormField(
    icon: ImageVector,
    placeholder: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onValueChange: ((String) -> Unit)? = null,
    isDropdown: Boolean = false,
    dropdownOptions: List<String> = emptyList(),
    isTextArea: Boolean = false,
    readOnly: Boolean = false,
    keyboardType: androidx.compose.ui.text.input.KeyboardType = androidx.compose.ui.text.input.KeyboardType.Text
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = if (isTextArea) Alignment.Top else Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        // Left Icon Box
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFF2EC)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = placeholder,
                tint = Color(0xFF4A3B32),
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Right Input Field
        Box(modifier = Modifier.weight(1f)) {
            if (isDropdown) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text(placeholder, color = Color.Gray, fontSize = 14.sp) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = Color(0xFFFF8C00),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        dropdownOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    onValueChange?.invoke(option)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            } else {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange ?: {},
                    readOnly = readOnly || (onClick != null),
                    placeholder = { Text(placeholder, color = Color.Gray, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (isTextArea) 120.dp else 56.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = Color(0xFFFF8C00),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = !isTextArea,
                    maxLines = if (isTextArea) 5 else 1,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType)
                )

                if (onClick != null) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { onClick() }
                    )
                }
            }
        }
    }
}
