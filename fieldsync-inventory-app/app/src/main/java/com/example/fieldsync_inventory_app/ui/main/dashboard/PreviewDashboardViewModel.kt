package com.example.fieldsync_inventory_app.ui.main.dashboard

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.dashboard.model.BarChartData
import com.example.fieldsync_inventory_app.ui.main.dashboard.state.DashboardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewDashboardViewModel() : ViewModel(), IDashboardViewModel {
    override val resourceUiState: StateFlow<ResourceUiState> =
        MutableStateFlow(ResourceUiState())

    private val _dashboardUiState = MutableStateFlow(DashboardUiState())
    override val dashboardUiState: StateFlow<DashboardUiState> = _dashboardUiState

    override val isFirstLaunch: StateFlow<Boolean> = MutableStateFlow(true)

    override fun firstLaunch() {
        calculateDashboardMetrics()
    }

    override fun refreshScreen() {
        calculateDashboardMetrics()
    }

    override fun updateSalesRange(from: Long, to: Long) {
        // No-op for preview
    }


    private fun calculateDashboardMetrics() {
        val stockData = listOf(
            BarChartData(
                varietyName = "RX-78-2",
                value = 0.8f
            ),
            BarChartData(
                varietyName = "MSZ-006",
                value = 0.5f
            ),
            BarChartData(
                varietyName = "MSZ-007",
                value = 0.3f
            ),
            BarChartData(
                varietyName = "MSZ-008",
                value = 0.2f
            ),
            BarChartData(
                varietyName = "RX-93",
                value = 0.1f
            )
        )

        _dashboardUiState.value = _dashboardUiState.value.copy(
            stockData = stockData,
            topSaleData = stockData.map { it.copy(value = it.value * 0.7f) } // Sample data
        )
    }
}