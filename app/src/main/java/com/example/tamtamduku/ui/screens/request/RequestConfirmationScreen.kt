package com.example.tamtamduku.ui.screens.request

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.net.Uri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tamtamduku.ui.viewmodels.WorkerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestConfirmationScreen(
    navCon: NavHostController,
    viewModel: WorkerViewModel,
    workerName: String?,
    layanan: String,
    lokasi: String,
    tanggal: String,
    jam: String,
    catatan: String
) {
    val uiState by viewModel.uiState.collectAsState()
    val worker = uiState.workers.find { it.nama.equals(workerName, ignoreCase = true) }

    if (worker == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Pekerja tidak ditemukan")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail permintaan", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navCon.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                color = Color.Transparent
            ) {
                Button(
                    onClick = {
                        val encodedWorkerName = Uri.encode(workerName ?: "")
                        val encodedLayanan = Uri.encode(layanan.ifBlank { "Pembuatan SIAKAD" })
                        navCon.navigate("payment/$encodedWorkerName/$encodedLayanan")
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A00))
                ) {
                    Text("Lakukan Pembayaran", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            }
        },
        containerColor = Color.White
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
                    model = "https://i.pravatar.cc/150?u=${worker.nama}",
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Pekerja", color = Color.Gray, fontSize = 14.sp)
                    Text(worker.nama, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                }
            }

            SummaryItem(icon = Icons.Outlined.WorkOutline, label = "Layanan", value = layanan.ifBlank { "Pembuatan SIAKAD" })
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))
            
            SummaryItem(icon = Icons.Outlined.LocationOn, label = "Lokasi", value = lokasi.ifBlank { "Jl Bhayangkara No 10, Bandar Lampung" })
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))

            SummaryItem(icon = Icons.Outlined.CalendarMonth, label = "Tanggal", value = tanggal.ifBlank { "20 Mei 2025" })
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))

            SummaryItem(icon = Icons.Outlined.Schedule, label = "Waktu", value = jam.ifBlank { "10.00 -12.00" })
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))

            SummaryItem(icon = Icons.Outlined.NoteAlt, label = "Catatan :", value = catatan.ifBlank { "Analisis dan perancangan (SIAKAD) untuk kebutuhan pengelolaan data mahasiswa, jadwal, nilai, dan administrasi kampus." })
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))

            SummaryItem(icon = Icons.Outlined.LocalOffer, label = "Estimasi Harga", value = "Rp300.000,00")
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))
        }
    }
}

@Composable
fun SummaryItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFFFF0E5), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = Color(0xFFFF7A00))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, color = Color.DarkGray, fontSize = 14.sp)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
        }
    }
}
