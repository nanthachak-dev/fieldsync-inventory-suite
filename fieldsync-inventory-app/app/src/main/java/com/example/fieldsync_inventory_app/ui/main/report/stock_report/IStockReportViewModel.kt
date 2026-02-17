package com.example.fieldsync_inventory_app.ui.main.report.stock_report

import android.content.Context
import android.net.Uri
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.model.StockReportSeedBatch
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.state.StockReportUiState
import kotlinx.coroutines.flow.StateFlow

interface IStockReportViewModel {
    val resourceUiState: StateFlow<ResourceUiState>
    val stockReportUiState: StateFlow<StockReportUiState>
    val isFirstLaunch: StateFlow<Boolean>
    fun firstLaunch()
    fun exportReport(context: Context, uri: Uri, data: List<StockReportSeedBatch>)
    fun onLastDateChanged(date: String?)
}