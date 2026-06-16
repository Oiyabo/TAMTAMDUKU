package com.example.tamtamduku.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tamtamduku.core.ui.theme.AppTheme
import com.example.tamtamduku.features.profile.ProfileScreen
import com.example.tamtamduku.features.profile.FavoriteWorkersScreen
import com.example.tamtamduku.features.profile.EditProfileScreen
import com.example.tamtamduku.features.profile.address.AddressListScreen
import com.example.tamtamduku.features.profile.address.EditAddressScreen
import com.example.tamtamduku.features.profile.reports.ReportListScreen
import com.example.tamtamduku.features.profile.reports.CreateReportScreen
import com.example.tamtamduku.features.profile.reports.ReportDetailScreen
import com.example.tamtamduku.features.profile.SettingsScreen
import com.example.tamtamduku.features.auth.AuthViewModel
import com.example.tamtamduku.features.profile.ProfileViewModel
import com.example.tamtamduku.features.profile.ReportViewModel
import com.example.tamtamduku.features.profile.FavoriteWorkersViewModel

import androidx.navigation.NavHostController

fun NavGraphBuilder.profileGraph(
    navCon: NavHostController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    reportViewModel: ReportViewModel,
    favoriteWorkersViewModel: FavoriteWorkersViewModel,
    onThemeChange: (AppTheme) -> Unit,
    onLanguageChange: (String) -> Unit,
    currentLanguage: String
) {
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
            onReportClick = { reportId ->
                val encodedId = reportId.replace(" ", "_").replace("#", "H")
                navCon.navigate("report_detail/$encodedId")
            }
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
            onBack = { navCon.popBackStack() },
            onThemeChange = onThemeChange,
            onLanguageChange = onLanguageChange,
            currentLanguage = currentLanguage
        )
    }
}
