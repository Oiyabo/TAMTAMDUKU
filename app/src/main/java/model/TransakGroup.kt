package model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Park
import androidx.compose.ui.graphics.Color

object TransakGroup {
    @RequiresApi(Build.VERSION_CODES.O)
    val TG = listOf(
        TransakCluster(
            date = "20 Mei 2024",
            items = listOf(
                Transak(NWGroup.NWG[0], "20 Mei 2024", "09:30", "Selesai", Icons.Default.Computer, Color(0xFFE3F2FD), Color(0xFF2196F3)),
                Transak(NWGroup.NWG[13], "20 Mei 2024", "08:00", "Selesai", Icons.Default.CleaningServices, Color(0xFFFFF3E0), Color(0xFFFF9800)),
                Transak(NWGroup.NWG[16], "20 Mei 2024", "07:00", "Selesai", Icons.Default.Gamepad, Color(0xFFEDE7F6), Color(0xFF673AB7))
            )
        ),
        TransakCluster(
            date = "19 Mei 2024",
            items = listOf(
                Transak(NWGroup.NWG[20], "19 Mei 2024", "10:00", "Dibatalkan", Icons.Default.Park, Color(0xFFE8F5E9), Color(0xFF4CAF50)),
                Transak(NWGroup.NWG[1], "19 Mei 2024", "08:00", "Selesai", Icons.Default.Code, Color(0xFFE3F2FD), Color(0xFF2196F3))
            )
        )
    )
}
