package com.example.tamtamduku.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.tamtamduku.presentation.home.HomeScreen
import com.example.tamtamduku.presentation.search.SearchScreen
import com.example.tamtamduku.presentation.detail.ServiceDetailScreen
import com.example.tamtamduku.presentation.detail.ReviewScreen
import com.example.tamtamduku.presentation.notification.NotificationsScreen
import com.example.tamtamduku.presentation.chat.ChatPage
import com.example.tamtamduku.presentation.chat.PersonalChat
import com.example.tamtamduku.presentation.search.viewmodels.WorkerViewModel

import androidx.navigation.NavHostController

fun NavGraphBuilder.homeGraph(
    navCon: NavHostController,
    workerViewModel: WorkerViewModel
) {
    composable("home") {
        HomeScreen(
            viewModel = workerViewModel,
            onNavigateToSearch = { category ->
                if (category.isNotEmpty()) {
                    workerViewModel.onKategoriChange(category)
                } else {
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
            },
            onNavigateToAddress = { navCon.navigate("address_list") }
        )
    }
    composable("notifications") {
        NotificationsScreen(
            onBack = { navCon.popBackStack() },
            onNavigate = { route ->
                // Pop the notifications screen off the backstack first so it doesn't get saved in the home state
                navCon.popBackStack()
                
                if (route == "home" || route == "tracking" || route == "chat" || route == "profile") {
                    navCon.navigate(route) {
                        popUpTo(navCon.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                } else if (route.startsWith("personal_chat/")) {
                    navCon.navigate("chat") {
                        popUpTo(navCon.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                    navCon.navigate(route)
                } else if (route.startsWith("status_pekerjaan/") || route.startsWith("hasil_kerja/")) {
                    navCon.navigate("tracking") {
                        popUpTo(navCon.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                    navCon.navigate(route)
                } else if (route.startsWith("detail/") || route.startsWith("review/") || route.startsWith("search")) {
                    navCon.navigate("home") {
                        popUpTo(navCon.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                    navCon.navigate(route)
                } else if (route.startsWith("report_list")) {
                    navCon.navigate("profile") {
                        popUpTo(navCon.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                    navCon.navigate(route)
                } else {
                    navCon.navigate(route)
                }
            }
        )
    }
    composable("chat") {
        ChatPage(
            onNavigateToPersonalChat = { userName ->
                navCon.navigate("personal_chat/$userName")
            },
            onBack = { navCon.popBackStack() }
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
}
