package com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.model.SeedBatchStockOutData
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.seed_batch.state.SeedBatchStockOutUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.state.StockOutUiState
import com.example.fieldsync_inventory_app.util.constans.StockOutTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

class PreviewStockOutViewModel() : ViewModel(), IStockOutViewModel {
    override val seedBatchUiState: StateFlow<SeedBatchStockOutUiState> =
        MutableStateFlow(SeedBatchStockOutUiState())

    override val isFirstLaunch: StateFlow<Boolean> = MutableStateFlow(true)

    override val stockOutUiState: StateFlow<StockOutUiState> =
        MutableStateFlow(StockOutUiState(selectedTask = StockOutTask.SALE))

    override val resourceUiState: StateFlow<ResourceUiState> =
        MutableStateFlow(ResourceUiState())

    override val stockOutCartItems = mutableStateListOf<SeedBatchStockOutData>(
        SeedBatchStockOutData(
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
            totalStock = 0.0,
            quantity = 300.00, //This can be updated later
            price = 1000000.00
        ),
        SeedBatchStockOutData(
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
            totalStock = 0.0,
            quantity = 200.00, //This can be updated later
            price = 500000.00
        )
    )

    override fun firstLaunch() {}
    override fun screenLaunch() {}
    override fun addSeedBatchesToCart(batches: List<SeedBatchStockOutData>) {
        TODO("Not yet implemented")
    }

    // -- Add Seed Batch Screen --
    override fun onVarietyChange(variety: String?) {}
    override fun onYearChange(year: Int) {}
    override fun onSeasonChange(season: String?) {}
    override fun onGenerationChange(generation: String?) {}
    override fun onGradingChange(grading: String?) {}
    override fun onGerminationChange(germination: String?) {}
    override fun onQuantityChange(quantity: String?) {}
    override fun onPriceChange(price: String?) {}
    override fun clearSeedBatchForm() {}

    // -- Stock Out Screen --
    override fun onTaskChange(task: StockOutTask?) {}
    override fun onCustomerChange(customer: String?) {}
    override fun onDateTimeSelected(newDateTime: LocalDateTime) {}
    override fun onNoteChange(note: String?) {}
    override fun onEditClicked(seedBatch: SeedBatchStockOutData) {}
    override fun deleteSeedBatch(seedBatch: SeedBatchStockOutData) {}
    override fun onAddOrEditSeedBatchClicked(onSuccess: () -> Unit, onFailure: (String) -> Unit) {}
    override fun clearStockOutScreen() {}
    override fun onSubmitClicked() {}
}