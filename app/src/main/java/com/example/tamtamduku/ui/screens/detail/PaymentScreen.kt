package com.example.tamtamduku.ui.screens.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.MoreHoriz
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
import com.example.tamtamduku.data.model.Transaction
import com.example.tamtamduku.ui.viewmodels.TrackingViewModel
import com.example.tamtamduku.ui.viewmodels.WorkerViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    workerName: String,
    workerViewModel: WorkerViewModel,
    trackingViewModel: TrackingViewModel,
    onBack: () -> Unit,
    onPaymentSuccess: (invoiceId: String, totalAmount: Double, paymentMethod: String) -> Unit
) {
    val uiState by workerViewModel.uiState.collectAsState()
    val worker = uiState.workers.find { it.nama.equals(workerName, ignoreCase = true) }

    if (worker == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Pekerja tidak ditemukan", color = MaterialTheme.colorScheme.outline)
        }
        return
    }

    var selectedPaymentMethod by remember { mutableStateOf("qris") }

    val hargaLayanan = worker.baseSalary
    val biayaAdmin = 5000.0
    val diskon = 0.0
    val totalPembayaran = hargaLayanan + biayaAdmin - diskon

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            // Orange Header matching the image mockup
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFF6D00)) // Deep vibrant orange
                    .statusBarsPadding()
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pembayaran",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFAF6F0),
                shadowElevation = 0.dp
            ) {
                Button(
                    onClick = {
                        val sdf = SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.forLanguageTag("id-ID"))
                        val currentDateStr = "${sdf.format(Date())} WIB"
                        val trackingState = trackingViewModel.uiState.value
                        val nextInvoiceNum = "#INV-260507-${(trackingState.transactions.size + 1).toString().padStart(3, '0')}"

                        val newTransaction = Transaction(
                            invoiceNumber = nextInvoiceNum,
                            workerName = worker.nama,
                            workerProfession = worker.pekerjaan,
                            date = currentDateStr.split(",")[0],
                            price = totalPembayaran,
                            status = "Dikerjakan"
                        )
                        trackingViewModel.addTransaction(newTransaction)

                        onPaymentSuccess(nextInvoiceNum, totalPembayaran, selectedPaymentMethod)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D00))
                ) {
                    Text(
                        text = "Bayar Sekarang",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        },
        containerColor = Color(0xFFFAF6F0) // Warm off-white background matching the image
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Detail Pesanan Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header of card
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFFFF3E0), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                                contentDescription = null,
                                tint = Color(0xFFFF6D00),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Detail Pesanan",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Text(
                                text = worker.pekerjaan,
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFF0F0F0))

                    // Worker Info Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AsyncImage(
                            model = "https://i.pravatar.cc/150?u=${worker.nama}",
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                        Text(
                            text = worker.nama,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            // 2. Rincian Pembayaran Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Rincian Pembayaran",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Harga Layanan", color = Color.Gray, fontSize = 13.sp)
                        Text(text = formatRupiah(hargaLayanan), fontWeight = FontWeight.Medium, fontSize = 13.sp, color = Color.Black)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Biaya Admin", color = Color.Gray, fontSize = 13.sp)
                        Text(text = formatRupiah(biayaAdmin), fontWeight = FontWeight.Medium, fontSize = 13.sp, color = Color.Black)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Diskon", color = Color.Gray, fontSize = 13.sp)
                        Text(text = "-${formatRupiah(diskon)}", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = Color.Gray)
                    }

                    HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(vertical = 4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Total Pembayaran", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                        Text(
                            text = formatRupiah(totalPembayaran),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFFFF6D00)
                        )
                    }
                }
            }

            // 3. Metode Pembayaran Section
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Metode Pembayaran",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                ) {
                    Column {
                        // QRIS option
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPaymentMethod = "qris" }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedPaymentMethod == "qris",
                                onClick = { selectedPaymentMethod = "qris" },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF6D00))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Qris",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Bayar dengan Scan Qris",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            // Rounded circle outline symbol on the right (like QRIS logo placeholder in mockup)
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFF3E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .border(1.5.dp, Color(0xFFFF6D00), CircleShape)
                                )
                            }
                        }

                        HorizontalDivider(color = Color(0xFFF9F9F9), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))

                        // Lainnya option
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPaymentMethod = "lainnya" }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedPaymentMethod == "lainnya",
                                onClick = { selectedPaymentMethod = "lainnya" },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF6D00))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Lainnya",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Lihat Pembayaran Lainmu",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatRupiah(number: Double): String {
    val formatRupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
    return formatRupiah.format(number).replace("Rp", "Rp").replace(",00", "")
}
