package com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock

import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.seed_batch.SeedBatchResult
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface IAdjustStockViewModel {
    val state: StateFlow<AdjustStockUiState>
    val resourceUiState: StateFlow<ResourceUiState>


    fun onTaskSelected(task: String)
    fun onSeedBatchResultReceived(result: SeedBatchResult)
    fun onDateTimeSelected(newDateTime: LocalDateTime) // Function to update the date/time
    fun onSubmitClicked()
    fun onSuccessShown()
    fun onQuantityChanged(newQuantity: String)
    fun onLossChanged(newLoss: String)
    fun onReasonChanged(newReason: String)
    fun clearForm()
    fun screenLaunch()
}