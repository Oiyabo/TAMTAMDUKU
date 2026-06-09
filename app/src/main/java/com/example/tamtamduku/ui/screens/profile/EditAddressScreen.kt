package com.example.tamtamduku.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tamtamduku.ui.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddressScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // Default to user's name but leave address parts empty for a new address
    var name by remember { mutableStateOf(if (uiState.name.isEmpty()) "Bang Lijen" else uiState.name) }
    var province by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var addressDetail by remember { mutableStateOf("") }
    var additionalDetail by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Tambah Alamat Baru",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = Color(0xFFFFFDF8)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            AddressSectionHeader("Nama Lengkap Penerima")
            AddressTextField(value = name, onValueChange = { name = it }, placeholder = "Cth: Budi Santoso")

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp, color = Color.LightGray)

            AddressSectionHeader("Provinsi, Kota, Kecamatan, Kode Pos")
            AddressTextField(value = province, onValueChange = { province = it }, placeholder = "Provinsi (Cth: Lampung)")
            Spacer(modifier = Modifier.height(8.dp))
            AddressTextField(value = city, onValueChange = { city = it }, placeholder = "Kota/Kabupaten (Cth: Bandar Lampung)")
            Spacer(modifier = Modifier.height(8.dp))
            AddressTextField(value = district, onValueChange = { district = it }, placeholder = "Kecamatan (Cth: Rajabasa)")
            Spacer(modifier = Modifier.height(8.dp))
            AddressTextField(value = postalCode, onValueChange = { postalCode = it }, placeholder = "Kode Pos (Cth: 35141)")

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp, color = Color.LightGray)

            AddressSectionHeader("Nama Jalan, Gedung, No Rumah")
            AddressTextField(value = addressDetail, onValueChange = { addressDetail = it }, placeholder = "Cth: Jl. Pramuka No.10, Gedung A", singleLine = false, minLines = 4)

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp, color = Color.LightGray)

            AddressSectionHeader("Detail Lainnya (Opsional)")
            AddressTextField(value = additionalDetail, onValueChange = { additionalDetail = it }, placeholder = "Cth: Blok B, Warna Pagar, dll")

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { 
                    val fullAddress = buildString {
                        append(addressDetail)
                        if (district.isNotBlank()) append(", $district")
                        if (city.isNotBlank()) append(", $city")
                        if (province.isNotBlank()) append(", $province")
                        if (postalCode.isNotBlank()) append(" $postalCode")
                        if (additionalDetail.isNotBlank()) append(" ($additionalDetail)")
                    }
                    if (fullAddress.isNotBlank()) {
                        viewModel.addAddress(name, fullAddress)
                    }
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF7A00)
                )
            ) {
                Text(
                    text = "Simpan Alamat Baru",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AddressSectionHeader(title: String) {
    Text(
        text = title,
        color = Color.Gray,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            if (placeholder.isNotEmpty()) {
                Text(text = placeholder, color = Color.Gray)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color(0xFFFF7A00),
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        ),
        singleLine = singleLine,
        minLines = minLines
    )
}
