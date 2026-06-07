package com.example.tamtamduku.ui.screens.tracking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tamtamduku.data.model.Transaction
import com.example.tamtamduku.util.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HasilKerjaScreen(
    transaction: Transaction?,
    onBack: () -> Unit,
    onReview: (String) -> Unit
) {
    if (transaction == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Transaksi tidak ditemukan")
        }
        return
    }

    val descText = when {
        transaction.workerProfession.contains("AC", ignoreCase = true) -> 
            "Telah dilakukan servis cuci AC and pengecekan freon secara menyeluruh."
        transaction.workerProfession.contains("Data", ignoreCase = true) ->
            "Telah diselesaikan analisis data untuk kebutuhan perusahaan."
        transaction.workerProfession.contains("Kebersihan", ignoreCase = true) || transaction.workerProfession.contains("Cleaning", ignoreCase = true) ->
            "Telah dilakukan pembersihan rumah atau ruangan secara menyeluruh."
        transaction.workerProfession.contains("Listrik", ignoreCase = true) ->
            "Telah diperbaiki masalah instalasi pada kelistrikan."
        transaction.workerProfession.contains("Web", ignoreCase = true) || transaction.workerProfession.contains("Developer", ignoreCase = true) || transaction.workerProfession.contains("Analyst", ignoreCase = true) ->
            "Telah dibuat sistem inventory sederhana berbasis web yang mencakup pengelolaan barang, stok, transaksi, dan laporan."
        else -> "Telah diselesaikan pekerjaan sesuai dengan kesepakatan dan permintaan."
    }

    val tags = when {
        transaction.workerProfession.contains("AC", ignoreCase = true) -> listOf("Cuci AC", "Pengecekan Freon")
        transaction.workerProfession.contains("Data", ignoreCase = true) -> listOf("Analisis Data", "Visualisasi")
        transaction.workerProfession.contains("Kebersihan", ignoreCase = true) || transaction.workerProfession.contains("Cleaning", ignoreCase = true) -> listOf("Sapu & Pel", "Pembersihan Debu")
        transaction.workerProfession.contains("Listrik", ignoreCase = true) -> listOf("Perbaikan Instalasi", "Pengecekan Panel")
        transaction.workerProfession.contains("Web", ignoreCase = true) || transaction.workerProfession.contains("Developer", ignoreCase = true) || transaction.workerProfession.contains("Analyst", ignoreCase = true) -> listOf("Manajemen Barang", "Transaksi", "Laporan")
        else -> listOf("Pekerjaan Selesai", "Pengecekan Akhir")
    }

    val noteText = when {
        transaction.workerProfession.contains("AC", ignoreCase = true) -> 
            "AC sudah kembali dingin dan tekanan freon dipastikan normal. Jangan lupa cuci AC rutin setiap 3 bulan."
        transaction.workerProfession.contains("Data", ignoreCase = true) ->
            "Laporan hasil akhir beserta dashboard interaktif sudah saya kirimkan via email. Silakan dicek."
        transaction.workerProfession.contains("Kebersihan", ignoreCase = true) || transaction.workerProfession.contains("Cleaning", ignoreCase = true) ->
            "Ruangan sudah bersih, rapi, dan wangi."
        transaction.workerProfession.contains("Listrik", ignoreCase = true) ->
            "Jalur listrik sudah diisolasi dengan aman dan dites menyala normal kembali."
        transaction.workerProfession.contains("Web", ignoreCase = true) || transaction.workerProfession.contains("Developer", ignoreCase = true) || transaction.workerProfession.contains("Analyst", ignoreCase = true) ->
            "Sistem sudah diuji dan berjalan dengan baik. Silakan dicoba fitur-fiturnya. Jika ada revisi minor, saya siap membantu."
        else -> "Terima kasih atas kepercayaannya. Semua pekerjaan telah diselesaikan dengan baik."
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text("Hasil Kerja Pekerja", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                color = Color.Transparent
            ) {
                Button(
                    onClick = { 
                        onReview(transaction.workerName) 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00))
                ) {
                    Text("Beri Ulasan", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            }
        },
        containerColor = Color(0xFFFAF9F6)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = "https://i.pravatar.cc/150?u=${transaction.workerName}",
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = transaction.workerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = transaction.workerProfession,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = transaction.invoiceNumber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatRupiah(transaction.price),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFFFF8C00)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Surface(
                            color = Color(0xFF81C784),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Selesai",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Selesai pada\n${transaction.date}",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.End
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF1F8E9),
                border = BorderStroke(1.dp, Color(0xFFDCEDC8))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Pekerja telah selesai",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Silakan tinjau hasil pekerjaan sebelum memberikan ulasan.",
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Hasil Pekerjaan",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = descText,
                        fontSize = 12.sp,
                        color = Color.Black,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tags.forEach { tag ->
                            TagPill(tag)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF5F5F5),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Catatan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = noteText,
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TagPill(text: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF1F8E9),
        border = BorderStroke(1.dp, Color(0xFFDCEDC8))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                fontSize = 10.sp,
                color = Color.DarkGray
            )
        }
    }
}
