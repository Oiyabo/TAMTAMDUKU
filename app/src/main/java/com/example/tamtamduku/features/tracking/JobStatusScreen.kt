package com.example.tamtamduku.features.tracking

import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R
import androidx.compose.material3.MaterialTheme

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobStatusScreen(
    navCon: NavHostController,
    workerName: String,
    viewModel: TrackingViewModel
) {
    val bgColor = MaterialTheme.colorScheme.background
    val primaryOrange = Color(0xFFFF8C00)

    val uiState by viewModel.uiState.collectAsState()
    val item = uiState.transactions.find { it.workerName == workerName && it.status == "Dikerjakan" } ?: uiState.transactions.find { it.workerName == workerName }
    val isSelesai = item?.status == "Selesai"
    val isBatal = item?.status == stringResource(R.string.batal)
    val showCancelDialog = remember { mutableStateOf(false) }
    val selectedReasonIndex = remember { mutableIntStateOf(-1) }
    val customReasonText = remember { mutableStateOf("") }
    val cancellationReasons = listOf(
        "Kesepakatan jadwal tidak dapat dipenuhi",
        "Menemukan penyedia jasa lain yang lebih cocok",
        "Pekerja tidak merespon atau tidak datang ke lokasi",
        "Perubahan rencana / tidak lagi membutuhkan jasa",
        stringResource(R.string.lainnya)
    )

    if (showCancelDialog.value) {
        AlertDialog(
            onDismissRequest = { 
                showCancelDialog.value = false 
                selectedReasonIndex.intValue = -1
                customReasonText.value = ""
            },
            title = { 
                Text(
                    text = "Pilih Alasan Pembatalan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
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
                                .clickable { selectedReasonIndex.intValue = index }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedReasonIndex.intValue == index),
                                onClick = { selectedReasonIndex.intValue = index },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = primaryOrange
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = reason,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    if (selectedReasonIndex.intValue == 4) {
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = customReasonText.value,
                            onValueChange = { customReasonText.value = it },
                            placeholder = { Text(stringResource(R.string.tulis_alasan_anda_di_sini), fontSize = 14.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryOrange,
                                cursorColor = primaryOrange,
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 3
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedReasonIndex.intValue != -1) {
                            val reason = if (selectedReasonIndex.intValue == 4) {
                                customReasonText.value.trim()
                            } else {
                                cancellationReasons[selectedReasonIndex.intValue]
                            }
                            item?.id?.let { viewModel.cancelTransaction(it, reason) }
                            showCancelDialog.value = false
                            selectedReasonIndex.intValue = -1
                            customReasonText.value = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B4B)),
                    shape = RoundedCornerShape(50),
                    enabled = selectedReasonIndex.intValue != -1 && (selectedReasonIndex.intValue != 4 || customReasonText.value.trim().isNotEmpty())
                ) {
                    Text(stringResource(R.string.ya_batalkan), color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showCancelDialog.value = false 
                        selectedReasonIndex.intValue = -1
                        customReasonText.value = ""
                    }
                ) {
                    Text(stringResource(R.string.batal), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.status_pekerjaan), 
                            fontWeight = FontWeight.ExtraBold, 
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navCon.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            }
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
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = if (isSelesai) Color(0xFFE8F5E9) else if (isBatal) Color(0xFFFFEBEE) else Color(0xFFFFF3E0),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = if (isSelesai) "Selesai" else if (isBatal) "Dibatalkan" else item?.tracking?.posisiSaatIni ?: "Sedang Dikerjakan",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = if (isSelesai) Color(0xFF2E7D32) else if (isBatal) Color(0xFFC62828) else Color(0xFFE65100)
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
                    time = item?.date ?: "-",
                    isCompleted = true,
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
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(50))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(if (isSelesai || isBatal) 1f else (statusLevel / 6f))
                                .height(10.dp)
                                .background(if (isBatal) Color(0xFFFF4B4B) else primaryOrange, RoundedCornerShape(50))
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = if (isSelesai || isBatal) "100%" else "${(statusLevel * 100) / 6}%",
                        color = if (isBatal) Color(0xFFFF4B4B) else primaryOrange,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Worker Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = item?.profileUrl?.ifEmpty { "https://i.pravatar.cc/150?u=${item.workerName}" } ?: "https://i.pravatar.cc/150?u=$workerName",
                        contentDescription = "Worker Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFDE8E0))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = workerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = item?.workerProfession ?: "",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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
                        showCancelDialog.value = true
                    } else {
                        navCon.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(bottom = 0.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelesai || isBatal) primaryOrange else Color(0xFFFF4B4B)
                ),
                enabled = true
            ) {
                Text(
                    text = if (isSelesai || isBatal) stringResource(R.string.tutup) else "Batalkan Jasa",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TimelineItem(title: String, time: String, isCompleted: Boolean, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(if (isCompleted) Color(0xFFFF8C00) else MaterialTheme.colorScheme.outlineVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color.White, CircleShape)
                    )
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(if (isCompleted) Color(0xFFFF8C00) else MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 16.dp)) {
            Text(
                text = title, 
                fontWeight = FontWeight.Bold, 
                fontSize = 14.sp,
                color = if (isCompleted) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (time != "-") {
                Text(text = time, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }
        }
    }
}
