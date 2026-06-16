package com.example.tamtamduku.presentation.detail
import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R
import androidx.compose.material3.MaterialTheme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tamtamduku.domain.model.Transaction
import com.example.tamtamduku.domain.model.Tracking
import com.example.tamtamduku.presentation.tracking.viewmodels.TrackingViewModel
import com.example.tamtamduku.presentation.search.viewmodels.WorkerViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    workerName: String,
    paymentMethod: String,
    workerViewModel: WorkerViewModel,
    trackingViewModel: TrackingViewModel,
    onBack: () -> Unit,
    onOtpVerified: (invoiceId: String, totalAmount: Double) -> Unit
) {
    val uiState by workerViewModel.uiState.collectAsState()
    val worker = if (workerName.isNotEmpty()) uiState.workers.find { it.nama.equals(workerName, ignoreCase = true) } else null

    if (workerName.isNotEmpty() && worker == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.pekerja_tidak_ditemukan), color = MaterialTheme.colorScheme.outline)
        }
        return
    }

    val totalAmount = if (worker != null) worker.baseSalary + 5000.0 else 0.0 // Salary + 5000 Admin fee

    var otpCode by remember { mutableStateOf("") }
    var timeLeft by remember { mutableIntStateOf(45) }

    // Countdown Timer logic
    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Watermark "VERIF" in background (faint premium font)
            Text(
                text = stringResource(R.string.verif),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(start = 24.dp, top = 8.dp)
                    .align(Alignment.TopStart)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(50.dp))

                // Title
                Text(
                    text = stringResource(R.string.verifikasi_otp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = stringResource(R.string.kode_otp_telah_dikirim_ke),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Phone number
                Text(
                    text = stringResource(R.string.phone_0812xxxxxxxx),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                // 5 Box OTP Input layout
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until 5) {
                        val char = if (i < otpCode.length) otpCode[i].toString() else ""
                        OtpBox(
                            value = char,
                            isSelected = i == otpCode.length,
                            onClick = {
                                if (otpCode.length < 5) {
                                    otpCode += (1..9).random().toString()
                                }
                            }
                        )
                    }
                }

                // Clickable text to clear code if they make a mistake
                if (otpCode.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.hapus_angka),
                        color = Color(0xFFFF6D00),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            otpCode = otpCode.dropLast(1)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Kirim ulang OTP timer
                Text(
                    text = if (timeLeft > 0) {
                        "Kirim ulang OTP dalam 00:${timeLeft.toString().padStart(2, '0')}"
                    } else {
                        "Kirim ulang OTP"
                    },
                    fontSize = 13.sp,
                    color = if (timeLeft > 0) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFFFF6D00),
                    modifier = Modifier.clickable(enabled = timeLeft == 0) {
                        timeLeft = 45
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                // Action Buttons
                Column(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Verifikasi Button
                    Button(
                        onClick = {
                            if (workerName.isNotEmpty() && worker != null) {
                                val sdf = SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.forLanguageTag("id-ID"))
                                val currentDateStr = "${sdf.format(Date())} WIB"
                                val trackingState = trackingViewModel.uiState.value
                                val nextInvoiceNum = "#INV-260507-${(trackingState.transactions.size + 1).toString().padStart(3, '0')}"

                                val newTransaction = Transaction(
                                    invoiceNumber = nextInvoiceNum,
                                    workerName = worker.nama,
                                    workerProfession = worker.pekerjaan,
                                    status = "Menunggu Konfirmasi",
                                    date = currentDateStr,
                                    price = totalAmount,
                                    profileUrl = worker.profileUrl,
                                    tracking = Tracking(
                                        estimasiWaktu = "-",
                                        posisiSaatIni = "Pesanan Dibuat",
                                        iconType = "Work"
                                    )
                                )
                                trackingViewModel.addTransaction(newTransaction)
                                
                                onOtpVerified(nextInvoiceNum, totalAmount)
                            } else {
                                onOtpVerified("", 0.0)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D00))
                    ) {
                        Text(
                            text = stringResource(R.string.verifikasi),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.background
                        )
                    }

                    // Ubah Nomor Telepon Button
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFFFB070)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF6D00))
                    ) {
                        Text(
                            text = stringResource(R.string.ubah_nomor_telepon),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFFFF6D00)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OtpBox(
    value: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 46.dp, height = 46.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(0xFFFF6D00) else Color(0xFFFFB070),
                shape = RoundedCornerShape(10.dp)
            )
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(10.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
