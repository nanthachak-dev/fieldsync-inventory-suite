package com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.model.SeedBatchStockInData
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.seed_batch.state.SeedBatchStockInUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.state.StockInUiState
import com.example.fieldsync_inventory_app.util.constans.StockInTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

class PreviewStockInViewModel: ViewModel(), IStockInViewModel {
    override val seedBatchUiState: StateFlow<SeedBatchStockInUiState> =
        MutableStateFlow(SeedBatchStockInUiState())

    override val stockInUiState: StateFlow<StockInUiState> =
        MutableStateFlow(StockInUiState(selectedTask = StockInTask.PURCHASE))

    override val resourceUiState: StateFlow<ResourceUiState> =
        MutableStateFlow(ResourceUiState())

    override val seedBatchDataList = mutableStateListOf<SeedBatchStockInData>(
        SeedBatchStockInData(
            id = 1,
            varietyName = "RX-78-2",
            varietyId = 1,
            generation = "R1",
            generationId = 1,
            season = "Wet",
            seasonId = 1,
            year = 2023,
            grading = "Yes",
            gradingValue = true,
            germination = "Yes",
            germinationValue = true,
            totalStock = 100.00,
            quantity = 300.00, //This can be updated later
            price = 1000000.00
        ),
        SeedBatchStockInData(
            id = 2,
            varietyName = "MSZ-006",
            varietyId = 2,
            generation = "R3",
            generationId = 3,
            season = "Dry",
            seasonId = 2,
            year = 2023,
            grading = "No",
            gradingValue = false,
            germination = "No",
            germinationValue = false,
            totalStock = 50.00,
            quantity = 200.00, //This can be updated later
            price = 500000.00
        )
    )

    override fun firstLaunch() {}
    override fun screenLaunch() {}

    // -- Add Seed Batch Screen --
    override fun onVarietyChange(variety: String?) {}
    override fun onYearChange(year: Int) {}
    override fun onSeasonChange(season: String?) {}
    override fun onGenerationChange(generation: String?) {}
    override fun onGradingChange(grading: String?) {}
    override fun onGerminationChange(germination: String?) {}
    override fun onQuantityChange(quantity: String?) {}
    override fun onPriceChange(price: String?) {}
    override fun onAddOrEditSeedBatchClicked(onSuccess: () -> Unit, onFailure: (String) -> Unit) {}
    override fun clearSeedBatchForm() {}

    // -- Stock Out Screen --
    override fun onTaskChange(task: StockInTask?) {}
    override fun onSupplierChange(supplier: String?) {}
    override fun onDateTimeSelected(newDateTime: LocalDateTime) {}
    override fun onNoteChange(note: String?) {}
    override fun onEditClicked(seedBatch: SeedBatchStockInData) {}
    override fun deleteSeedBatch(seedBatch: SeedBatchStockInData) {}
    override fun clearStockInScreen() {}
    override fun onSubmitClicked() {}

    override val isFirstLaunch: StateFlow<Boolean> = MutableStateFlow(true)
}