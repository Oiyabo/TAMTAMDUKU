package com.example.tamtamduku.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.tamtamduku.ui.screens.auth.LoginScreen
import com.example.tamtamduku.ui.screens.auth.RegisterScreen
import com.example.tamtamduku.ui.screens.home.HomeScreen
import com.example.tamtamduku.ui.screens.chat.ChatPage
import com.example.tamtamduku.ui.screens.chat.PersonalChat
import com.example.tamtamduku.ui.screens.search.SearchScreen
import com.example.tamtamduku.ui.screens.detail.ServiceDetailScreen
import com.example.tamtamduku.ui.screens.detail.ReviewScreen
import com.example.tamtamduku.ui.screens.tracking.TrackingScreen
import com.example.tamtamduku.ui.screens.tracking.StatusPekerjaanScreen
import com.example.tamtamduku.ui.screens.tracking.HasilKerjaScreen
import com.example.tamtamduku.ui.screens.tracking.BatalJobScreen
import com.example.tamtamduku.ui.screens.profile.ProfileScreen
import com.example.tamtamduku.ui.screens.profile.FavoriteWorkersScreen
import com.example.tamtamduku.ui.screens.profile.EditProfileScreen
import com.example.tamtamduku.ui.screens.profile.AddressListScreen
import com.example.tamtamduku.ui.screens.profile.EditAddressScreen
import com.example.tamtamduku.ui.screens.profile.ReportListScreen
import com.example.tamtamduku.ui.screens.profile.CreateReportScreen
import com.example.tamtamduku.ui.screens.profile.SettingsScreen
import com.example.tamtamduku.ui.viewmodels.AuthViewModel
import com.example.tamtamduku.ui.viewmodels.WorkerViewModel
import com.example.tamtamduku.ui.viewmodels.TrackingViewModel
import com.example.tamtamduku.ui.screens.request.RequestFormScreen
import com.example.tamtamduku.ui.screens.request.RequestConfirmationScreen
import com.example.tamtamduku.ui.screens.payment.PaymentScreen
import com.example.tamtamduku.ui.screens.payment.PaymentSuccessScreen
import com.example.tamtamduku.ui.screens.notification.NotificationsScreen
import com.example.tamtamduku.ui.viewmodels.ProfileViewModel
import com.example.tamtamduku.ui.viewmodels.ReportViewModel
import com.example.tamtamduku.ui.viewmodels.FavoriteWorkersViewModel
import com.example.tamtamduku.ui.screens.profile.ReportDetailScreen
import com.example.tamtamduku.ui.theme.AppTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    onThemeChange: (AppTheme) -> Unit,
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

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            if (currentRoute == "home" || currentRoute == "chat" || currentRoute == "tracking" || currentRoute == "profile") {
                NavigationBar(
                    modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
                    windowInsets = WindowInsets(0.dp),
                    containerColor = Color(0xFFFFFDF8),
                    tonalElevation = 8.dp
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
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = Color(0xFFFF6D00)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Outlined.FactCheck, contentDescription = "Tracking") },
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
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = Color(0xFFFF6D00)
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
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = Color(0xFFFF6D00)
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
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = Color(0xFFFF6D00)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        val authUiState by authViewModel.uiState.collectAsState()
        val startDestination = if (authUiState.isLoggedIn) "home" else "login"

        NavHost(
            navController = navCon,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding).statusBarsPadding()
        ) {
            composable("login") {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        navCon.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onToRegister = { navCon.navigate("register") }
                )
            }
            composable("register") {
                RegisterScreen(
                    viewModel = authViewModel,
                    onRegisterSuccess = { navCon.navigate("login") },
                    onToLogin = { navCon.navigate("login") }
                )
            }
            composable("home") {
                HomeScreen(
                    viewModel = workerViewModel,
                    onNavigateToSearch = { category ->
                        if (category.isNotEmpty()) {
                            workerViewModel.onKategoriChange(category)
                        } else {
                            // Reset kategori if just opening search normally
                            workerViewModel.onKategoriChange("")
                        }
                        navCon.navigate("search")
                    },
                    onNavigateToNotifications = { navCon.navigate("notifications") },
                    onNavigateToDetail = { workerName ->
                        navCon.navigate("detail/${Uri.encode(workerName)}")
                    },
                    onNavigateToPaymentTest = {
                        navCon.navigate("payment/TestWorker/TestLayanan")
                    }
                )
            }
            composable("notifications") {
                NotificationsScreen(
                    onBack = { navCon.popBackStack() }
                )
            }
            composable("chat") {
                ChatPage(
                    onNavigateToPersonalChat = { userName ->
                        navCon.navigate("personal_chat/$userName")
                    }
                )
            }
            composable(
                "personal_chat/{userName}",
                arguments = listOf(navArgument("userName") { type = NavType.StringType })
            ) { backStackEntry ->
                PersonalChat(
                    userName = backStackEntry.arguments?.getString("userName") ?: "",
                    onBack = { navCon.popBackStack() }
                )
            }
            composable("search") {
                SearchScreen(
                    viewModel = workerViewModel,
                    onBack = { navCon.popBackStack() },
                    onNavigateToDetail = { workerName ->
                        navCon.navigate("detail/${Uri.encode(workerName)}")
                    }
                )
            }
            composable("tracking") {
                TrackingScreen(navCon = navCon, viewModel = trackingViewModel)
            }
            composable("profile") {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onBack = { navCon.popBackStack() },
                    onNavigateToFavorite = { navCon.navigate("favorite_workers") },
                    onNavigateToEditProfile = { navCon.navigate("edit_profile") },
                    onNavigateToAddress = { navCon.navigate("address_list") },
                    onNavigateToReport = { navCon.navigate("report_list") },
                    onNavigateToSettings = { navCon.navigate("settings") },
                    onLogout = {
                        authViewModel.logout {
                            navCon.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }
            composable(
                "detail/{workerName}",
                arguments = listOf(navArgument("workerName") { type = NavType.StringType }),
                deepLinks = listOf(
                    navDeepLink { uriPattern = "https://tamtamduku-9647d.web.app/worker/{workerName}" },
                    navDeepLink { uriPattern = "tamtamduku://worker/{workerName}" }
                )
            ) { backStackEntry ->
                ServiceDetailScreen(
                    navCon = navCon,
                    viewModel = workerViewModel,
                    workerName = backStackEntry.arguments?.getString("workerName")
                )
            }
            composable(
                "review/{workerName}",
                arguments = listOf(navArgument("workerName") { type = NavType.StringType })
            ) { backStackEntry ->
                ReviewScreen(
                    viewModel = workerViewModel,
                    onBack = { navCon.popBackStack() },
                    workerName = backStackEntry.arguments?.getString("workerName")
                )
            }
            composable(
                "status_pekerjaan/{workerName}",
                arguments = listOf(navArgument("workerName") { type = NavType.StringType })
            ) { backStackEntry ->
                StatusPekerjaanScreen(
                    navCon = navCon,
                    viewModel = trackingViewModel,
                    workerName = backStackEntry.arguments?.getString("workerName") ?: ""
                )
            }
            composable(
                "hasil_kerja/{workerName}",
                arguments = listOf(navArgument("workerName") { type = NavType.StringType })
            ) { backStackEntry ->
                val workerName = backStackEntry.arguments?.getString("workerName") ?: ""
                val transaction = trackingViewModel.uiState.value.transactions.find { it.workerName == workerName }
                HasilKerjaScreen(
                    transaction = transaction,
                    onBack = { navCon.popBackStack() },
                    onReview = { wName ->
                        navCon.navigate("review/${Uri.encode(wName)}")
                    }
                )
            }
            composable(
                "batal_job/{workerName}",
                arguments = listOf(navArgument("workerName") { type = NavType.StringType })
            ) { backStackEntry ->
                val workerName = backStackEntry.arguments?.getString("workerName") ?: ""
                val transaction = trackingViewModel.uiState.value.transactions.find { it.workerName == workerName }
                BatalJobScreen(
                    transaction = transaction,
                    onBack = { navCon.popBackStack() }
                )
            }
            composable("favorite_workers") {
                FavoriteWorkersScreen(navCon = navCon, viewModel = favoriteWorkersViewModel)
            }
            composable("edit_profile") {
                EditProfileScreen(
                    onBack = { navCon.popBackStack() },
                    viewModel = profileViewModel
                )
            }
            composable("address_list") {
                AddressListScreen(
                    onBack = { navCon.popBackStack() },
                    onNavigateToEditAddress = { navCon.navigate("edit_address") },
                    viewModel = profileViewModel
                )
            }
            composable("edit_address") {
                EditAddressScreen(
                    onBack = { navCon.popBackStack() },
                    viewModel = profileViewModel
                )
            }
            composable("report_list") {
                ReportListScreen(
                    onBack = { navCon.popBackStack() },
                    onNavigateToCreateReport = { navCon.navigate("create_report") },
                    onReportClick = { reportId -> navCon.navigate("report_detail/${android.net.Uri.encode(reportId)}") },
                    viewModel = reportViewModel
                )
            }
            composable("create_report") {
                CreateReportScreen(
                    onBack = { navCon.popBackStack() },
                    viewModel = reportViewModel
                )
            }
            composable(
                "report_detail/{reportId}",
                arguments = listOf(navArgument("reportId") { type = NavType.StringType })
            ) { backStackEntry ->
                val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
                ReportDetailScreen(
                    reportId = reportId,
                    onBack = { navCon.popBackStack() },
                    viewModel = reportViewModel
                )
            }
            composable("settings") {
                SettingsScreen(
                    onBack = { navCon.popBackStack() }
                )
            }
            composable(
                "request_form/{workerName}",
                arguments = listOf(navArgument("workerName") { type = NavType.StringType })
            ) { backStackEntry ->
                RequestFormScreen(
                    navCon = navCon,
                    viewModel = workerViewModel,
                    workerName = backStackEntry.arguments?.getString("workerName")
                )
            }
            composable(
                "request_confirmation/{workerName}/{layanan}/{lokasi}/{tanggal}/{jam}/{catatan}",
                arguments = listOf(
                    navArgument("workerName") { type = NavType.StringType },
                    navArgument("layanan") { type = NavType.StringType },
                    navArgument("lokasi") { type = NavType.StringType },
                    navArgument("tanggal") { type = NavType.StringType },
                    navArgument("jam") { type = NavType.StringType },
                    navArgument("catatan") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                RequestConfirmationScreen(
                    navCon = navCon,
                    viewModel = workerViewModel,
                    workerName = backStackEntry.arguments?.getString("workerName"),
                    layanan = backStackEntry.arguments?.getString("layanan")?.trim() ?: "",
                    lokasi = backStackEntry.arguments?.getString("lokasi")?.trim() ?: "",
                    tanggal = backStackEntry.arguments?.getString("tanggal")?.trim() ?: "",
                    jam = backStackEntry.arguments?.getString("jam")?.trim() ?: "",
                    catatan = backStackEntry.arguments?.getString("catatan")?.trim() ?: ""
                )
            }
            composable(
                "payment/{workerName}/{layanan}",
                arguments = listOf(
                    navArgument("workerName") { type = NavType.StringType },
                    navArgument("layanan") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                PaymentScreen(
                    onBack = { navCon.popBackStack() },
                    onNavigateToSuccess = { 
                        navCon.navigate("payment_success") {
                            popUpTo("payment") { inclusive = true }
                        }
                    },
                    workerName = backStackEntry.arguments?.getString("workerName") ?: "",
                    layanan = backStackEntry.arguments?.getString("layanan") ?: "",
                    workerViewModel = workerViewModel
                )
            }
            composable("payment_success") {
                PaymentSuccessScreen(
                    onNavigateHome = {
                        navCon.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
