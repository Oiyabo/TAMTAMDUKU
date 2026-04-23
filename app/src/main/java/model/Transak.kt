package model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Transak(
    val worker: NovaWorker,
    val date: String,
    val time: String,
    val status: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconColor: Color
)

data class TransakCluster(
    val date: String,
    val items: List<Transak>
)