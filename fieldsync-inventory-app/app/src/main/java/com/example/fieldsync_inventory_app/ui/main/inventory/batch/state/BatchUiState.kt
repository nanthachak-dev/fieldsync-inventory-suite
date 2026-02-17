package com.example.fieldsync_inventory_app.ui.main.inventory.batch.state

import com.example.fieldsync_inventory_app.ui.main.inventory.model.InventorySort
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.model.BatchCardData

data class BatchUiState (
    val selectedVarietyId: Int = 0,
    val selectedVarietyName: String = "",
    val batchList: List<BatchCardData> = emptyList(),
    val currentSort: InventorySort = InventorySort.NONE,
    val currentFilter: String = "None"
)