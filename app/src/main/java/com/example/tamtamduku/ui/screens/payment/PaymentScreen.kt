package com.example.tamtamduku.ui.screens.payment

import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R

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
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onBack: () -> Unit,
    onNavigateToSimulation: (String) -> Unit,
    workerName: String,
    layanan: String,
    harga: String,
    tanggal: String,
    jam: String,
    workerViewModel: com.example.tamtamduku.ui.viewmodels.WorkerViewModel
) {
    val uiState by workerViewModel.uiState.collectAsState()
    val worker = uiState.workers.find { it.nama.equals(workerName, ignoreCase = true) }
    val profileUrl = worker?.profileUrl ?: ""
    var selectedPaymentMethod by remember { mutableStateOf("Qris") }
    
    val context = LocalContext.current
    
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Pembayaran",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFF7A00)
                )
            )
        },
        containerColor = Color(0xFFFFFDF8),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = { onNavigateToSimulation(selectedPaymentMethod) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                ) {
                    Text(stringResource(R.string.bayar_sekarang), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Orange Background extension
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0xFFFF7A00))
            )

            // Detail Pesanan Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-40).dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Document Icon
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color(0xFFFFF0E5), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.ReceiptLong, contentDescription = null, tint = Color(0xFFFF7A00), modifier = Modifier.size(30.dp))
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text("Detail Pesanan", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(layanan, fontSize = 14.sp, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = profileUrl.ifEmpty { "https://i.pravatar.cc/150?u=${workerName}" },
                                contentDescription = "Worker Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(workerName, fontSize = 14.sp, color = Color.Black)
                        }
                    }
                }
            }

            // Rincian Pembayaran Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-24).dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Rincian Pembayaran", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val baseHarga = harga.toDoubleOrNull() ?: 0.0
                    val adminFee = 5000.0
                    val total = baseHarga + adminFee
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Harga Layanan", fontSize = 14.sp, color = Color.DarkGray)
                        Text("Rp${harga}", fontSize = 14.sp, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Biaya Admin", fontSize = 14.sp, color = Color.DarkGray)
                        Text("Rp5000", fontSize = 14.sp, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Diskon", fontSize = 14.sp, color = Color.DarkGray)
                        Text("-Rp0", fontSize = 14.sp, color = Color.Black)
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray)
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Pembayaran", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Rp${total.toLong()}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFFF7A00))
                    }
                }
            }

            // Metode Pembayaran Title
            Text(
                "Metode Pembayaran",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .offset(y = (-8).dp)
            )

            // Metode Pembayaran Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    // Qris Option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPaymentMethod = "Qris" }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPaymentMethod == "Qris",
                            onClick = { selectedPaymentMethod = "Qris" },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF7A00))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Qris", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Bayar dengan Scan Qris", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
                    
                    // Lainnya Option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPaymentMethod = "Lainnya" }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedPaymentMethod == "Lainnya",
                                onClick = { selectedPaymentMethod = "Lainnya" },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF7A00))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Lainnya", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Lihat Pembayaran Lainmu", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        Icon(Icons.Default.MoreHoriz, contentDescription = "More")
                    }
                }
            }
        }
    }
}
