package com.example.tamtamduku.ui.screens.request
import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R
import androidx.compose.material3.MaterialTheme

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
    catatan: String,
    kategori: String,
    harga: String
) {
    val uiState by viewModel.uiState.collectAsState()
    val worker = uiState.workers.find { it.nama.equals(workerName, ignoreCase = true) }

    if (worker == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.pekerja_tidak_ditemukan))
        }
        return
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
                        val encodedWorkerName = Uri.encode(workerName ?: "")
                        val encodedLayanan = Uri.encode(layanan.ifBlank { "Pembuatan SIAKAD" })
                        val encodedHarga = Uri.encode(harga.ifBlank { "300000" })
                        navCon.navigate("payment/$encodedWorkerName/$encodedLayanan/$encodedHarga")
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(R.string.lakukan_pembayaran), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.background)
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

            SummaryItem(icon = Icons.Outlined.WorkOutline, label = "Kategori", value = kategori.ifBlank { "Layanan Umum" })
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)

            SummaryItem(icon = Icons.Outlined.WorkOutline, label = stringResource(R.string.layanan), value = layanan.ifBlank { "Pembuatan SIAKAD" })
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)
            
            SummaryItem(icon = Icons.Outlined.LocationOn, label = stringResource(R.string.lokasi), value = lokasi.ifBlank { "Jl Bhayangkara No 10, Bandar Lampung" })
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)

            SummaryItem(icon = Icons.Outlined.CalendarMonth, label = stringResource(R.string.tanggal), value = tanggal.ifBlank { "20 Mei 2025" })
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)

            SummaryItem(icon = Icons.Outlined.Schedule, label = stringResource(R.string.waktu), value = jam.ifBlank { "10.00 -12.00" })
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)

            SummaryItem(icon = Icons.Outlined.NoteAlt, label = stringResource(R.string.catatan), value = catatan.ifBlank { "Analisis dan perancangan (SIAKAD) untuk kebutuhan pengelolaan data mahasiswa, jadwal, nilai, dan administrasi kampus." })
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)

            SummaryItem(icon = Icons.Outlined.LocalOffer, label = stringResource(R.string.estimasi_harga), value = "Rp$harga")
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)
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
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, color = MaterialTheme.colorScheme.secondaryContainer, fontSize = 14.sp)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}
