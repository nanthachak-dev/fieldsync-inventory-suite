package com.example.fieldsync_inventory_app.ui.main.dashboard.state

import com.example.fieldsync_inventory_app.ui.main.dashboard.model.BarChartData
import com.example.fieldsync_inventory_app.ui.main.history.model.TransactionCardData

import com.example.fieldsync_inventory_app.util.number.formatCurrency

data class DashboardUiState(
    val totalStockValue: Double = 0.0,
    val totalTransactions: Int = 0,
    val totalSoldOut: Double = 0.0,
    val stockData: List<BarChartData> = emptyList(),
    val topSaleData: List<BarChartData> = emptyList(),
    val salesFromDate: Long = 0L,
    val salesToDate: Long = 0L,
    val recentTransactions: List<TransactionCardData> = emptyList(),
    val isLoading: Boolean = false
){
    val totalStockValueDisplay: String
        get() = formatCurrency(totalStockValue)
    val totalSoldOutDisplay: String
        get() = formatCurrency(totalSoldOut)
}