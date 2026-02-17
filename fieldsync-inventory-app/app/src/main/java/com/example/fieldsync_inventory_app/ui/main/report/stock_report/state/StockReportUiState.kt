package com.example.fieldsync_inventory_app.ui.main.report.stock_report.state

import com.example.fieldsync_inventory_app.ui.main.report.stock_report.model.StockReportSeedBatch

data class StockReportUiState(
    val seedBatchList: List<StockReportSeedBatch> = emptyList(),
    val selectedLastDate: String? = null,
)
