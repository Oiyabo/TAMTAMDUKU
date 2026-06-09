package com.example.tamtamduku.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tamtamduku.ui.viewmodels.ProfileViewModel
import com.example.tamtamduku.ui.theme.AppTheme
import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onThemeChange: (AppTheme) -> Unit,
    onLanguageChange: (String) -> Unit,
    currentLanguage: String,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val bgColor = MaterialTheme.colorScheme.background
    val orangeMain = Color(0xFFFF7A00)
    var notificationsEnabled by remember(uiState.settings.pushNotification) {
        mutableStateOf(uiState.settings.pushNotification)
    }

    var themeExpanded by remember { mutableStateOf(false) }
    var currentThemeLabel by remember { mutableStateOf(if (currentLanguage == "id") "TamtamDuku" else "TamtamDuku") }
    var languageExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(stringResource(R.string.settings), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        },
        containerColor = bgColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(stringResource(R.string.preferences), fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    // Notifikasi row
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFFFF0E5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = orangeMain, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(stringResource(R.string.notifications), fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.Black)
                        }
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = orangeMain,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFE0DDD8)
                            )
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F0EA), thickness = 1.dp)

                    // Bahasa row
                    Box {
                        PremiumSettingRow(
                            icon = Icons.Default.Language,
                            title = stringResource(R.string.language),
                            subtitle = if (currentLanguage == "id") stringResource(R.string.indonesian) else stringResource(R.string.english),
                            onClick = { languageExpanded = true }
                        )
                        DropdownMenu(
                            expanded = languageExpanded,
                            onDismissRequest = { languageExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.indonesian)) },
                                onClick = {
                                    onLanguageChange("id")
                                    languageExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.english)) },
                                onClick = {
                                    onLanguageChange("en")
                                    languageExpanded = false
                                }
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F0EA), thickness = 1.dp)

                    // Tema row
                    Box {
                        PremiumSettingRow(
                            icon = Icons.Default.DarkMode,
                            title = stringResource(R.string.theme),
                            subtitle = currentThemeLabel,
                            onClick = { themeExpanded = true }
                        )
                        DropdownMenu(
                            expanded = themeExpanded,
                            onDismissRequest = { themeExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("TamtamDuku") },
                                onClick = {
                                    currentThemeLabel = "TamtamDuku"
                                    onThemeChange(AppTheme.MAIN)
                                    themeExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.light_mode)) },
                                onClick = {
                                    currentThemeLabel = "Mode Terang"
                                    onThemeChange(AppTheme.LIGHT)
                                    themeExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.dark_mode)) },
                                onClick = {
                                    currentThemeLabel = "Mode Gelap"
                                    onThemeChange(AppTheme.DARK)
                                    themeExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumSettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFFFF0E5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFFFF7A00), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.Black)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}

// keep old SettingItem for backward compat
@Composable
fun SettingItem(
    title: String,
    trailingContent: @Composable () -> Unit,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F6F0), RoundedCornerShape(12.dp))
            .let { if (onClick != null) it.clickable { onClick() } else it }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
        trailingContent()
    }
}
