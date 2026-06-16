package com.example.tamtamduku.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.tamtamduku.features.auth.LoginScreen
import com.example.tamtamduku.features.auth.RegisterScreen
import com.example.tamtamduku.features.auth.AuthViewModel

fun NavGraphBuilder.authGraph(navCon: NavController, authViewModel: AuthViewModel) {
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
}
