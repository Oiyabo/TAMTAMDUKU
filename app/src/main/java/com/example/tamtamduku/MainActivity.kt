package com.example.tamtamduku

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tamtamduku.ui.theme.AppTheme
import com.example.tamtamduku.ui.theme.TAMTAMDUKUTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentTheme by remember { mutableStateOf(AppTheme.MAIN) }
            
            TAMTAMDUKUTheme(appTheme = currentTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navCon = rememberNavController()
                    AppNavigation(navCon, currentTheme) { currentTheme = it }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navCon: NavHostController, 
    currentTheme: AppTheme, 
    onThemeChange: (AppTheme) -> Unit
) {
    val navBackStackEntry by navCon.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var themeMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (currentRoute == "home" || currentRoute == "search") {
                Column {
                    // Thin orange line on top of Bottom Nav for better separation
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                        tonalElevation = 8.dp
                    ) {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home") },
                            selected = currentRoute == "home",
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.outline,
                                unselectedTextColor = MaterialTheme.colorScheme.outline
                            ),
                            onClick = {
                                if (currentRoute != "home") {
                                    navCon.navigate("home") {
                                        popUpTo(navCon.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                            label = { Text("Search") },
                            selected = currentRoute == "search",
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.outline,
                                unselectedTextColor = MaterialTheme.colorScheme.outline
                            ),
                            onClick = {
                                if (currentRoute != "search") {
                                    navCon.navigate("search") {
                                        popUpTo(navCon.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                        // Theme Selector Dropdown in Bottom Nav
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            IconButton(onClick = { themeMenuExpanded = true }) {
                                Icon(
                                    Icons.Default.Settings, 
                                    contentDescription = "Theme",
                                    tint = if (themeMenuExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                            }
                            DropdownMenu(
                                expanded = themeMenuExpanded,
                                onDismissRequest = { themeMenuExpanded = false }
                            ) {
                                AppTheme.entries.forEach { theme ->
                                    DropdownMenuItem(
                                        text = { Text(theme.name) },
                                        onClick = {
                                            onThemeChange(theme)
                                            themeMenuExpanded = false
                                        },
                                        leadingIcon = {
                                            val icon = when(theme) {
                                                AppTheme.LIGHT -> Icons.Default.Build
                                                AppTheme.DARK -> Icons.Default.Info
                                                AppTheme.MAIN -> Icons.Default.Favorite
                                            }
                                            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navCon,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navCon) }
            composable("search") { SearchScreen(navCon) }
            composable("search/filterSearch") { FilterSearchScreen(navCon) }
        }
    }
}
