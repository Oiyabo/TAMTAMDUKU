package com.example.tamtamduku.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tamtamduku.ui.components.WorkerCard
import com.example.tamtamduku.ui.viewmodels.WorkerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: WorkerViewModel = viewModel(),
    onNavigateToSearch: (String) -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToPaymentTest: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val randomWorker = remember(uiState.workers) {
        if (uiState.workers.isNotEmpty()) uiState.workers.random() else null
    }

    Scaffold(
        containerColor = Color(0xFFFFFDF8),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(28.dp),
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Alamat Saya",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    modifier = Modifier.size(28.dp).clickable { onNavigateToNotifications() },
                    tint = Color.Black
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        "Cari pekerja...",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { onNavigateToSearch("") },
                enabled = false, // Disable it to just act as a button
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.Gray,
                    disabledContainerColor = Color.White,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Banner Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Butuh bantuan?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Temukan pekerja terbaik\ndi sekitar Anda",
                        fontSize = 14.sp,
                        color = Color.Black,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onNavigateToSearch("") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A00)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text(
                            text = "Cari Sekarang",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
                
                // Worker Image Placeholder
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.DarkGray)
                ) {
                    Icon(
                        imageVector = Icons.Default.Engineering,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // Temporary button to test payment
            Button(
                onClick = onNavigateToPaymentTest,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("TEST PEMBAYARAN MIDTRANS")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Categories Section
            Text(
                text = "Kategori Populer",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CategoryItem(icon = Icons.AutoMirrored.Filled.TrendingUp, label = "Data", onClick = { onNavigateToSearch("Analisis Data") })
                CategoryItem(icon = Icons.Default.Bolt, label = "Listrik", onClick = { onNavigateToSearch("Instalasi Listrik") })
                CategoryItem(icon = Icons.Default.AcUnit, label = "Perbaikan", onClick = { onNavigateToSearch("Perbaikan") })
                CategoryItem(icon = Icons.Default.Code, label = "Web Dev", onClick = { onNavigateToSearch("Pembuatan Website") })
                CategoryItem(icon = Icons.Default.Home, label = "Cleaning", onClick = { onNavigateToSearch("Cleaning Service") })
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Best Workers Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pekerja Terbaik Kami",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = "Lihat Semua",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFFFF7A00),
                    modifier = Modifier.clickable { onNavigateToSearch("") }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (randomWorker != null) {
                WorkerCard(
                    worker = randomWorker,
                    onClick = { onNavigateToDetail(randomWorker.nama) },
                    isFavorite = uiState.favoriteWorkerIds.contains(randomWorker.id)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CategoryItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .background(Color(0xFFFDECDA).copy(alpha = 0.3f)), // Very faint orange tint
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(36.dp),
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}
