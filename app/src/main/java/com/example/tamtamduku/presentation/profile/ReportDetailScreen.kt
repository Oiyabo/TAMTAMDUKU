package com.example.tamtamduku.presentation.profile
import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R
import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.tamtamduku.presentation.profile.viewmodels.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailScreen(
    reportId: String,
    onBack: () -> Unit,
    viewModel: ReportViewModel = viewModel()
) {
    val reports by viewModel.reports.collectAsState()
    val decodedId = reportId.replace("_", " ").replace("H", "#")
    val report = reports.find { it.id == decodedId }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.detail_laporan), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = Color(0xFFFAF9F6)
    ) { innerPadding ->
        if (report != null) {
            val statusColor = when (report.status.lowercase()) {
                "selesai" -> Color(0xFFE8F5E9)
                "menunggu" -> Color(0xFFFFF3E0)
                else -> Color(0xFFF5F5F5)
            }
            val statusTextColor = when (report.status.lowercase()) {
                "selesai" -> Color(0xFF2E7D32)
                "menunggu" -> Color(0xFFEF6C00)
                else -> Color(0xFF616161)
            }
            val indicatorColor = when (report.status.lowercase()) {
                "selesai" -> Color(0xFF4CAF50)
                "menunggu" -> Color(0xFFFF9800)
                else -> Color(0xFF9E9E9E)
            }

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, Color(0xFFF0F0F0))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = report.id,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Surface(
                                color = statusColor,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = report.status,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = statusTextColor
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFFF5F5F5))
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Text(
                            text = stringResource(R.string.kategori_masalah),
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = report.category,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Text(
                            text = stringResource(R.string.deskripsi),
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .fillMaxHeight()
                                    .background(indicatorColor, RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = report.description,
                                fontSize = 15.sp,
                                lineHeight = 22.sp,
                                color = Color.Black,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFFF5F5F5))
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Text(
                            text = stringResource(R.string.tanggal),
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = report.date,
                            fontSize = 15.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )

                        if (!report.imageUrl.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color(0xFFF5F5F5))
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "Bukti Gambar",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            AsyncImage(
                                model = report.imageUrl,
                                contentDescription = "Bukti Gambar Laporan",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.laporan_tidak_ditemukan))
            }
        }
    }
}
