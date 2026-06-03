package com.example.tamtamduku.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tamtamduku.data.model.VocaWorker
import java.util.Locale

// --- MOCK DATA ---
data class MockExperience(val duration: String, val role: String, val period: String)
data class MockReview(val name: String, val timeAgo: String, val rating: Int, val text: String, val service: String)
data class MockPortfolio(val title: String, val description: String, val tags: List<String>)

val mockExperiences = listOf(
    MockExperience("5 Tahun", "System Analyst di Tech Solution", "Jan 2020 - Sekarang"),
    MockExperience("2 Tahun", "Junior System Analyst di Inovatech", "Jan 2018 - Des 2019")
)

val mockReviews = listOf(
    MockReview("Maulana Ramadhan", "2 hari lalu", 5, "Hasil analisisnya sangat bagus", "Requirement Analysis"),
    MockReview("Keisha Aurel Ratu", "2 hari lalu", 5, "Dokumentasi UML lengkap", "UML Design"),
    MockReview("Annisa Syifa Hakim", "2 hari lalu", 5, "Hasil ERD sesuai kebutuhan", "Database Design")
)

val mockPortfolios = listOf(
    MockPortfolio("Sistem Informasi Akademik", "Perancangan sistem akademik menggunakan UML (Use Case Diagram)", listOf("UML", "System Design")),
    MockPortfolio("Perancangan Database", "Perancangan struktur database untuk sistem manajemen proyek", listOf("ERD", "Database Design")),
    MockPortfolio("Dokumentasi Requirement", "Dokumentasi kebutuhan sistem beserta use case dan spesifikasi fungsional", listOf("Requirement Analysis", "SQL"))
)
// -----------------

@Composable
fun ProfilTabContent(worker: VocaWorker) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Tentang Saya
        Column {
            Text("Tentang Saya", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
            Text(
                text = worker.deskripsi,
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
        }

        // Keahlian
        Column {
            Text("Keahlian", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
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

        // Pengalaman
        Column {
            Text("Pengalaman", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 12.dp))
            mockExperiences.forEach { exp ->
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
                        Text(exp.role, fontSize = 14.sp, color = Color.DarkGray)
                        Text(exp.period, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }

        HorizontalDivider(color = Color.LightGray)

        // Tarif
        Column {
            Text("Tarif", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
            val formattedSalary = String.format(Locale("id", "ID"), "%,d", worker.baseSalary.toInt()).replace(',', '.')
            Text("RP $formattedSalary / jam", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Minimal pemesanan 2 jam", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
        }
        
        Spacer(modifier = Modifier.height(100.dp)) // padding for bottom action bar
    }
}

@Composable
fun UlasanTabContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Rating Summary Box
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    Text("4.0", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF8C00))
                    Row {
                        repeat(4) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp)) }
                        Icon(Icons.Outlined.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                    }
                    Text("120 Ulasan", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                }
                
                // Progress Bars
                Column(modifier = Modifier.weight(1f)) {
                    val progressValues = listOf(0.25f, 0.25f, 0.25f, 0f, 0f)
                    progressValues.forEachIndexed { index, progress ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                            Row(modifier = Modifier.width(50.dp)) {
                                repeat(5 - index) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(10.dp)) }
                                repeat(index) { Icon(Icons.Outlined.Star, null, tint = Color.LightGray, modifier = Modifier.size(10.dp)) }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = Color(0xFFFF8C00),
                                trackColor = Color(0xFFE0E0E0),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${(progress * 100).toInt()}%", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.width(24.dp))
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
                Text("Semua Layanan", fontSize = 12.sp, color = Color.DarkGray)
                Icon(Icons.Default.ArrowDropDown, null, tint = Color(0xFFFF8C00), modifier = Modifier.size(16.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Terbaru", fontSize = 12.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.width(4.dp))
                // Just mock swap icon
                Text("↑↓", color = Color(0xFFFF8C00), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Reviews List
        mockReviews.forEach { review ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
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
                        Text(review.timeAgo, fontSize = 12.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(review.text, fontSize = 14.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Layanan :", fontSize = 10.sp, color = Color.Gray)
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
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun PortofolioTabContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        mockPortfolios.forEach { portfolio ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Mock Image Thumbnail
                Box(
                    modifier = Modifier
                        .size(80.dp, 60.dp)
                        .background(Color(0xFFF0F5FF), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Gambar", fontSize = 10.sp, color = Color.Gray)
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(portfolio.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                    Text(
                        text = portfolio.description,
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
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
        Spacer(modifier = Modifier.height(100.dp))
    }
}
