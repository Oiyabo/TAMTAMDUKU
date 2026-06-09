package com.example.tamtamduku

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import android.content.res.Configuration
import java.util.Locale
import com.example.tamtamduku.navigation.AppNavigation
import com.example.tamtamduku.ui.theme.AppTheme
import com.example.tamtamduku.ui.theme.TAMTAMDUKUTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        setContent {
            var currentTheme by remember { mutableStateOf(AppTheme.LIGHT) }
            var currentLanguage by remember { mutableStateOf("id") }
            
            val configuration = LocalConfiguration.current
            val context = LocalContext.current
            val newConfiguration = remember(currentLanguage, configuration) {
                Configuration(configuration).apply {
                    setLocale(Locale(currentLanguage))
                }
            }
            val newContext = remember(newConfiguration) {
                object : android.content.ContextWrapper(context) {
                    val localizedContext = context.createConfigurationContext(newConfiguration)
                    override fun getResources() = localizedContext.resources
                }
            }

            CompositionLocalProvider(
                LocalContext provides newContext,
                LocalConfiguration provides newConfiguration
            ) {
                TAMTAMDUKUTheme(appTheme = currentTheme) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background

                    ) {
                        AppNavigation(
                            onThemeChange = { currentTheme = it },
                            onLanguageChange = { currentLanguage = it },
                            currentLanguage = currentLanguage
                        )
                    }
                }
            }
        }
    }
}
