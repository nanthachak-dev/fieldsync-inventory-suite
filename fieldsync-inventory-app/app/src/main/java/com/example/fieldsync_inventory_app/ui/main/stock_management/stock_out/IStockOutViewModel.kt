package com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.model.SeedBatchStockOutData
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.state.StockOutUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.seed_batch.state.SeedBatchStockOutUiState
import com.example.fieldsync_inventory_app.util.constans.StockOutTask
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface IStockOutViewModel {
    val resourceUiState: StateFlow<ResourceUiState>
    val seedBatchUiState: StateFlow<SeedBatchStockOutUiState>
    val stockOutUiState: StateFlow<StockOutUiState>
    val stockOutCartItems: SnapshotStateList<SeedBatchStockOutData>
    val isFirstLaunch: StateFlow<Boolean>

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

    // -- Stock Out Screen --
    fun onTaskChange(task: StockOutTask?)
    fun onCustomerChange(customer: String?)
    fun onDateTimeSelected(newDateTime: LocalDateTime)
    fun onNoteChange(note: String?)
    fun deleteSeedBatch(seedBatch: SeedBatchStockOutData)
    fun onEditClicked(seedBatch: SeedBatchStockOutData)
    fun clearStockOutScreen()
    fun onSubmitClicked()
    fun firstLaunch()
    fun screenLaunch()
    fun addSeedBatchesToCart(batches: List<SeedBatchStockOutData>)
}
