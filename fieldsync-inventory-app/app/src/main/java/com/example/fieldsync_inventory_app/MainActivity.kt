package com.example.fieldsync_inventory_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.AndroidEntryPoint
import com.example.fieldsync_inventory_app.ui.common.navigation.AppNavigation
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RCRCSeedManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF3D3C31)
                ) {
                    // The AppNavigation composable will handle all screen changes
                    AppNavigation()
                }
            }
        }
    }
}