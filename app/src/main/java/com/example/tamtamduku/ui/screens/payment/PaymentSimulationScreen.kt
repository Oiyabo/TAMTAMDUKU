package com.example.tamtamduku.ui.screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSimulationScreen(
    paymentMethod: String,
    onBack: () -> Unit,
    onPaymentCompleted: () -> Unit
) {
    var isProcessing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Simulasi Pembayaran",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFF7A00)
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        isProcessing = true
                        coroutineScope.launch {
                            delay(2000) // Simulate processing time
                            isProcessing = false
                            onPaymentCompleted()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A00)),
                    enabled = !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Selesaikan Pembayaran", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (paymentMethod.equals("Qris", ignoreCase = true)) {
                Text(
                    text = "Scan QRIS ini dengan aplikasi M-Banking atau E-Wallet Anda.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(32.dp))
                // Fake QR Code
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "Fake QR Code",
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Total: Rp255.000",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFFFF7A00)
                )
            } else {
                // Simulasi Transfer Bank / VA
                Text(
                    text = "Silakan transfer ke Virtual Account berikut:",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0E5))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Bank BCA", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "8077 1234 5678",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            letterSpacing = 2.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("a.n. VOCA Escrow", color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Total: Rp255.000",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFFFF7A00)
                )
            }
        }
    }
}
