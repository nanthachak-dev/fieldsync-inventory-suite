package com.example.fieldsync_inventory_app.ui.main.inventory.stock

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.inventory.model.InventorySort
import com.example.fieldsync_inventory_app.ui.main.inventory.model.VarietyCardData
import com.example.fieldsync_inventory_app.ui.main.inventory.state.InventoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewStockViewModel(): ViewModel(), IStockViewModel {
    override val resourceUiState: StateFlow<ResourceUiState> =
        MutableStateFlow(ResourceUiState())

    private val _inventoryUiState = MutableStateFlow(InventoryUiState())
    override val inventoryUiState: StateFlow<InventoryUiState> = _inventoryUiState

    override val isFirstLaunch: StateFlow<Boolean> = MutableStateFlow(true)

    override fun firstLaunch() {
        generateDummyData()
    }

    override fun onSearchQueryChange(query: String) {
        // Not needed for preview
    }

    override fun onSortChange(sort: InventorySort) {
    }


    override fun refreshScreen() {}

    private fun generateDummyData() {
        // Dummy data
        val varieties = listOf(
            VarietyCardData(
                varietyId = 1,
                varietyName = "RX-78-2",
                stock = 12000.0,
                graded = 3250.0,
                ungraded = 9000.0,
                germinated = 5250.0,
                ungerminated = 6750.0
            ),
            VarietyCardData(
                varietyId = 2,
                varietyName = "MSZ-006",
                stock = 8000.0,
                graded = 1250.0,
                ungraded = 7000.0,
                germinated = 2250.0,
                ungerminated = 5750.0
            ),
            VarietyCardData(
                varietyId = 3,
                varietyName = "RX-93",
                stock = 25000.0,
                graded = 250.0,
                ungraded = 24750.0,
                germinated = 3300.0,
                ungerminated = 20700.0
            )
        )

        _inventoryUiState.value = _inventoryUiState.value.copy(varieties = varieties)
    }
}