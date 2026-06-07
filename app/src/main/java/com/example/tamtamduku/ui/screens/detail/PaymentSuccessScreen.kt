package com.example.tamtamduku.ui.screens.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tamtamduku.ui.viewmodels.WorkerViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentSuccessScreen(
    workerName: String,
    invoiceId: String,
    totalAmount: Double,
    paymentMethod: String,
    workerViewModel: WorkerViewModel,
    onDone: () -> Unit
) {
    val uiState by workerViewModel.uiState.collectAsState()
    val worker = uiState.workers.find { it.nama.equals(workerName, ignoreCase = true) }

    val sdfDate = SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.forLanguageTag("id-ID"))
    val currentDateTimeStr = "${sdfDate.format(Date())} WIB"

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFFFBF7),
                shadowElevation = 0.dp
            ) {
                Button(
                    onClick = onDone,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D00))
                ) {
                    Text(
                        text = "Lihat Transaksi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        },
        containerColor = Color(0xFFFFFBF7) // Match screenshot background exactly
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Orange Gradient Header with wave decorations
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFF8C00),
                                Color(0xFFFF6D00)
                            )
                        )
                    )
                    .statusBarsPadding()
                    .padding(top = 32.dp, bottom = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Pembayaran Berhasil!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Terima kasih, pembayaran Anda telah berhasil diproses.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Circular Checkmark with dashed decorative circle concentric around it
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(130.dp)
                    ) {
                        Canvas(modifier = Modifier.size(130.dp)) {
                            val strokeWidth = 3.dp.toPx()
                            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)

                            // Left dashed arc segment
                            drawArc(
                                color = Color.White.copy(alpha = 0.7f),
                                startAngle = 110f,
                                sweepAngle = 140f,
                                useCenter = false,
                                style = Stroke(
                                    width = strokeWidth,
                                    pathEffect = pathEffect,
                                    cap = StrokeCap.Round
                                )
                            )

                            // Right dashed arc segment
                            drawArc(
                                color = Color.White.copy(alpha = 0.7f),
                                startAngle = 290f,
                                sweepAngle = 140f,
                                useCenter = false,
                                style = Stroke(
                                    width = strokeWidth,
                                    pathEffect = pathEffect,
                                    cap = StrokeCap.Round
                                )
                            )
                        }

                        // White inner circle with bold orange tick
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = Color(0xFFFF6D00),
                                modifier = Modifier.size(54.dp)
                            )
                        }
                    }
                }
            }

            // Cards Content - Single clean white card containing all info
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .offset(y = (-30).dp), // Overlay over the header
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE).copy(alpha = 0.5f))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Pembayaran Berhasil",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFFFF6D00)
                            )

                            Text(
                                text = "Transaksi Anda telah berhasil\ndan pesanan sedang diproses.",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // 1. Details Container with light orange/yellow border
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(BorderStroke(1.dp, Color(0xFFFFCC80)), RoundedCornerShape(16.dp))
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                DetailRow(label = "ID Transaksi", value = invoiceId)
                                DetailRow(label = "Tanggal & Waktu", value = currentDateTimeStr)
                                DetailRow(
                                    label = "Metode Pembayaran",
                                    value = if (paymentMethod.equals("qris", ignoreCase = true)) "Qris" else "Lainnya"
                                )
                                
                                HorizontalDivider(color = Color(0xFFFFCC80).copy(alpha = 0.5f), thickness = 1.dp)

                                DetailRow(
                                    label = "Total Pembayaran",
                                    value = formatRupiah(totalAmount),
                                    valueBold = true
                                )
                            }

                            // 2. Langkah Selanjutnya Row with border
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(BorderStroke(1.dp, Color(0xFFFFCC80)), RoundedCornerShape(16.dp))
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color(0xFFFFF3E0), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Assignment,
                                        contentDescription = null,
                                        tint = Color(0xFFFF6D00),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Langkah Selanjutnya",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFFFF6D00)
                                    )
                                    Text(
                                        text = "Penyedia jasa akan segera menerima pesanan Anda dan menghubungi Anda melalui chat.",
                                        color = Color.Gray,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // 3. Keamanan Row with border
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(BorderStroke(1.dp, Color(0xFFFFCC80)), RoundedCornerShape(16.dp))
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color(0xFFFFF3E0), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = Color(0xFFFF6D00),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Transaksi Aman",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFFFF6D00)
                                    )
                                    Text(
                                        text = "Pembayaran Anda terlindungi dengan sistem keamanan VOCA.",
                                        color = Color.Gray,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Solid orange strip at the very bottom of the card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .background(
                                    Color(0xFFFF6D00),
                                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = Color(0xFFFF6D00), // Default value color is orange
    valueBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.Gray, fontSize = 13.sp)
        Text(
            text = value,
            color = valueColor,
            fontWeight = if (valueBold) FontWeight.Bold else FontWeight.Medium,
            fontSize = 13.sp
        )
    }
}

private fun formatRupiah(number: Double): String {
    val formatRupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
    return formatRupiah.format(number).replace("Rp", "Rp").replace(",00", "")
}
