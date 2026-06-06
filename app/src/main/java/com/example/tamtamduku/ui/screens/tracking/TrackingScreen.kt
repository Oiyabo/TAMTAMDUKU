package com.example.tamtamduku.ui.screens.tracking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tamtamduku.data.model.Transaction
import com.example.tamtamduku.ui.viewmodels.TrackingViewModel
import java.text.NumberFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen(
    navCon: NavHostController,
    viewModel: TrackingViewModel,
    onNavigateToHasilKerja: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf("Semua") }
    val filters = listOf("Semua", "Selesai", "Dibatalkan", "Dikerjakan")

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text("Riwayat Transaksi", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navCon.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    val isSelected = selectedFilter == filter
                    val bgColor = if (isSelected) Color(0xFFFF8C00) else Color.White
                    val contentColor = if (isSelected) Color.White else Color.Black
                    val borderColor = if (isSelected) Color(0xFFFF8C00) else Color.LightGray

                    Surface(
                        modifier = Modifier.clickable { selectedFilter = filter },
                        shape = RoundedCornerShape(8.dp),
                        color = bgColor,
                        border = BorderStroke(1.dp, borderColor)
                    ) {
                        Text(
                            text = filter,
                            color = contentColor,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Transaction List
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val filteredTransactions = if (selectedFilter == "Semua") {
                    uiState.transactions
                } else {
                    val statusMap = mapOf(
                        "Selesai" to "Selesai",
                        "Dibatalkan" to "Batal",
                        "Dikerjakan" to "Dikerjakan"
                    )
                    uiState.transactions.filter { it.status == statusMap[selectedFilter] }
                }

                if (filteredTransactions.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Tidak ada transaksi", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredTransactions) { transaction ->
                            TransactionCard(transaction) {
                                if (transaction.status == "Selesai") {
                                    onNavigateToHasilKerja(transaction.invoiceNumber)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionCard(transaction: Transaction, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Worker Avatar
            AsyncImage(
                model = "https://i.pravatar.cc/150?u=${transaction.workerName}",
                contentDescription = "Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Top Row: Invoice and Price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = transaction.invoiceNumber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                    Text(
                        text = formatRupiah(transaction.price),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFFFF8C00)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Worker Name
                Text(
                    text = transaction.workerName,
                    fontSize = 14.sp,
                    color = Color.Black
                )

                // Worker Profession
                Text(
                    text = transaction.workerProfession,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom Row: Date and Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Date",
                            modifier = Modifier.size(12.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = transaction.date,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    StatusBadge(status = transaction.status)
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val bgColor: Color
    val textColor: Color
    
    when (status) {
        "Selesai" -> {
            bgColor = Color(0xFF81C784) // Green
            textColor = Color.Black
        }
        "Batal" -> {
            bgColor = Color(0xFFE57373) // Red
            textColor = Color.Black
        }
        "Dikerjakan" -> {
            bgColor = Color(0xFFFFD54F) // Yellow
            textColor = Color.Black
        }
        else -> {
            bgColor = Color.LightGray
            textColor = Color.Black
        }
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

fun formatRupiah(number: Double): String {
    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return formatRupiah.format(number).replace("Rp", "Rp").replace(",00", "")
}
