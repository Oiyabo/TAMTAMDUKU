package com.example.tamtamduku.navigation

import android.net.Uri
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tamtamduku.presentation.tracking.TrackingScreen
import com.example.tamtamduku.presentation.tracking.StatusPekerjaanScreen
import com.example.tamtamduku.presentation.tracking.HasilKerjaScreen
import com.example.tamtamduku.presentation.tracking.BatalJobScreen
import com.example.tamtamduku.presentation.tracking.viewmodels.TrackingViewModel

import androidx.navigation.NavHostController

fun NavGraphBuilder.trackingGraph(
    navCon: NavHostController,
    trackingViewModel: TrackingViewModel
) {
    composable("tracking") {
        TrackingScreen(navCon = navCon, viewModel = trackingViewModel)
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
        val uiState by trackingViewModel.uiState.collectAsState()
        val transaction = uiState.transactions.find { it.workerName == workerName }
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
        val uiState by trackingViewModel.uiState.collectAsState()
        val transaction = uiState.transactions.find { it.workerName == workerName }
        BatalJobScreen(
            transaction = transaction,
            onBack = { navCon.popBackStack() }
        )
    }
}
