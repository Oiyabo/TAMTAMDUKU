package model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.School
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

    @RequiresApi(Build.VERSION_CODES.O)
    val TrackingList = listOf(
        Transak(NWGroup.NWG[15], "21 Mei 2024", "10:00", "Sedang Berjalan", Icons.Default.Construction, Color(0xFFFBE9E7), Color(0xFFFF5722)),
        Transak(NWGroup.NWG[12], "21 Mei 2024", "13:00", "Menuju Lokasi", Icons.Default.School, Color(0xFFE1F5FE), Color(0xFF03A9F4))
    )
}
