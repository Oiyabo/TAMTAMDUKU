package com.example.tamtamduku.ui.screens.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tamtamduku.ui.viewmodels.WorkerViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    viewModel: WorkerViewModel,
    onBack: () -> Unit,
    workerName: String?
) {
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    val uiState by viewModel.uiState.collectAsState()
    val worker = remember(workerName, uiState.workers) {
        uiState.workers.find { it.nama == workerName }
    }
    
    val bgColor = Color(0xFFFFFDFB)
    val primaryOrange = Color(0xFFF97316)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Beri Rating & Ulasan",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryOrange)
            )
        },
        containerColor = bgColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Worker Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = worker?.nama?.uppercase() ?: "JENO",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = worker?.pekerjaan ?: "Design Analyst",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("5.0 (328)", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Bagian Rating
            Text("Berikan Rating", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Seberapa puas Anda dengan layanan dari ${worker?.nama?.split(" ")?.get(0) ?: "Jeno"}?",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bintang Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(5) { index ->
                    IconButton(onClick = { rating = index + 1 }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < rating) Color(0xFFFFD700) else Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Beri Opini Anda",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.DarkGray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Bagian Tulis Review
            Text("Tulis Ulasan", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Ceritakan pengalaman Anda dengan layanan yang diberikan.",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = reviewText,
                onValueChange = { if (it.length <= 500) reviewText = it },
                placeholder = { Text("Pekerja sangat profesinal\nSangat recommended!!", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = primaryOrange
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Kirim
            Button(
                onClick = { onBack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryOrange)
            ) {
                Text("Kirim Ulasan", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
