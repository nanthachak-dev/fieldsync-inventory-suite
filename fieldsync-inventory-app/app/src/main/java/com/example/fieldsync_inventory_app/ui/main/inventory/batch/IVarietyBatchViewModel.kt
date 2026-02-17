package com.example.fieldsync_inventory_app.ui.main.inventory.batch

import com.example.fieldsync_inventory_app.ui.main.inventory.model.InventorySort
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.model.BatchCardData
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.state.BatchUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.model.SeedBatchStockOutData
import kotlinx.coroutines.flow.StateFlow

interface IVarietyBatchViewModel {
    val resourceUiState: StateFlow<ResourceUiState>
    val batchUiState: StateFlow<BatchUiState>
    val isFirstLaunch: StateFlow<Boolean>
    val saleCartItems: SnapshotStateList<SeedBatchStockOutData>

    fun firstLaunch(varietyId: Int)
    fun onSortChange(sort: InventorySort)
    fun onFilterChange(filter: String)
    fun onHideZeroStockChange(hide: Boolean)
    fun clearSortAndFilter()
    fun loadBatchData(varietyId: Int)
    fun refreshScreen()
    fun onAddToSaleCartClick(batchCard: BatchCardData, quantity: Double, price: Double)
    fun onAddToAdjustStockClick(batchCard: BatchCardData)
}
