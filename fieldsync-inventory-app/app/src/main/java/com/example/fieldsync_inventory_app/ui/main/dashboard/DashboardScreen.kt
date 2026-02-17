package com.example.fieldsync_inventory_app.ui.main.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.BottomNavigationBar
import com.example.fieldsync_inventory_app.ui.common.components.MetricCard
import com.example.fieldsync_inventory_app.ui.main.dashboard.components.BarChart
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import com.example.fieldsync_inventory_app.util.composables.PressBackToExit
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DashboardScreen(viewModel: IDashboardViewModel,
                    topBar: @Composable () -> Unit = {},
                    navController: NavController? = null) {
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val uiState by viewModel.dashboardUiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        uiState.isLoading,
        { viewModel.refreshScreen() })

    // This will exit the app when the back button is pressed
    PressBackToExit()

    // Observe first launch state
    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch) {
            viewModel.firstLaunch()
        }
    }

    // Screen Launch
    LaunchedEffect(key1 = Unit) {
    }

    // Observe uiState.error for showing error messages
    LaunchedEffect(resourceUiState.error) {
        resourceUiState.error?.let { errorMessage ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    duration = SnackbarDuration.Short // Or SnackbarDuration.Long
                )
            }
        }
    }

    Scaffold(
        topBar = {
            topBar()
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            BottomNavigationBar(navController, 0)
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // Stock value
                        MetricCard(
                            title = "Total Stock Value",
                            duration = "Present",
                            value = "${uiState.totalStockValueDisplay} Kg",
                            valueColor = Color.Black,
                            cardColor = Color(0xFF8CBE43),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        // Total transaction
                        MetricCard(
                            title = "Total Transactions",
                            duration = "30 Days",
                            value = uiState.totalTransactions.toString(),
                            valueColor = Color.Black,
                            cardColor = Color(0xFF24A0ED),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Total sold out
                    MetricCard(
                        title = "Total Sold Out",
                        duration = "30 Days",
                        value = "${uiState.totalSoldOutDisplay} LAK",
                        valueColor = Color.Black,
                        cardColor = Color(0xFFFEF10F),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }

                item {
                    Text(
                        text = "Inventory Overview",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Top Stock Varieties",
                        color = Color.LightGray,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    // Top stock varieties
                    BarChart(data = uiState.stockData)
                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Top Sales Varieties [30 Days]",
                        color = Color.LightGray,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    // Top sale varieties
                    BarChart(data = uiState.topSaleData)
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
            PullRefreshIndicator(
                refreshing = uiState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }

    // Loading indicator and overlay
    if (resourceUiState.isLoading) {
        // A Box with a background and clickable modifier will consume touch events,
        // effectively disabling the UI below.
        // The semi-transparent background also provides visual feedback that the UI is busy.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                if (resourceUiState.loadingMessage != null) {
                    Text(
                        text = resourceUiState.loadingMessage.toString(),
                        Modifier.padding(start = 20.dp, end = 20.dp),
                        color = Color.White
                    )
                }
            }
        }
    }
}

//-- Preview --
val previewViewModel = PreviewDashboardViewModel()


@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    RCRCSeedManagerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF4A443A)
        ) {
            DashboardScreen(previewViewModel)
        }
    }
}
