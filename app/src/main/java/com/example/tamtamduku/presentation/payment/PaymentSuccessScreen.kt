package com.example.tamtamduku.presentation.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tamtamduku.R
import com.example.tamtamduku.presentation.tracking.viewmodels.TrackingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PaymentSuccessScreen(
    workerName: String,
    layanan: String,
    paymentMethod: String,
    harga: String,
    tanggal: String,
    jam: String,
    trackingViewModel: TrackingViewModel,
    onNavigateHome: () -> Unit
) {
    val dateFormat = SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.forLanguageTag("id-ID"))
    val now = Date()
    val dateStr = "${dateFormat.format(now)} WIB"
    val invoiceStr = "#INV-${SimpleDateFormat("ddMMyy", Locale.forLanguageTag("id-ID")).format(now)}-${(100..999).random()}"

    val baseHarga = harga.toDoubleOrNull() ?: 0.0
    val adminFee = 5000.0
    val total = baseHarga + adminFee

    LaunchedEffect(Unit) {
        trackingViewModel.processSuccessfulPayment(
            workerName = workerName,
            layanan = layanan,
            paymentMethod = paymentMethod,
            price = total,
            invoiceNumber = invoiceStr,
            tanggal = tanggal,
            jam = jam
        )
    }

    val bgColor = MaterialTheme.colorScheme.background
    val primaryOrange = Color(0xFFFF8C00)

    Scaffold(
        containerColor = bgColor,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = onNavigateHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryOrange)
                ) {
                    Text(
                        text = stringResource(R.string.selesai),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Success Checkmark Icon
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Pembayaran Berhasil!",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.terima_kasih_pembayaran_anda_telah),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Rincian Transaksi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    DetailRow("ID Transaksi", invoiceStr, MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow("Pekerja", workerName, MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow("Layanan", layanan, MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow("Tanggal & Waktu", dateStr, MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow("Metode Pembayaran", paymentMethod, MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow("Total Pembayaran", "Rp${total.toLong()}", primaryOrange, isBold = true)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Langkah Selanjutnya Info Box (Peach Pastel)
            PremiumInfoBox(
                icon = Icons.AutoMirrored.Outlined.Assignment,
                iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                title = stringResource(R.string.langkah_selanjutnya),
                description = "Penyedia jasa akan segera menerima pesanan Anda dan menghubungi Anda melalui chat."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Transaksi Aman Info Box (Green Pastel)
            PremiumInfoBox(
                icon = Icons.Outlined.Security,
                iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                title = stringResource(R.string.transaksi_aman),
                description = "Pembayaran Anda terlindungi dengan sistem keamanan VOCA."
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, valueColor: Color, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun PremiumInfoBox(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    containerColor: Color,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
