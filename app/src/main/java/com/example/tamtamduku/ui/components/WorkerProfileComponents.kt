package com.example.tamtamduku.ui.components
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
import com.example.tamtamduku.data.model.VocaWorker
import java.util.Locale

// --- MOCK DATA LOGIC ---
data class MockExperience(val duration: String, val role: String, val period: String)
data class MockReview(val name: String, val timeAgo: String, val rating: Int, val text: String, val service: String)
data class MockPortfolio(val title: String, val description: String, val tags: List<String>, val imageUrl: String)

fun generateMockExperiences(worker: VocaWorker): List<MockExperience> {
    return listOf(
        MockExperience("5 Tahun", "Senior ${worker.pekerjaan}", "Jan 2020 - Sekarang"),
        MockExperience("2 Tahun", "Junior ${worker.pekerjaan}", "Jan 2018 - Des 2019")
    )
}

fun generateMockPortfolios(worker: VocaWorker): List<MockPortfolio> {
    val job = worker.pekerjaan.lowercase()
    val keyword = when {
        job.contains("apoteker") || job.contains("medis") || job.contains("dokter") -> "medicine,doctor"
        job.contains("teknisi") || job.contains("listrik") || job.contains("ac") -> "technician,repair"
        job.contains("ledeng") || job.contains("bangunan") || job.contains("tukang") -> "construction,plumbing"
        job.contains("developer") || job.contains("programmer") -> "coding,computer"
        job.contains("desain") || job.contains("graphic") -> "design,art"
        job.contains("bersih") || job.contains("cleaning") -> "cleaning,house"
        else -> "work,professional"
    }

    return listOf(
        MockPortfolio(
            title = "Proyek ${worker.pekerjaan} 1",
            description = "Penyelesaian tugas dan tanggung jawab terkait ${worker.pekerjaan} dengan standar kualitas tinggi.",
            tags = worker.skills.take(2),
            imageUrl = "https://loremflickr.com/200/150/$keyword?random=1"
        ),
        MockPortfolio(
            title = "Proyek ${worker.pekerjaan} 2",
            description = "Kolaborasi dan implementasi solusi inovatif dalam bidang ${worker.pekerjaan}.",
            tags = worker.skills.takeLast(2),
            imageUrl = "https://loremflickr.com/200/150/$keyword?random=2"
        ),
        MockPortfolio(
            title = "Proyek ${worker.pekerjaan} 3",
            description = "Penanganan kasus khusus dan pemeliharaan sistem di sektor ${worker.pekerjaan}.",
            tags = worker.skills.take(1),
            imageUrl = "https://loremflickr.com/200/150/$keyword?random=3"
        )
    )
}

val mockReviews = listOf(
    MockReview("Maulana Ramadhan", "2 hari lalu", 5, "Sangat profesional dan cepat", "Pelayanan Reguler"),
    MockReview("Keisha Aurel Ratu", "2 hari lalu", 5, "Puas sekali dengan hasil kerjanya", "Konsultasi"),
    MockReview("Annisa Syifa Hakim", "2 hari lalu", 4, "Bagus dan sesuai ekspektasi", "Pengerjaan Proyek")
)

fun getRatingDistribution(rating: Double): List<Float> {
    return when {
        rating >= 4.8 -> listOf(0.85f, 0.10f, 0.05f, 0f, 0f)
        rating >= 4.5 -> listOf(0.60f, 0.30f, 0.10f, 0f, 0f)
        rating >= 4.0 -> listOf(0.40f, 0.40f, 0.15f, 0.05f, 0f)
        rating >= 3.5 -> listOf(0.20f, 0.40f, 0.30f, 0.10f, 0f)
        rating >= 3.0 -> listOf(0.10f, 0.20f, 0.40f, 0.20f, 0.10f)
        else -> listOf(0.05f, 0.10f, 0.20f, 0.30f, 0.35f)
    }
}
// -----------------

@Composable
fun ProfilTabContent(worker: VocaWorker) {
    val experiences = generateMockExperiences(worker)
    
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
                color = MaterialTheme.colorScheme.secondaryContainer,
                lineHeight = 20.sp
            )
        }

        // Keahlian
        if (worker.skills.isNotEmpty()) {
            Column {
                Text(stringResource(R.string.keahlian), fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    worker.skills.forEach { skill ->
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFFF0E6), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFFFFE0CC), RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text = skill, color = Color(0xFFFF8C00), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Pengalaman
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
                        Text(exp.duration, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(exp.role, fontSize = 14.sp, color = MaterialTheme.colorScheme.secondaryContainer)
                        Text(exp.period, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        // Tarif
        Column {
            Text(stringResource(R.string.tarif), fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
            val formattedSalary = String.format(Locale("id", "ID"), "%,d", worker.baseSalary.toInt()).replace(',', '.')
            Text("RP $formattedSalary / jam", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(stringResource(R.string.minimal_pemesanan_2_jam), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
        }
        
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun UlasanTabContent(worker: VocaWorker) {
    val distribution = getRatingDistribution(worker.reviewSummary.averageRating)
    
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
            border = BorderStroke(1.dp, Color(0xFFF0F0F0))
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
                    Text(stringResource(R.string.review_120_ulasan), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
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

        // Filters
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.semua_layanan), fontSize = 12.sp, color = MaterialTheme.colorScheme.secondaryContainer)
                Icon(Icons.Default.ArrowDropDown, null, tint = Color(0xFFFF8C00), modifier = Modifier.size(16.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.terbaru), fontSize = 12.sp, color = MaterialTheme.colorScheme.secondaryContainer)
                Spacer(modifier = Modifier.width(4.dp))
                Text(stringResource(R.string.str_empty), color = Color(0xFFFF8C00), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Reviews List
        mockReviews.forEach { review ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                border = BorderStroke(1.dp, Color(0xFFF0F0F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null, tint = Color(0xFFFF8C00), modifier = Modifier.size(36.dp).background(Color(0xFFFFF0E6), CircleShape).padding(6.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(review.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Row {
                                    repeat(review.rating) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(12.dp)) }
                                }
                            }
                        }
                        Text(review.timeAgo, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(review.text, fontSize = 14.sp, color = MaterialTheme.colorScheme.secondaryContainer)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.layanan), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .background(Color(0xFFFFF0E6), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(review.service, color = Color(0xFFFF8C00), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun PortofolioTabContent(worker: VocaWorker) {
    val portfolios = generateMockPortfolios(worker)
    
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
                    .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Image Thumbnail
                AsyncImage(
                    model = portfolio.imageUrl,
                    contentDescription = "Portfolio Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp, 60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF0F5FF))
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(portfolio.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        text = portfolio.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        portfolio.tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFF0E6), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(text = tag, color = Color(0xFFFF8C00), fontSize = 10.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
