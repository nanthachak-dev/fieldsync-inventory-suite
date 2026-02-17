package com.example.fieldsync_inventory_app.ui.main.report.stock_report

import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.model.StockReportSeedBatch
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.state.StockReportUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel

class PreviewStockReportViewModel : ViewModel(), IStockReportViewModel {
    override val resourceUiState: StateFlow<ResourceUiState> = MutableStateFlow(ResourceUiState())
    
    private val dummyData = listOf(
        StockReportSeedBatch(
            varietyId = 1,
            varietyName = "Phka Rumduol",
            year = 2024,
            seasonId = 1,
            seasonName = "Dry Season",
            seasonDescription = "Early Dry Season",
            generationId = 1,
            generationName = "Foundation",
            graded = 5000.0,
            ungraded = 150.0,
            germination = true
        ),
        StockReportSeedBatch(
            varietyId = 1,
            varietyName = "Phka Rumduol",
            year = 2024,
            seasonId = 1,
            seasonName = "Dry Season",
            seasonDescription = "Early Dry Season",
            generationId = 2,
            generationName = "Registered",
            graded = 12000.0,
            ungraded = 500.0,
            germination = true
        ),
        StockReportSeedBatch(
            varietyId = 1,
            varietyName = "Phka Rumduol",
            year = 2023,
            seasonId = 2,
            seasonName = "Wet Season",
            seasonDescription = "Main Wet Season",
            generationId = 2,
            generationName = "Registered",
            graded = 8000.0,
            ungraded = 200.0,
            germination = false
        ),
        StockReportSeedBatch(
            varietyId = 2,
            varietyName = "Sen Kra Ob",
            year = 2024,
            seasonId = 1,
            seasonName = "Dry Season",
            seasonDescription = "Early Dry Season",
            generationId = 1,
            generationName = "Foundation",
            graded = 3000.0,
            ungraded = 100.0,
            germination = true
        )
    )

    override val stockReportUiState: StateFlow<StockReportUiState> = MutableStateFlow(
        StockReportUiState(seedBatchList = dummyData)
    ).asStateFlow()
    
    override val isFirstLaunch: StateFlow<Boolean> = MutableStateFlow(false)

    override fun firstLaunch() {}
    override fun exportReport(context: Context, uri: Uri, data: List<StockReportSeedBatch>) {}
    override fun onLastDateChanged(date: String?) {}
}