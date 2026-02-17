package com.example.fieldsync_inventory_app.ui.main.report.sale_report

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.report.sale_report.model.SaleReportSeedBatch
import com.example.fieldsync_inventory_app.ui.main.report.sale_report.state.SaleReportUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewSaleReportViewModel : ViewModel(), ISaleReportViewModel {
    override val resourceUiState: StateFlow<ResourceUiState> = MutableStateFlow(ResourceUiState())

    private val dummyData = listOf(
        SaleReportSeedBatch(
            varietyId = 1,
            varietyName = "Phka Rumduol",
            year = 2024,
            seasonId = 1,
            seasonName = "Dry Season",
            seasonDescription = "Early Dry Season",
            generationId = 1,
            generationName = "Foundation",
            germinated = 1500.0,
            ungerminated = 200.0,
            germinatedSale = 45000000.0,
            ungerminatedSale = 5000000.0,
            transactionDate = System.currentTimeMillis()
        ),
        SaleReportSeedBatch(
            varietyId = 1,
            varietyName = "Phka Rumduol",
            year = 2024,
            seasonId = 1,
            seasonName = "Dry Season",
            seasonDescription = "Early Dry Season",
            generationId = 2,
            generationName = "Registered",
            germinated = 3000.0,
            ungerminated = 500.0,
            germinatedSale = 80000000.0,
            ungerminatedSale = 12000000.0,
            transactionDate = System.currentTimeMillis()
        ),
        SaleReportSeedBatch(
            varietyId = 2,
            varietyName = "Sen Kra Ob",
            year = 2024,
            seasonId = 2,
            seasonName = "Wet Season",
            seasonDescription = "Main Wet Season",
            generationId = 1,
            generationName = "Foundation",
            germinated = 1000.0,
            ungerminated = 0.0,
            germinatedSale = 30000000.0,
            ungerminatedSale = 0.0,
            transactionDate = System.currentTimeMillis()
        )
    )

    override val saleReportUiState: StateFlow<SaleReportUiState> = MutableStateFlow(
        SaleReportUiState(seedBatchList = dummyData)
    ).asStateFlow()

    override val isFirstLaunch: StateFlow<Boolean> = MutableStateFlow(false)

    override fun onFromDateChanged(date: String?) {}

    override fun onToDateChanged(date: String?) {}

    override fun firstLaunch() {}

    override fun exportReport(
        context: Context,
        uri: Uri,
        data: List<SaleReportSeedBatch>
    ) {}
}