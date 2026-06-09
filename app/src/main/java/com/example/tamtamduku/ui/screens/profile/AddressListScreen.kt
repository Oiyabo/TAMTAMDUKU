package com.example.tamtamduku.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tamtamduku.ui.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressListScreen(
    onBack: () -> Unit,
    onNavigateToEditAddress: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val bgColor = Color(0xFFFFFDF8)
    val orangeMain = Color(0xFFFF7A00)

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text("Alamat Saya", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        },
        containerColor = bgColor,
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Button(
                    onClick = onNavigateToEditAddress,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = orangeMain),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("+ Tambah Alamat", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Label section
            item {
                Text("Alamat Tersimpan", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                PremiumAddressCard(
                    label = "Rumah",
                    name = uiState.name.ifEmpty { "Emily Johnson" },
                    address = uiState.address.ifEmpty { "Jl. Merdeka No 10, Jakarta Pusat, DKI Jakarta 10110." },
                    onClick = onNavigateToEditAddress
                )
            }
        }
    }
}

@Composable
fun PremiumAddressCard(
    label: String,
    name: String,
    address: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFF0E5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFFF7A00), modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFFF7A00))
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF0E5), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Utama", fontSize = 11.sp, color = Color(0xFFFF7A00), fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(2.dp))
                Text(address, fontSize = 13.sp, color = Color.Gray, lineHeight = 18.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

// Keep old AddressCard for backward compatibility
@Composable
fun AddressCard(
    name: String,
    address: String,
    onClick: () -> Unit
) {
    PremiumAddressCard(label = "Rumah", name = name, address = address, onClick = onClick)
}
