package com.example.tamtamduku.ui.screens.payment

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.core.MidtransSDK
import android.util.Log
import kotlinx.coroutines.launch
import com.example.tamtamduku.data.remote.PaymentRequest
import com.example.tamtamduku.data.remote.RetrofitClient
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentTestScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var snapToken by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Uji Coba Pembayaran") },
                navigationIcon = {
                    // Back button placeholder
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Total Pembayaran: Rp 10.000",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = snapToken,
                onValueChange = { },
                readOnly = true,
                label = { Text("Snap Token (Dari Firebase)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Token akan di-generate otomatis saat Anda menekan tombol di bawah. (Mendukung Saved Payment / One-Click)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        try {
                            val request = PaymentRequest(
                                orderId = "ORDER-${System.currentTimeMillis()}",
                                grossAmount = 10000,
                                userId = "user_test_123"
                            )
                            
                            val response = RetrofitClient.paymentApiService.getSnapToken(
                                url = "https://getsnaptoken-yawgyekncq-uc.a.run.app",
                                request = request
                            )
                            
                            snapToken = response.token
                            if (snapToken.isNotEmpty()) {
                                MidtransSDK.getInstance().startPaymentUiFlow(context, snapToken)
                            } else {
                                Toast.makeText(context, "Snap Token kosong dari server", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Log.e("PaymentTest", "Error fetching snap token", e)
                            Toast.makeText(context, "Gagal mendapatkan token: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Bayar Sekarang")
                }
            }
        }
    }
}
