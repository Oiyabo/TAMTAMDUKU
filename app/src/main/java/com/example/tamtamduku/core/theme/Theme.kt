package com.example.tamtamduku.core.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    secondary = OrangeOnPrimary,
    tertiary = OrangeTertiary
)

private val OrangeColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = OrangeOnPrimary,
    primaryContainer = OrangePrimaryContainer,
    onPrimaryContainer = OrangeOnPrimaryContainer,
    secondary = OrangeSecondary,
    onSecondary = OrangeOnSecondary,
    secondaryContainer = OrangeSecondaryContainer,
    onSecondaryContainer = OrangeOnSecondaryContainer,
    tertiary = OrangeTertiary,
    onTertiary = OrangeOnTertiary,
    tertiaryContainer = OrangeTertiaryContainer,
    onTertiaryContainer = OrangeOnTertiaryContainer,
    error = OrangeError,
    onError = OrangeOnError,
    background = OrangeBackground,
    onBackground = OrangeOnBackground,
    surface = OrangeSurface,
    onSurface = OrangeOnSurface,
    surfaceVariant = OrangeSurfaceVariant,
    onSurfaceVariant = OrangeOnSurfaceVariant,
    outline = OrangeOutline,
    outlineVariant = OrangeOutlineVariant
)

private val DarkOrangeColorScheme = darkColorScheme(
    primary = OrangePrimary,
    onPrimary = Color(0xFF121212),
    primaryContainer = Color(0xFF4E2600),
    onPrimaryContainer = OrangePrimaryContainer,
    secondary = Color(0xFFFF8A65),
    onSecondary = Color(0xFF3E0E00),
    secondaryContainer = Color(0xFF5D3020),
    onSecondaryContainer = Color(0xFFFFAB91),
    tertiary = OrangeTertiary,
    onTertiary = OrangeOnTertiary,
    tertiaryContainer = Color(0xFF3B2820),
    onTertiaryContainer = Color(0xFFD7CCC8),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFC4C4C4),
    outline = Color(0xFF444444),
    outlineVariant = Color(0xFF333333)
)

enum class AppTheme {
    LIGHT, DARK
}

@Composable
fun TAMTAMDUKUTheme(
    appTheme: AppTheme = AppTheme.LIGHT,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.LIGHT -> OrangeColorScheme
        AppTheme.DARK -> DarkOrangeColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
