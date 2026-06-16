package com.example.tamtamduku.presentation.detail.components
import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R
import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
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
import com.example.tamtamduku.domain.model.VocaWorker
import java.util.Locale

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

fun getRatingDistribution(reviews: List<com.example.tamtamduku.domain.model.WorkerReview>): List<Float> {
    if (reviews.isEmpty()) return listOf(0f, 0f, 0f, 0f, 0f)
    
    val totalReviews = reviews.size.toFloat()
    val counts = IntArray(5)
    
    for (review in reviews) {
        if (review.rating in 1..5) {
            counts[5 - review.rating]++ // Index 0 is 5 stars, index 4 is 1 star
        }
    }
    
    return counts.map { it / totalReviews }
}
// -----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilTabContent(worker: VocaWorker) {
    val experiences = worker.pengalaman
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tentang Saya
        Column {
            Text(stringResource(R.string.tentang_saya), fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
            Text(
                text = worker.deskripsi.ifBlank { "Halo, saya adalah seorang ${worker.pekerjaan} profesional yang siap membantu Anda." },
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }

        // Keahlian (menggunakan kategori)
        if (worker.kategori.isNotEmpty()) {
            Column {
                Text(stringResource(R.string.keahlian), fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    worker.kategori.forEach { kategori ->
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text = kategori, color = MaterialTheme.colorScheme.onSecondaryContainer, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Pengalaman
        if (experiences.isNotEmpty()) {
            Column {
                Text(stringResource(R.string.pengalaman), fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 12.dp))
                experiences.forEach { exp ->
                    Row(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.BusinessCenter,
                            contentDescription = null,
                            tint = Color(0xFFFF8C00),
                            modifier = Modifier.size(24.dp).padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(exp.tahun, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(exp.judul, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(exp.tempat, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        // Tarif (Layanan)
        var expanded by remember { mutableStateOf(false) }
        var selectedLayanan by remember(worker.layanan) { mutableStateOf(worker.layanan.firstOrNull()) }

        Column {
            Text(stringResource(R.string.tarif), fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
            
            if (worker.layanan.isNotEmpty() && selectedLayanan != null) {
                val formattedSalary = String.format(Locale("id", "ID"), "%,d", selectedLayanan!!.harga.toInt()).replace(',', '.')
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = "Rp $formattedSalary / ${selectedLayanan!!.namaLayanan}",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        worker.layanan.forEach { layanan ->
                            val formattedHarga = String.format(Locale("id", "ID"), "%,d", layanan.harga.toInt()).replace(',', '.')
                            DropdownMenuItem(
                                text = { Text("Rp $formattedHarga / ${layanan.namaLayanan}") },
                                onClick = { 
                                    selectedLayanan = layanan
                                    expanded = false 
                                }
                            )
                        }
                    }
                }
            } else {
                val formattedSalary = String.format(Locale("id", "ID"), "%,d", worker.baseSalary.toInt()).replace(',', '.')
                Text("RP $formattedSalary / jam", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Text(stringResource(R.string.minimal_pemesanan_2_jam), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
        }
        
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun UlasanTabContent(worker: VocaWorker) {
    val distribution = getRatingDistribution(worker.ulasan)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Rating Summary Box
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(2.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text(worker.reviewSummary.averageRating.toString(), fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF8C00))
                    Row {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < worker.reviewSummary.averageRating.toInt()) Icons.Default.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Text("${worker.reviewSummary.totalReviews} ulasan", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
                }
                
                // Progress Bars
                Column(modifier = Modifier.weight(1f)) {
                    distribution.forEachIndexed { index, progress ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                            Row(modifier = Modifier.width(50.dp)) {
                                repeat(5 - index) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(10.dp)) }
                                repeat(index) { Icon(Icons.Outlined.Star, null, tint = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.size(10.dp)) }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = Color(0xFFFF8C00),
                                trackColor = MaterialTheme.colorScheme.outline,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${(progress * 100).toInt()}%", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(24.dp))
                        }
                    }
                }
            }
        }


        // Reviews List
        worker.ulasan.forEach { review ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape).padding(6.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(review.username, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Row {
                                    repeat(review.rating) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(12.dp)) }
                                }
                            }
                        }
                        Text(review.date, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(review.comment, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun PortofolioTabContent(worker: VocaWorker) {
    val portfolios = worker.portofolio
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        portfolios.forEach { portfolio ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Image Thumbnail
                AsyncImage(
                    model = portfolio.imageUrl.ifEmpty { "https://loremflickr.com/200/150/work?random=${portfolio.id}" },
                    contentDescription = "Portfolio Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp, 60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(portfolio.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        text = portfolio.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
