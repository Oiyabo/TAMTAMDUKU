package com.example.tamtamduku.presentation.tracking
import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R
import androidx.compose.material3.MaterialTheme

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tamtamduku.domain.model.Transaction
import com.example.tamtamduku.presentation.tracking.viewmodels.TrackingViewModel

@Composable
fun TrackingScreen(
    navCon: NavHostController,
    viewModel: TrackingViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Tab states
    val tabs = listOf(
        stringResource(R.string.semua_status),
        stringResource(R.string.selesai),
        stringResource(R.string.dibatalkan),
        stringResource(R.string.status_dikerjakan)
    )
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navCon.popBackStack() },
                tint = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.riwayat_transaksi),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(24.dp)) // To balance the back arrow
        }

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color(0xFFFF8C00) else MaterialTheme.colorScheme.background)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Color(0xFFFF8C00) else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedTab = index }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFF8C00))
            }
        } else {
            val transactions = uiState.transactions
            val filteredTransactions = when (selectedTab) {
                1 -> transactions.filter { it.status == "Selesai" }
                2 -> transactions.filter { it.status == "Batal" }
                3 -> transactions.filter { it.status == "Dikerjakan" }
                else -> transactions
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp, bottom = 0.dp)
            ) {
                items(filteredTransactions) { transaction ->
                    TransactionHistoryCard(item = transaction) {
                        when (transaction.status) {
                            "Dikerjakan" -> navCon.navigate("status_pekerjaan/${Uri.encode(transaction.workerName)}")
                            "Selesai" -> navCon.navigate("hasil_kerja/${Uri.encode(transaction.workerName)}")
                            "Batal" -> navCon.navigate("batal_job/${Uri.encode(transaction.workerName)}")
                            else -> navCon.navigate("detail/${Uri.encode(transaction.workerName)}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionHistoryCard(item: Transaction, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.profileUrl.ifEmpty { "https://i.pravatar.cc/150?u=${item.workerName}" },
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.invoiceNumber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    val formattedPrice = String.format("%,d", item.price.toInt()).replace(',', '.')
                    Text(
                        text = "Rp$formattedPrice",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFFFF8C00)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.workerName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = item.workerProfession,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

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
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = item.date,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    StatusBadge(status = item.status)
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val displayStatus = when (status) {
        "Selesai" -> stringResource(R.string.selesai)
        "Batal" -> stringResource(R.string.batal)
        "Dikerjakan" -> stringResource(R.string.status_dikerjakan)
        else -> status
    }

    val (bgColor, textColor) = when (status) {
        "Selesai" -> Color(0xFF6FCF97) to MaterialTheme.colorScheme.background
        "Batal" -> Color(0xFFFF6B6B) to MaterialTheme.colorScheme.background
        "Dikerjakan" -> Color(0xFFFFD54F) to MaterialTheme.colorScheme.onBackground
        else -> MaterialTheme.colorScheme.outlineVariant to MaterialTheme.colorScheme.onBackground
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayStatus,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
