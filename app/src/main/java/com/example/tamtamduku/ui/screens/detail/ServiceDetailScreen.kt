package com.example.tamtamduku.ui.screens.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.tamtamduku.ui.viewmodels.WorkerViewModel

@Composable
fun ServiceDetailScreen(
    navCon: NavHostController,
    viewModel: WorkerViewModel,
    workerName: String?
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Service Detail Screen for: $workerName (TODO)")
            Button(onClick = { navCon.popBackStack() }) {
                Text("Back")
            }
            Button(onClick = { navCon.navigate("review/$workerName") }) {
                Text("Go to Review")
            }
        }
    }
}
