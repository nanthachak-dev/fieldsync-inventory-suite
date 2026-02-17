package com.example.fieldsync_inventory_app.ui.main.inventory.stock

import com.example.fieldsync_inventory_app.ui.main.inventory.model.InventorySort
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.inventory.state.InventoryUiState
import kotlinx.coroutines.flow.StateFlow

interface IStockViewModel {
    val resourceUiState: StateFlow<ResourceUiState>
    val inventoryUiState: StateFlow<InventoryUiState>
    val isFirstLaunch: StateFlow<Boolean>
    fun firstLaunch()
    fun onSearchQueryChange(query: String)
    fun onSortChange(sort: InventorySort)
    fun refreshScreen()
}