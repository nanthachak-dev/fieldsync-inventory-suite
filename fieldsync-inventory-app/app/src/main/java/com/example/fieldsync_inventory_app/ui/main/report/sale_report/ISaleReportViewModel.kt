package com.example.fieldsync_inventory_app.ui.main.report.sale_report

import android.content.Context
import android.net.Uri
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.report.sale_report.model.SaleReportSeedBatch
import com.example.fieldsync_inventory_app.ui.main.report.sale_report.state.SaleReportUiState
import kotlinx.coroutines.flow.StateFlow

interface ISaleReportViewModel {
    val resourceUiState: StateFlow<ResourceUiState>
    val saleReportUiState: StateFlow<SaleReportUiState>
    val isFirstLaunch: StateFlow<Boolean>
    fun onFromDateChanged(date: String?)
    fun onToDateChanged(date: String?)
    fun firstLaunch()
    fun exportReport(context: Context, uri: Uri, data: List<SaleReportSeedBatch>)
}