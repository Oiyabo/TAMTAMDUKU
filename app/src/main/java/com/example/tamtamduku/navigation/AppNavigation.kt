package com.example.tamtamduku.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.tamtamduku.ui.screens.auth.LoginScreen
import com.example.tamtamduku.ui.screens.auth.RegisterScreen
import com.example.tamtamduku.ui.screens.home.HomeScreen
import com.example.tamtamduku.ui.screens.chat.ChatPage
import com.example.tamtamduku.ui.screens.chat.PersonalChat
import com.example.tamtamduku.ui.screens.search.SearchScreen
import com.example.tamtamduku.ui.screens.detail.ServiceDetailScreen
import com.example.tamtamduku.ui.screens.detail.ReviewScreen
import com.example.tamtamduku.ui.screens.detail.PaymentScreen
import com.example.tamtamduku.ui.screens.detail.OtpVerificationScreen
import com.example.tamtamduku.ui.screens.detail.PaymentSuccessScreen
import com.example.tamtamduku.ui.screens.notification.NotificationsScreen
import com.example.tamtamduku.ui.screens.tracking.TrackingScreen
import com.example.tamtamduku.ui.screens.tracking.HasilKerjaScreen
import com.example.tamtamduku.ui.screens.profile.AccountScreen
import com.example.tamtamduku.ui.screens.profile.EditProfileScreen
import com.example.tamtamduku.ui.screens.profile.EditAddressScreen
import com.example.tamtamduku.ui.screens.profile.AddressListScreen
import com.example.tamtamduku.ui.screens.profile.ReportListScreen
import com.example.tamtamduku.ui.screens.profile.CreateReportScreen
import com.example.tamtamduku.ui.viewmodels.AuthViewModel
import com.example.tamtamduku.ui.viewmodels.WorkerViewModel
import com.example.tamtamduku.ui.viewmodels.TrackingViewModel
import com.example.tamtamduku.ui.viewmodels.ProfileViewModel
import com.example.tamtamduku.ui.theme.AppTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    onThemeChange: (AppTheme) -> Unit,
    navCon: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    workerViewModel: WorkerViewModel = viewModel(),
    trackingViewModel: TrackingViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val navBackStackEntry by navCon.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val authUiState by authViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            if (currentRoute == "home" || currentRoute == "chat" || currentRoute == "tracking" || currentRoute == "profile") {
                Column {
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
                            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Riwayat") },
                            label = { Text("Riwayat") },
                            selected = currentRoute == "tracking",
                            onClick = {
                                if (currentRoute != "tracking") {
                                    navCon.navigate("tracking") {
                                        popUpTo(navCon.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat") },
                            label = { Text("Chat") },
                            selected = currentRoute == "chat",
                            onClick = {
                                if (currentRoute != "chat") {
                                    navCon.navigate("chat") {
                                        popUpTo(navCon.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text("Profil") },
                            selected = currentRoute == "profile",
                            onClick = {
                                if (currentRoute != "profile") {
                                    navCon.navigate("profile") {
                                        popUpTo(navCon.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        val startDestination = if (authUiState.isLoggedIn) "home" else "login"

        NavHost(
            navController = navCon,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
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
                    onRegisterSuccess = { navCon.navigate("register_otp") },
                    onToLogin = { navCon.navigate("login") }
                )
            }
            composable("register_otp") {
                OtpVerificationScreen(
                    workerName = "",
                    paymentMethod = "",
                    workerViewModel = workerViewModel,
                    trackingViewModel = trackingViewModel,
                    onBack = { navCon.popBackStack() },
                    onOtpVerified = { _, _ ->
                        navCon.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )
            }
            composable("home") {
                HomeScreen(
                    onNavigateToSearch = { navCon.navigate("search") },
                    onNavigateToNotifications = { navCon.navigate("notifications") }
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
                TrackingScreen(
                    navCon = navCon, 
                    viewModel = trackingViewModel,
                    onNavigateToHasilKerja = { invoiceId ->
                        navCon.navigate("hasil_kerja/$invoiceId")
                    }
                )
            }
            composable(
                "hasil_kerja/{invoiceId}",
                arguments = listOf(navArgument("invoiceId") { type = NavType.StringType })
            ) { backStackEntry ->
                val invoiceId = backStackEntry.arguments?.getString("invoiceId") ?: ""
                val transaction = trackingViewModel.getTransactionByInvoice(invoiceId)
                HasilKerjaScreen(
                    transaction = transaction,
                    onBack = { navCon.popBackStack() },
                    onReview = { workerName ->
                        navCon.navigate("review/${Uri.encode(workerName)}")
                    }
                )
            }
            composable("profile") {
                AccountScreen(
                    viewModel = profileViewModel,
                    onBack = { navCon.popBackStack() },
                    onNavigateToEditProfile = { navCon.navigate("edit_profile") },
                    onNavigateToAddress = { navCon.navigate("address") },
                    onNavigateToReportList = { navCon.navigate("report_list") },
                    onLogout = {
                        authViewModel.logout {
                            navCon.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }
            composable("edit_profile") {
                EditProfileScreen(
                    onBack = { navCon.popBackStack() }
                )
            }
            composable("address") {
                AddressListScreen(
                    onBack = { navCon.popBackStack() },
                    onNavigateToEditAddress = { navCon.navigate("edit_address") }
                )
            }
            composable("edit_address") {
                EditAddressScreen(
                    onBack = { navCon.popBackStack() }
                )
            }
            composable("report_list") {
                ReportListScreen(
                    onBack = { navCon.popBackStack() },
                    onNavigateToCreateReport = { navCon.navigate("create_report") }
                )
            }
            composable("create_report") {
                CreateReportScreen(
                    onBack = { navCon.popBackStack() }
                )
            }
            composable(
                "detail/{workerName}",
                arguments = listOf(navArgument("workerName") { type = NavType.StringType })
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
                "payment/{workerName}",
                arguments = listOf(navArgument("workerName") { type = NavType.StringType })
            ) { backStackEntry ->
                val workerName = backStackEntry.arguments?.getString("workerName") ?: ""
                PaymentScreen(
                    workerName = workerName,
                    workerViewModel = workerViewModel,
                    trackingViewModel = trackingViewModel,
                    onBack = { navCon.popBackStack() },
                    onPaymentSuccess = { invoiceId, totalAmount, paymentMethod ->
                        navCon.navigate("payment_success/${Uri.encode(workerName)}/$invoiceId/$totalAmount/$paymentMethod") {
                            popUpTo("payment/$workerName") { inclusive = true }
                        }
                    }
                )
            }
            composable(
                "payment_success/{workerName}/{invoiceId}/{totalAmount}/{paymentMethod}",
                arguments = listOf(
                    navArgument("workerName") { type = NavType.StringType },
                    navArgument("invoiceId") { type = NavType.StringType },
                    navArgument("totalAmount") { type = NavType.FloatType },
                    navArgument("paymentMethod") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val workerName = backStackEntry.arguments?.getString("workerName") ?: ""
                val invoiceId = backStackEntry.arguments?.getString("invoiceId") ?: ""
                val totalAmount = backStackEntry.arguments?.getFloat("totalAmount")?.toDouble() ?: 0.0
                val paymentMethod = backStackEntry.arguments?.getString("paymentMethod") ?: ""
                PaymentSuccessScreen(
                    workerName = workerName,
                    invoiceId = invoiceId,
                    totalAmount = totalAmount,
                    paymentMethod = paymentMethod,
                    workerViewModel = workerViewModel,
                    onDone = {
                        navCon.navigate("tracking") {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable("notifications") {
                NotificationsScreen(
                    onBack = { navCon.popBackStack() }
                )
            }
        }
    }
}
