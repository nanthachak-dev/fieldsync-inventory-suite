package com.example.fieldsync_inventory_app.ui.main.inventory.state

import com.example.fieldsync_inventory_app.ui.main.inventory.model.InventorySort
import com.example.fieldsync_inventory_app.ui.main.inventory.model.VarietyCardData

data class InventoryUiState(
    val searchQuery: String = "",
    val varieties: List<VarietyCardData> = emptyList(),
    val currentSort: InventorySort = InventorySort.NONE
)
