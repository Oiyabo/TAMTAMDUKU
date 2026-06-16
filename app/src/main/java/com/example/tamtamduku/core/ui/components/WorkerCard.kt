package com.example.tamtamduku.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tamtamduku.R
import com.example.tamtamduku.core.util.formatShortPrice
import com.example.tamtamduku.domain.model.VocaWorker

@Composable
fun WorkerCard(
    worker: VocaWorker,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onFavoriteToggle: (Boolean) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = worker.profileUrl.ifEmpty { "https://i.pravatar.cc/150?u=${worker.nama}" },
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 90.dp, height = 110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = worker.nama,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f)
                    )
                    var isFavoriteState by remember(isFavorite) { mutableStateOf(isFavorite) }
                    Icon(
                        imageVector = if (isFavoriteState) Icons.Filled.Favorite else Icons.Filled.Bookmark,
                        contentDescription = "Favorite/Bookmark",
                        tint = if (isFavoriteState) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { 
                                isFavoriteState = !isFavoriteState 
                                onFavoriteToggle(isFavoriteState)
                            }
                    )
                }

                Text(
                    text = worker.pekerjaan,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < worker.reviewSummary.averageRating.toInt()) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${worker.reviewSummary.averageRating} (${worker.reviewSummary.totalReviews})",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.str_empty),
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.distance_32_km),
                        fontSize = 10.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(percent = 50))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = worker.deskripsi,
                        fontSize = 10.sp,
                        color = Color(0xFF4A4A4A),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                var expanded by remember { mutableStateOf(false) }
                var selectedLayanan by remember(worker.layanan) { 
                    mutableStateOf(worker.layanan.firstOrNull()) 
                }

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFF8C00), RoundedCornerShape(percent = 50))
                            .clickable { expanded = true }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val displayText = if (selectedLayanan != null) {
                                "${formatShortPrice(selectedLayanan!!.harga)} (${selectedLayanan!!.namaLayanan})"
                            } else {
                                "${formatShortPrice(worker.baseSalary)} (Basic)"
                            }
                            Text(
                                text = displayText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        if (worker.layanan.isNotEmpty()) {
                            worker.layanan.forEach { layanan ->
                                DropdownMenuItem(
                                    text = { 
                                        Text(
                                            "${layanan.namaLayanan} - ${formatShortPrice(layanan.harga)}", 
                                            color = MaterialTheme.colorScheme.onBackground, 
                                            fontSize = 12.sp
                                        ) 
                                    },
                                    onClick = { 
                                        selectedLayanan = layanan
                                        expanded = false 
                                    }
                                )
                            }
                        } else {
                            val premiumPrice = formatShortPrice(worker.baseSalary * 1.5)
                            val vipPrice = formatShortPrice(worker.baseSalary * 2.0)
                            
                            DropdownMenuItem(
                                text = { Text("Layanan Premium - $premiumPrice", color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp) },
                                onClick = { expanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Layanan VIP - $vipPrice", color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp) },
                                onClick = { expanded = false }
                            )
                        }
                    }
                }
            }
        }
    }
}
