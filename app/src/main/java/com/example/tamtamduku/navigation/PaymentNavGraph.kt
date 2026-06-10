package com.example.tamtamduku.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tamtamduku.presentation.request.RequestFormScreen
import com.example.tamtamduku.presentation.request.RequestConfirmationScreen
import com.example.tamtamduku.presentation.payment.PaymentScreen
import com.example.tamtamduku.presentation.payment.PaymentSimulationScreen
import com.example.tamtamduku.presentation.payment.PaymentSuccessScreen
import com.example.tamtamduku.presentation.search.viewmodels.WorkerViewModel
import com.example.tamtamduku.presentation.tracking.viewmodels.TrackingViewModel

import androidx.navigation.NavHostController

fun NavGraphBuilder.paymentGraph(
    navCon: NavHostController,
    workerViewModel: WorkerViewModel,
    trackingViewModel: TrackingViewModel
) {
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
        "request_confirmation/{workerName}/{layanan}/{lokasi}/{tanggal}/{jam}/{catatan}/{kategori}/{harga}",
        arguments = listOf(
            navArgument("workerName") { type = NavType.StringType },
            navArgument("layanan") { type = NavType.StringType },
            navArgument("lokasi") { type = NavType.StringType },
            navArgument("tanggal") { type = NavType.StringType },
            navArgument("jam") { type = NavType.StringType },
            navArgument("catatan") { type = NavType.StringType },
            navArgument("kategori") { type = NavType.StringType },
            navArgument("harga") { type = NavType.StringType }
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
            catatan = backStackEntry.arguments?.getString("catatan")?.trim() ?: "",
            kategori = backStackEntry.arguments?.getString("kategori")?.trim() ?: "",
            harga = backStackEntry.arguments?.getString("harga")?.trim() ?: ""
        )
    }
    composable(
        "payment/{workerName}/{layanan}/{harga}/{tanggal}/{jam}",
        arguments = listOf(
            navArgument("workerName") { type = NavType.StringType },
            navArgument("layanan") { type = NavType.StringType },
            navArgument("harga") { type = NavType.StringType },
            navArgument("tanggal") { type = NavType.StringType },
            navArgument("jam") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val workerName = backStackEntry.arguments?.getString("workerName") ?: ""
        val layanan = backStackEntry.arguments?.getString("layanan") ?: ""
        val harga = backStackEntry.arguments?.getString("harga") ?: ""
        val tanggal = backStackEntry.arguments?.getString("tanggal") ?: ""
        val jam = backStackEntry.arguments?.getString("jam") ?: ""
        PaymentScreen(
            onBack = { navCon.popBackStack() },
            onNavigateToSimulation = { paymentMethod -> 
                navCon.navigate("payment_simulation/${Uri.encode(workerName)}/${Uri.encode(layanan)}/${Uri.encode(paymentMethod)}/${Uri.encode(harga)}/${Uri.encode(tanggal)}/${Uri.encode(jam)}") {
                    popUpTo("payment") { inclusive = true }
                }
            },
            workerName = workerName,
            layanan = layanan,
            harga = harga,
            tanggal = tanggal,
            jam = jam,
            workerViewModel = workerViewModel
        )
    }
    composable(
        "payment_simulation/{workerName}/{layanan}/{paymentMethod}/{harga}/{tanggal}/{jam}",
        arguments = listOf(
            navArgument("workerName") { type = NavType.StringType },
            navArgument("layanan") { type = NavType.StringType },
            navArgument("paymentMethod") { type = NavType.StringType },
            navArgument("harga") { type = NavType.StringType },
            navArgument("tanggal") { type = NavType.StringType },
            navArgument("jam") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val workerName = backStackEntry.arguments?.getString("workerName") ?: ""
        val layanan = backStackEntry.arguments?.getString("layanan") ?: ""
        val paymentMethod = backStackEntry.arguments?.getString("paymentMethod") ?: "Lainnya"
        val harga = backStackEntry.arguments?.getString("harga") ?: "0"
        val tanggal = backStackEntry.arguments?.getString("tanggal") ?: ""
        val jam = backStackEntry.arguments?.getString("jam") ?: ""
        
        PaymentSimulationScreen(
            paymentMethod = paymentMethod,
            onBack = { navCon.popBackStack() },
            onPaymentCompleted = {
                navCon.navigate("payment_success/${Uri.encode(workerName)}/${Uri.encode(layanan)}/${Uri.encode(paymentMethod)}/${Uri.encode(harga)}/${Uri.encode(tanggal)}/${Uri.encode(jam)}") {
                    popUpTo("payment_simulation") { inclusive = true }
                }
            },
            harga = harga,
            tanggal = tanggal,
            jam = jam
        )
    }
    composable(
        "payment_success/{workerName}/{layanan}/{paymentMethod}/{harga}/{tanggal}/{jam}",
        arguments = listOf(
            navArgument("workerName") { type = NavType.StringType },
            navArgument("layanan") { type = NavType.StringType },
            navArgument("paymentMethod") { type = NavType.StringType },
            navArgument("harga") { type = NavType.StringType },
            navArgument("tanggal") { type = NavType.StringType },
            navArgument("jam") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        PaymentSuccessScreen(
            workerName = backStackEntry.arguments?.getString("workerName") ?: "",
            layanan = backStackEntry.arguments?.getString("layanan") ?: "",
            paymentMethod = backStackEntry.arguments?.getString("paymentMethod") ?: "",
            harga = backStackEntry.arguments?.getString("harga") ?: "0",
            tanggal = backStackEntry.arguments?.getString("tanggal") ?: "",
            jam = backStackEntry.arguments?.getString("jam") ?: "",
            trackingViewModel = trackingViewModel,
            onNavigateHome = {
                navCon.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
        )
    }
}
