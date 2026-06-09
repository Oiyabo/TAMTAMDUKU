package com.example.tamtamduku.ui.screens.tracking
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tamtamduku.ui.viewmodels.TrackingViewModel

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
    val item = uiState.transactions.find { it.workerName == workerName }
    val isSelesai = item?.status == "Selesai"

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
                text = stringResource(R.string.fe0732001),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = if (isSelesai) Color(0xFFE8F5E9) else yellowBadge,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (isSelesai) "Selesai" else "Sedang Dikerjakan",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = if (isSelesai) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Timeline
            Column(modifier = Modifier.fillMaxWidth()) {
                TimelineItem(
                    title = stringResource(R.string.permintaan_diterima),
                    time = "20 Mei 2026, 09.15",
                    isCompleted = true,
                    isLast = false
                )
                TimelineItem(
                    title = stringResource(R.string.pekerjaan_dimulai),
                    time = "20 Mei 2026, 09.45",
                    isCompleted = true,
                    isLast = false
                )
                TimelineItem(
                    title = stringResource(R.string.sedang_dikerjakan),
                    time = "20 Mei 2026, 10.00",
                    isCompleted = true,
                    isLast = false
                )
                TimelineItem(
                    title = stringResource(R.string.selesai),
                    time = if (isSelesai) "20 Mei 2026, 11.00" else "-",
                    isCompleted = isSelesai,
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
                                .fillMaxWidth(if (isSelesai) 1f else 0.6f)
                                .height(12.dp)
                                .background(primaryOrange, RoundedCornerShape(50))
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = if (isSelesai) "100%" else "60%",
                        color = if (isSelesai) primaryOrange else primaryOrange,
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
                    if (!isSelesai) viewModel.markAsSelesai(workerName)
                    navCon.navigate("review/${Uri.encode(workerName)}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelesai) primaryOrange else Color(0xFFD1D5DB)
                ),
                enabled = true
            ) {
                Text(
                    text = stringResource(R.string.konfirmasi_pekerjaan_selesai),
                    color = if (isSelesai) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.background,
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
