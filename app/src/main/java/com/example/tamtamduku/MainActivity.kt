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
import com.example.tamtamduku.core.theme.AppTheme
import com.example.tamtamduku.core.theme.TAMTAMDUKUTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Request notification permissions for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestPermissionLauncher = registerForActivityResult(
                androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    android.util.Log.d("FCM", "Notification permission granted")
                } else {
                    android.util.Log.d("FCM", "Notification permission denied")
                }
            }
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        enableEdgeToEdge()
        setContent {
            var currentTheme by remember { mutableStateOf(AppTheme.LIGHT) }
            var currentLanguage by remember { mutableStateOf("id") }
            
            // Read targetRoute from intent initially
            var targetRoute by remember { mutableStateOf(intent.getStringExtra("targetRoute") ?: intent.extras?.getString("targetRoute")) }

            // Listen for new intents
            DisposableEffect(Unit) {
                val listener = androidx.core.util.Consumer<android.content.Intent> { newIntent ->
                    val newRoute = newIntent.getStringExtra("targetRoute") ?: newIntent.extras?.getString("targetRoute")
                    if (newRoute != null) {
                        targetRoute = newRoute
                    }
                }
                addOnNewIntentListener(listener)
                onDispose { removeOnNewIntentListener(listener) }
            }
            
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
                LocalConfiguration provides newConfiguration,
                androidx.activity.compose.LocalActivityResultRegistryOwner provides this@MainActivity,
                androidx.activity.compose.LocalOnBackPressedDispatcherOwner provides this@MainActivity
            ) {
                TAMTAMDUKUTheme(appTheme = currentTheme) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background

                    ) {
                        AppNavigation(
                            onThemeChange = { currentTheme = it },
                            onLanguageChange = { currentLanguage = it },
                            currentLanguage = currentLanguage,
                            targetRoute = targetRoute,
                            onTargetRouteHandled = { targetRoute = null }
                        )
                    }
                }
            }
        }
    }
}
