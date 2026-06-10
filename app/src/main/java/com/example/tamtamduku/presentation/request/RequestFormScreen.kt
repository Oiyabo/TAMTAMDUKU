package com.example.tamtamduku.presentation.request

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tamtamduku.R
import com.example.tamtamduku.core.components.FormClickableField
import com.example.tamtamduku.core.components.FormDropdownField
import com.example.tamtamduku.core.components.FormInputField
import com.example.tamtamduku.core.components.FormTextAreaField
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
            Text(stringResource(R.string.pekerja_tidak_ditemukan))
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
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { setShowDatePicker(false) }) { Text("Batal") }
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
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { setShowTimePicker(false) }) { Text("Batal") }
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
            title = { Text("Pilih Lokasi", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
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
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                                    color = Color.Black,
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
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A00))
                ) {
                    Text("Tutup", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            },
            containerColor = Color(0xFFFFFDF8)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.detail_permintaan), fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navCon.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp),
                color = Color.Transparent
            ) {
                Button(
                    onClick = {
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
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(R.string.kirim_permintaan), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.background)
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Worker Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp, top = 8.dp)
            ) {
                AsyncImage(
                    model = worker.profileUrl.ifEmpty { "https://i.pravatar.cc/150?u=${worker.nama}" },
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(stringResource(R.string.pekerja), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                    Text(worker.nama, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                }
            }

            val kategoriOptions = worker.kategori.ifEmpty { listOf("Layanan Umum") }
            FormDropdownField(
                icon = Icons.Outlined.WorkOutline, 
                placeholder = "Kategori", 
                value = kategori, 
                options = kategoriOptions,
                onValueChange = setKategori
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)
            
            val layananOptions = worker.layanan.map { it.namaLayanan } + "Custom"
            FormDropdownField(
                icon = Icons.Outlined.WorkOutline, 
                placeholder = "Layanan", 
                value = layanan, 
                options = layananOptions,
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
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)

            if (layanan == "Custom") {
                FormInputField(
                    icon = Icons.Outlined.WorkOutline,
                    placeholder = "Masukkan Harga",
                    value = customHarga,
                    onValueChange = setCustomHarga,
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)
            }
            
            FormClickableField(
                icon = Icons.Outlined.LocationOn, 
                placeholder = "Lokasi", 
                value = lokasi, 
                onClick = { setShowLocationPicker(true) },
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)

            Row(modifier = Modifier.fillMaxWidth()) {
                FormClickableField(
                    icon = Icons.Outlined.CalendarMonth,
                    placeholder = "Tanggal",
                    value = tanggal,
                    onClick = { setShowDatePicker(true) },
                    modifier = Modifier.weight(1.5f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                FormClickableField(
                    icon = Icons.Outlined.Schedule,
                    placeholder = "Jam",
                    value = jam,
                    onClick = { setShowTimePicker(true) },
                    modifier = Modifier.weight(1f),
                    showIcon = false // Optional: hide icon to save space, but let's see if we can keep it
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)

            FormTextAreaField(
                icon = Icons.Outlined.NoteAlt, 
                placeholder = "Catatan", 
                value = catatan, 
                onValueChange = setCatatan
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)
        }
    }
}


