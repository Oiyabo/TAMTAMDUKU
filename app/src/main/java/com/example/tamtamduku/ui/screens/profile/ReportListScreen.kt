package com.example.tamtamduku.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportListScreen(
    onBack: () -> Unit,
    onNavigateToCreateReport: () -> Unit
) {
    val bgColor = Color(0xFFFFFDF8)
    val orangeMain = Color(0xFFFF7A00)

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text("Lapor Masalah", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        },
        containerColor = bgColor,
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Button(
                    onClick = onNavigateToCreateReport,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = orangeMain),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("+ Buat Laporan Baru", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Riwayat Laporan", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(3) { index ->
                PremiumReportCard(
                    reportId = "#RE-00${index + 1}",
                    workerName = listOf("Budi Santoso", "Siti Rahma", "Bambang Listrik")[index],
                    issue = listOf("Datang Terlambat", "Pekerjaan Tidak Selesai", "Bahan Tidak Sesuai")[index],
                    date = listOf("23 April 2026", "10 Mei 2026", "1 Juni 2026")[index],
                    status = listOf("Selesai", "Diproses", "Selesai")[index]
                )
            }
        }
    }
}

@Composable
fun PremiumReportCard(
    reportId: String,
    workerName: String,
    issue: String,
    date: String,
    status: String
) {
    val isSelesai = status == "Selesai"
    val statusColor = if (isSelesai) Color(0xFF22C55E) else Color(0xFFFF7A00)
    val statusBg = if (isSelesai) Color(0xFFEFFDF4) else Color(0xFFFFF0E5)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFFF0E5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.BugReport, contentDescription = null, tint = Color(0xFFFF7A00), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(reportId, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                }
                Box(
                    modifier = Modifier
                        .background(statusBg, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(status, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = statusColor)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF5F0EA), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Text(workerName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(issue, fontSize = 13.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(date, fontSize = 12.sp, color = Color(0xFFAAAAAA))
        }
    }
}

// keep old ReportCard
@Composable
fun ReportCard() {
    PremiumReportCard(
        reportId = "#RE-001",
        workerName = "Pekerja",
        issue = "Datang Terlambat",
        date = "23 April 2026",
        status = "Selesai"
    )
}
