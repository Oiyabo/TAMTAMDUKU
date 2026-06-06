package com.example.tamtamduku.ui.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NotificationItemData(
    val title: String,
    val description: String,
    val time: String,
    val icon: ImageVector,
    val iconColor: Color = Color(0xFF4CAF50) // Premium green
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    val notifications = listOf(
        NotificationItemData(
            title = "Pekerjaan Disetujui",
            description = "Pekerjaan yang anda ajukan disetuj...",
            time = "09:30",
            icon = Icons.Default.ThumbUp
        ),
        NotificationItemData(
            title = "Pekerjaan Selesai",
            description = "Pekerjaan Data Analis anda telah s...",
            time = "10:12",
            icon = Icons.Default.Check
        ),
        NotificationItemData(
            title = "Pembayaran Berhasil",
            description = "Pembayaran Rp. 120.000 untuk pek...",
            time = "12:00",
            icon = Icons.Default.Paid
        )
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(end = 48.dp), // Balance the back button spacing
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Notifikasi",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFAF6F0), // Matching background tone
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color(0xFFFAF6F0) // Soft warm off-white background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(notifications) { item ->
                NotificationRow(item = item)
            }
        }
    }
}

@Composable
fun NotificationRow(item: NotificationItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Badge on Left
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(item.iconColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text Content in Middle
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Time on Right
        Text(
            text = item.time,
            fontSize = 11.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Top)
        )
    }
}
