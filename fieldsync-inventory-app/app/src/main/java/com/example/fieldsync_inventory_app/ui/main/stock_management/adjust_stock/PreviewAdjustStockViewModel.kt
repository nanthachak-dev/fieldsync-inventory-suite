package com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.seed_batch.SeedBatchResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

class PreviewAdjustStockViewModel : ViewModel(), IAdjustStockViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState
    private val _state = MutableStateFlow(AdjustStockUiState())
    override val state: StateFlow<AdjustStockUiState> = _state

    override fun screenLaunch() {}

    override fun onTaskSelected(task: String) {}

    override fun onSeedBatchResultReceived(result: SeedBatchResult) {}

    override fun onDateTimeSelected(newDateTime: LocalDateTime) {}

    override fun onSubmitClicked() {}

    override fun onQuantityChanged(newQuantity: String) {}

    override fun onLossChanged(newLoss: String) {}

    override fun onReasonChanged(newReason: String) {}

    override fun onSuccessShown() {}

    override fun clearForm() {}
}