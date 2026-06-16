package com.example.tamtamduku.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tamtamduku.core.ui.theme.AppTheme
import com.example.tamtamduku.features.auth.AuthViewModel
import com.example.tamtamduku.features.profile.FavoriteWorkersViewModel
import com.example.tamtamduku.features.profile.ProfileViewModel
import com.example.tamtamduku.features.profile.ReportViewModel
import com.example.tamtamduku.features.search.WorkerViewModel
import com.example.tamtamduku.features.tracking.TrackingViewModel

@Composable
fun AppNavigation(
    onThemeChange: (AppTheme) -> Unit,
    onLanguageChange: (String) -> Unit,
    currentLanguage: String,
    targetRoute: String? = null,
    onTargetRouteHandled: () -> Unit = {},
    navCon: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    workerViewModel: WorkerViewModel = viewModel(),
    trackingViewModel: TrackingViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    reportViewModel: ReportViewModel = viewModel(),
    favoriteWorkersViewModel: FavoriteWorkersViewModel = viewModel()
) {
    val navBackStackEntry by navCon.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val authUiState by authViewModel.uiState.collectAsState()
    val isLoggedIn = authUiState.isLoggedIn

    LaunchedEffect(targetRoute, isLoggedIn) {
        if (targetRoute != null && isLoggedIn) {
            navCon.navigate(targetRoute) {
                popUpTo(navCon.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
            onTargetRouteHandled()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            if (currentRoute == "home" || currentRoute == "chat" || currentRoute == "tracking" || currentRoute == "profile") {
                NavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    windowInsets = WindowInsets.navigationBars,
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                        label = null,
                        selected = currentRoute == "home",
                        onClick = {
                            if (currentRoute != "home") {
                                navCon.navigate("home") {
                                    popUpTo(navCon.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.AutoMirrored.Outlined.FactCheck, contentDescription = "Tracking") },
                        label = null,
                        selected = currentRoute == "tracking",
                        onClick = {
                            if (currentRoute != "tracking") {
                                navCon.navigate("tracking") {
                                    popUpTo(navCon.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Outlined.Sms, contentDescription = "Chat") },
                        label = null,
                        selected = currentRoute == "chat",
                        onClick = {
                            if (currentRoute != "chat") {
                                navCon.navigate("chat") {
                                    popUpTo(navCon.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Outlined.PersonOutline, contentDescription = "Profile") },
                        label = null,
                        selected = currentRoute == "profile",
                        onClick = {
                            if (currentRoute != "profile") {
                                navCon.navigate("profile") {
                                    popUpTo(navCon.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        val startDestination = remember { if (authViewModel.uiState.value.isLoggedIn) "home" else "login" }

        NavHost(
            navController = navCon,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding).statusBarsPadding()
        ) {
            authGraph(navCon, authViewModel)
            homeGraph(navCon, workerViewModel)
            trackingGraph(navCon, trackingViewModel)
            profileGraph(
                navCon, authViewModel, profileViewModel, reportViewModel,
                favoriteWorkersViewModel, onThemeChange, onLanguageChange, currentLanguage
            )
            paymentGraph(navCon, workerViewModel, trackingViewModel)
        }
    }
}
