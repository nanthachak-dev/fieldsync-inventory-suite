package com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.model.SeedBatchStockInData
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.seed_batch.state.SeedBatchStockInUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.state.StockInUiState
import com.example.fieldsync_inventory_app.util.constans.StockInTask
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface IStockInViewModel {
    val resourceUiState: StateFlow<ResourceUiState>
    val seedBatchUiState: StateFlow<SeedBatchStockInUiState>
    val stockInUiState: StateFlow<StockInUiState>
    val seedBatchDataList: SnapshotStateList<SeedBatchStockInData>

    // -- Add Seed Batch Screen --
    fun onAddOrEditSeedBatchClicked(onSuccess: () -> Unit, onFailure: (String) -> Unit)
    fun onVarietyChange(variety: String?)
    fun onYearChange(year: Int)
    fun onSeasonChange(season: String?)
    fun onGenerationChange(generation: String?)
    fun onGradingChange(grading: String?)
    fun onGerminationChange(germination: String?)
    fun onQuantityChange(quantity: String?)
    fun onPriceChange(price: String?)
    fun clearSeedBatchForm()

    // -- Stock In Screen --
    fun onTaskChange(task: StockInTask?)
    fun onSupplierChange(supplier: String?)
    fun onDateTimeSelected(newDateTime: LocalDateTime)
    fun onNoteChange(note: String?)
    fun deleteSeedBatch(seedBatch: SeedBatchStockInData)
    fun onEditClicked(seedBatch: SeedBatchStockInData)
    fun clearStockInScreen()
    fun onSubmitClicked()
    fun firstLaunch()
    fun screenLaunch()
    val isFirstLaunch: StateFlow<Boolean>
}