package com.example.tamtamduku.presentation.tracking
import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R
import androidx.compose.material3.MaterialTheme

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tamtamduku.presentation.tracking.viewmodels.TrackingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusPekerjaanScreen(
    navCon: NavHostController,
    workerName: String,
    viewModel: TrackingViewModel
) {
    val bgColor = MaterialTheme.colorScheme.background
    val primaryOrange = MaterialTheme.colorScheme.primary
    val yellowBadge = Color(0xFFFFC107)

    val uiState by viewModel.uiState.collectAsState()
    val item = uiState.transactions.find { it.workerName == workerName && it.status == "Dikerjakan" } ?: uiState.transactions.find { it.workerName == workerName }
    val isSelesai = item?.status == "Selesai"
    val isBatal = item?.status == "Batal"
    var showCancelDialog by remember { mutableStateOf(false) }
    var selectedReasonIndex by remember { mutableStateOf(-1) }
    var customReasonText by remember { mutableStateOf("") }
    val cancellationReasons = listOf(
        "Kesepakatan jadwal tidak dapat dipenuhi",
        "Menemukan penyedia jasa lain yang lebih cocok",
        "Pekerja tidak merespon atau tidak datang ke lokasi",
        "Perubahan rencana / tidak lagi membutuhkan jasa",
        "Lainnya"
    )

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { 
                showCancelDialog = false 
                selectedReasonIndex = -1
                customReasonText = ""
            },
            title = { 
                Text(
                    text = "Pilih Alasan Pembatalan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ) 
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    cancellationReasons.forEachIndexed { index, reason ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedReasonIndex = index }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedReasonIndex == index),
                                onClick = { selectedReasonIndex = index },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = primaryOrange
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = reason,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    if (selectedReasonIndex == 4) {
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = customReasonText,
                            onValueChange = { customReasonText = it },
                            placeholder = { Text("Tulis alasan Anda di sini...", fontSize = 14.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryOrange,
                                cursorColor = primaryOrange
                            ),
                            maxLines = 3
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedReasonIndex != -1) {
                            val reason = if (selectedReasonIndex == 4) {
                                customReasonText.trim()
                            } else {
                                cancellationReasons[selectedReasonIndex]
                            }
                            item?.id?.let { viewModel.cancelTransaction(it, reason) }
                            showCancelDialog = false
                            selectedReasonIndex = -1
                            customReasonText = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    enabled = selectedReasonIndex != -1 && (selectedReasonIndex != 4 || customReasonText.trim().isNotEmpty())
                ) {
                    Text("Ya, Batalkan", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showCancelDialog = false 
                        selectedReasonIndex = -1
                        customReasonText = ""
                    }
                ) {
                    Text("Batal", color = MaterialTheme.colorScheme.onBackground)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.status_pekerjaan), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navCon.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Box(modifier = Modifier.size(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        },
        containerColor = bgColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = item?.invoiceNumber ?: "-",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = if (isSelesai) Color(0xFFE8F5E9) else if (item?.status == "Batal") Color(0xFFFFEBEE) else yellowBadge,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (isSelesai) "Selesai" else if (item?.status == "Batal") "Dibatalkan" else item?.tracking?.posisiSaatIni ?: "Sedang Dikerjakan",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = if (isSelesai) Color(0xFF4CAF50) else if (item?.status == "Batal") Color(0xFFF44336) else MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Timeline
            val statusLevel = when (item?.tracking?.posisiSaatIni?.lowercase()) {
                "menunggu konfirmasi" -> 1
                "permintaan diterima" -> 2
                "sedang menuju lokasi" -> 3
                "pekerjaan dimulai" -> 4
                "sedang dikerjakan" -> 5
                "selesai" -> 6
                "batal", "dibatalkan" -> 6
                else -> 1
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                TimelineItem(
                    title = "Menunggu Konfirmasi",
                    time = if (statusLevel >= 1) item?.date ?: "-" else "-",
                    isCompleted = statusLevel >= 1,
                    isLast = false
                )
                TimelineItem(
                    title = stringResource(R.string.permintaan_diterima),
                    time = if (statusLevel >= 2) item?.date ?: "-" else "-",
                    isCompleted = statusLevel >= 2,
                    isLast = false
                )
                TimelineItem(
                    title = "Sedang Menuju Lokasi",
                    time = if (statusLevel >= 3) item?.date ?: "-" else "-",
                    isCompleted = statusLevel >= 3,
                    isLast = false
                )
                TimelineItem(
                    title = stringResource(R.string.pekerjaan_dimulai),
                    time = if (statusLevel >= 4) item?.date ?: "-" else "-",
                    isCompleted = statusLevel >= 4,
                    isLast = false
                )
                TimelineItem(
                    title = stringResource(R.string.sedang_dikerjakan),
                    time = if (statusLevel >= 5) item?.date ?: "-" else "-",
                    isCompleted = statusLevel >= 5,
                    isLast = false
                )
                TimelineItem(
                    title = if (isBatal) "Dibatalkan" else stringResource(R.string.selesai),
                    time = if (statusLevel >= 6) item?.date ?: "-" else "-",
                    isCompleted = statusLevel >= 6,
                    isLast = true
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Progress bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.progress_pekerjaan),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(12.dp)
                            .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(50))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(if (isSelesai || isBatal) 1f else (statusLevel / 6f))
                                .height(12.dp)
                                .background(if (isBatal) Color.Red else primaryOrange, RoundedCornerShape(50))
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = if (isSelesai || isBatal) "100%" else "${(statusLevel * 100) / 6}%",
                        color = if (isBatal) Color.Red else primaryOrange,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Worker Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Fake Avatar
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = workerName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = stringResource(R.string.chat),
                        color = primaryOrange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .clickable { navCon.navigate("personal_chat/$workerName") }
                            .padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { 
                    if (!isSelesai && !isBatal) {
                        showCancelDialog = true
                    } else {
                        navCon.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelesai || isBatal) primaryOrange else Color.Red
                ),
                enabled = true
            ) {
                Text(
                    text = if (isSelesai || isBatal) "Tutup" else "Batalkan",
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun TimelineItem(title: String, time: String, isCompleted: Boolean, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(if (isCompleted) Color(0xFF22C55E) else Color(0xFFE5E7EB), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.Remove,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(20.dp)
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(32.dp)
                        .background(Color(0xFFE5E7EB))
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 24.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = time, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
    }
}
