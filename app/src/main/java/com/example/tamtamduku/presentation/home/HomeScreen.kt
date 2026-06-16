package com.example.tamtamduku.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import com.example.tamtamduku.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tamtamduku.presentation.search.viewmodels.WorkerViewModel
import com.example.tamtamduku.presentation.notification.viewmodels.NotificationViewModel
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: WorkerViewModel = viewModel(),
    onNavigateToSearch: (String) -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToPaymentTest: () -> Unit = {},
    onNavigateToAddress: () -> Unit = {},
    notificationViewModel: NotificationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val notifications by notificationViewModel.notifications.collectAsState()
    val hasUnread = notifications.any { !it.isRead }
    
    val randomWorker = remember(uiState.workers) {
        if (uiState.workers.isNotEmpty()) uiState.workers.random() else null
    }

    Scaffold(
        containerColor = Color(0xFFFAF9F6),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFAF9F6))
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onNavigateToAddress() }.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.my_address),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Temukan pekerjaan terbaik di sekitar Anda",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
                BadgedBox(
                    badge = {
                        if (hasUnread) {
                            Badge(
                                containerColor = Color.Red,
                                modifier = Modifier.size(8.dp)
                            )
                        }
                    },
                    modifier = Modifier.clickable { onNavigateToNotifications() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        modifier = Modifier.size(28.dp),
                        tint = Color.Black
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        stringResource(R.string.search_worker_placeholder),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { onNavigateToSearch("") },
                enabled = false,
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color(0xFFE0E0E0),
                    disabledContainerColor = Color.White,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Banner Section
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, Color(0xFFFFE5D0))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFFFAF7), Color(0xFFFFE5D0))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1.3f)) {
                            Text(
                                text = stringResource(R.string.need_help),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stringResource(R.string.find_best_worker_around_you),
                                fontSize = 13.sp,
                                color = Color.Gray,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { onNavigateToSearch("") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B00)),
                                shape = RoundedCornerShape(50),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = stringResource(R.string.search_now),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                        
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.worker_3d),
                            contentDescription = "Worker Image",
                            modifier = Modifier
                                .weight(1f)
                                .height(120.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Fit
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Categories Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.popular_categories),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = stringResource(R.string.see_all),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFFFF8C00),
                    modifier = Modifier.clickable { onNavigateToSearch("") }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CategoryItem(icon = Icons.AutoMirrored.Filled.TrendingUp, label = "Data", tintColor = Color(0xFFFF6B00), onClick = { onNavigateToSearch("Analisis Data") })
                CategoryItem(icon = Icons.Default.Bolt, label = "Listrik", tintColor = Color(0xFFFFB300), onClick = { onNavigateToSearch("Instalasi Listrik") })
                CategoryItem(icon = Icons.Default.Build, label = "Perbaikan", tintColor = Color(0xFF2196F3), onClick = { onNavigateToSearch("Perbaikan") })
                CategoryItem(icon = Icons.Default.Code, label = "Web Dev", tintColor = Color(0xFF9C27B0), onClick = { onNavigateToSearch("Pembuatan Website") })
                CategoryItem(icon = Icons.Default.Home, label = "Cleaning", tintColor = Color(0xFF4CAF50), onClick = { onNavigateToSearch("Cleaning Service") })
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Best Workers Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.our_best_workers),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = stringResource(R.string.see_all),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFFFF8C00),
                    modifier = Modifier.clickable { onNavigateToSearch("") }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (randomWorker != null) {
                HomeWorkerCard(
                    worker = randomWorker,
                    onClick = { onNavigateToDetail(randomWorker.nama) },
                    isFavorite = uiState.favoriteWorkerIds.contains(randomWorker.id),
                    onFavoriteToggle = { viewModel.toggleFavorite(randomWorker.id) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FeatureItem(
                        icon = Icons.Default.CheckCircle,
                        title = "Aman & Terpercaya",
                        subtitle = "Pekerja terverifikasi dan terpercaya",
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Color(0xFFEEEEEE))
                    )
                    FeatureItem(
                        icon = Icons.Default.Headphones,
                        title = "Support 24/7",
                        subtitle = "Tim support siap membantu Anda",
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Color(0xFFEEEEEE))
                    )
                    FeatureItem(
                        icon = Icons.Default.Stars,
                        title = "Satisfaction Guarantee",
                        subtitle = "Kepuasan Anda adalah prioritas kami",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryItem(icon: ImageVector, label: String, tintColor: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(28.dp),
                tint = tintColor
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

@Composable
fun HomeWorkerCard(
    worker: com.example.tamtamduku.domain.model.VocaWorker,
    onClick: () -> Unit,
    isFavorite: Boolean = false,
    onFavoriteToggle: (Boolean) -> Unit = {}
) {
    var isFavoriteState by remember(isFavorite) { mutableStateOf(isFavorite) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = worker.profileUrl.ifEmpty { "https://i.pravatar.cc/150?u=${worker.nama}" },
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 100.dp, height = 130.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = worker.nama,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFFFF0E5), RoundedCornerShape(8.dp))
                            .clickable {
                                isFavoriteState = !isFavoriteState
                                onFavoriteToggle(isFavoriteState)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFavoriteState) Icons.Filled.Favorite else Icons.Default.Bookmark,
                            contentDescription = "Favorite/Bookmark",
                            tint = if (isFavoriteState) Color.Red else Color(0xFFFF8C00),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = worker.pekerjaan,
                    fontSize = 13.sp,
                    color = Color(0xFFFF8C00),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${worker.reviewSummary.averageRating} (${worker.reviewSummary.totalReviews})",
                        fontSize = 11.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "|",
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "3.2 KM",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF7F7F9), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = worker.deskripsi,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                val formattedPrice = String.format("Rp. %,d", worker.baseSalary.toInt()).replace(',', '.')
                Surface(
                    onClick = onClick,
                    color = Color(0xFFFF6B00),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$formattedPrice (Basic)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color(0xFFFF6B00),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFFFF8C00),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 7.sp,
                color = Color.Gray,
                lineHeight = 9.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
