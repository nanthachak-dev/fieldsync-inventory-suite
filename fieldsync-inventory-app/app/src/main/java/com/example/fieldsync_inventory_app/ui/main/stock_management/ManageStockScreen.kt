package com.example.fieldsync_inventory_app.ui.main.stock_management

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.BottomNavigationBar
import com.example.fieldsync_inventory_app.ui.common.components.CardButton
import com.example.fieldsync_inventory_app.ui.common.navigation.Screen
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import com.example.fieldsync_inventory_app.util.composables.PressBackToExit

@Composable
fun ManageStockScreen(topBar: @Composable () -> Unit = {},
                      navController: NavController? = null) {
    // This will exit the app when the back button is pressed
    PressBackToExit()

    Scaffold(
        topBar = {
            topBar()
        },
        bottomBar = {
            BottomNavigationBar(navController,1)
        },
        containerColor = Color(0xFF3D3C31)
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // Defines a grid with 3 columns
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CardButton(
                    title = "Adjust Stock",
                    titleDescription = "Grading, Germination, Condition, etc.",
                    onClick = {
                        navController?.navigate("adjust_stock_screen")
                    }
                )
            }
            item {
                CardButton(
                    title = "Stock-In",
                    titleDescription = "Acquire new seeds",
                    onClick = {
                        navController?.navigate("stock_in_screen")
                    }
                )
            }
            item {
                CardButton(
                    title = "Stock-Out",
                    titleDescription = "Sell, Lost",
                    onClick = {
                        navController?.navigate("stock_out_screen")
                    }
                )
            }

            // This button is provided for admin only
            item {
                CardButton(
                    title = "Report",
                    titleDescription = "Stock Report, Sale Report",
                    onClick = {
                        navController?.navigate(Screen.SelectReport.route) // To be replaced with report screen in future update
                    }
                )
            }
        }
    }
}

// =============================== Preview ==============================

@Preview(showBackground = true)
@Composable
fun PreviewManageStockScreen() {
    RCRCSeedManagerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF4A443A)
        ) {
            ManageStockScreen()
        }
    }
}