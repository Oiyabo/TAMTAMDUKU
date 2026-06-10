package com.example.tamtamduku.presentation.detail
import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R
import androidx.compose.material3.MaterialTheme

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
import androidx.compose.material.icons.outlined.Star
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
import com.example.tamtamduku.presentation.search.viewmodels.WorkerViewModel

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
    
    val bgColor = MaterialTheme.colorScheme.background
    val primaryOrange = MaterialTheme.colorScheme.primary

    val ratingDescription = when (rating) {
        1 -> "Sangat Buruk"
        2 -> "Buruk"
        3 -> "Cukup Baik"
        4 -> "Baik"
        5 -> "Sangat Baik"
        else -> stringResource(R.string.beri_opini_anda)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.beri_rating_ulasan),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.background)
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
                        .background(MaterialTheme.colorScheme.secondaryContainer)
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        Text(stringResource(R.string.rating_50_328), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Bagian Rating
            Text(stringResource(R.string.berikan_rating), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Seberapa puas Anda dengan layanan dari ${worker?.nama?.split(" ")?.get(0) ?: "Jeno"}?",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondaryContainer
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
                            imageVector = if (index < rating) Icons.Default.Star else Icons.Outlined.Star,
                            contentDescription = null,
                            tint = if (index < rating) Color(0xFFFFD700) else Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = ratingDescription,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondaryContainer,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Bagian Tulis Review
            Text(stringResource(R.string.tulis_ulasan), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(stringResource(R.string.ceritakan_pengalaman_anda_dengan_layanan),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = reviewText,
                onValueChange = { if (it.length <= 500) reviewText = it },
                placeholder = { Text(stringResource(R.string.pekerja_sangat_profesinalnsangat_recommended), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = primaryOrange
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Kirim
            Button(
                onClick = { 
                    if (rating > 0 && worker != null && reviewText.isNotBlank()) {
                        viewModel.submitReview(worker.id, rating, reviewText) { success ->
                            if (success) {
                                onBack()
                            }
                        }
                    } else if (rating > 0 && worker != null) {
                        // Option to submit without text
                        viewModel.submitReview(worker.id, rating, reviewText) { success ->
                            if (success) {
                                onBack()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryOrange)
            ) {
                Text(stringResource(R.string.kirim_ulasan), color = MaterialTheme.colorScheme.background, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
