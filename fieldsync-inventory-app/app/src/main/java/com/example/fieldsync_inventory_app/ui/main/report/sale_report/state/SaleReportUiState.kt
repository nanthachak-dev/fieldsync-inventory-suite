package com.example.fieldsync_inventory_app.ui.main.report.sale_report.state

import com.example.fieldsync_inventory_app.ui.main.report.sale_report.model.SaleReportSeedBatch

data class SaleReportUiState(
    val seedBatchList:List<SaleReportSeedBatch> = emptyList(), // data model for SaleReportSeedBatchCard
    val selectedFromDate: String? = null,
    val selectedToDate: String? = null,
    val searchQuery:String = ""
)
