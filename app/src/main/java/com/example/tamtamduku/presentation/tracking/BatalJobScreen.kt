package com.example.tamtamduku.presentation.tracking

import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R
import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tamtamduku.domain.model.Transaction
import com.example.tamtamduku.core.util.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatalJobScreen(
    transaction: Transaction?,
    onBack: () -> Unit
) {
    if (transaction == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.transaksi_tidak_ditemukan))
        }
        return
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { 
                        Text(
                            text = stringResource(R.string.detail_pembatalan), 
                            fontWeight = FontWeight.Bold, 
                            fontSize = 20.sp,
                            color = Color.Black
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
            }
        },
        containerColor = Color(0xFFFAF9F6)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = transaction.profileUrl.ifEmpty { "https://i.pravatar.cc/150?u=${transaction.workerName}" },
                        contentDescription = "Worker Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFDE8E0))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = transaction.workerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = transaction.workerProfession,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = transaction.invoiceNumber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatRupiah(transaction.price),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFFFF8C00)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Surface(
                            color = Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = stringResource(R.string.batal),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC62828)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Dibatalkan pada\n${transaction.date}",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFFEBEE),
                border = BorderStroke(1.dp, Color(0xFFFFCDD2))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Canceled",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.pekerjaan_dibatalkan),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.pekerjaan_ini_telah_dibatalkan_lihat),
                            fontSize = 12.sp,
                            color = Color(0xFF7E2A2A)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.alasan_pembatalan),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = transaction.cancellationReason.ifEmpty { stringResource(R.string.kesepakatan_jadwal_tidak_dapat_dipenuhi) },
                        fontSize = 13.sp,
                        color = Color(0xFF333333),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF5F5F5),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.informasi_tambahan),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.jika_anda_memiliki_pertanyaan_lebih),
                            fontSize = 12.sp,
                            color = Color.Gray,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
