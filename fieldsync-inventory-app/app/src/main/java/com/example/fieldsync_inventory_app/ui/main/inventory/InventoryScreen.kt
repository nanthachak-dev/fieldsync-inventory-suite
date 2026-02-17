package com.example.fieldsync_inventory_app.ui.main.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.fieldsync_inventory_app.domain.model.stock.StockSummary
import com.example.fieldsync_inventory_app.ui.common.components.BottomNavigationBar
import com.example.fieldsync_inventory_app.ui.common.navigation.Screen
import com.example.fieldsync_inventory_app.ui.main.inventory.components.StockSummaryCard
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import com.example.fieldsync_inventory_app.ui.main.report.components.ReportButton
import com.example.fieldsync_inventory_app.util.composables.PressBackToExit

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InventoryScreen(
    viewModel: IInventoryViewModel,
    topBar: @Composable () -> Unit = {},
    navController: NavController? = null
) {
    // This will exit the app when the back button is pressed
    PressBackToExit()

    val stockSummaryState by viewModel.stockSummaryState.collectAsState()
    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = stockSummaryState.isLoading,
        onRefresh = { viewModel.refreshScreen() }
    )

    LaunchedEffect(key1 = isFirstLaunch) {
        if (isFirstLaunch) {
            viewModel.firstLaunch()
        }
    }

    Scaffold(
        topBar = {
            topBar()
        },
        bottomBar = {
            BottomNavigationBar(navController, 2)
        },
        containerColor = Color(0xFF3D3C31)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                val itemModifier = Modifier.padding(16.dp)
                
                // Stock Summary Card (Replaces Stock Data button)
                item {
                    val summary = stockSummaryState.data as? StockSummary
                    if (summary != null) {
                        StockSummaryCard(
                            summary = summary,
                            modifier = itemModifier,
                            onClick = {
                                navController?.navigate(Screen.Stock.route)
                            }
                        )
                    } else if (stockSummaryState.isLoading && stockSummaryState.data == null) {
                        // Show a loading placeholder only if it's the first load
                        Box(
                            modifier = itemModifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        )
                    }
                }

//                item {
//                    // Sale data button
//                    ReportButton(
//                        itemModifier,
//                        mainText = "Sale Data",
//                        onClick = {
//                            // Navigate to SaleScreen
//                        }
//                    )
//                }
//
//                item {
//                    // Preorder data button
//                    ReportButton(
//                        itemModifier,
//                        mainText = "Preorder Data",
//                        onClick = {
//                            // Navigate to PreorderScreen
//                        }
//                    )
//                }
            }

            PullRefreshIndicator(
                refreshing = stockSummaryState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun InventoryScreenPreview() {
    com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme {
        InventoryScreen(
            viewModel = PreviewInventoryViewModel()
        )
    }
}