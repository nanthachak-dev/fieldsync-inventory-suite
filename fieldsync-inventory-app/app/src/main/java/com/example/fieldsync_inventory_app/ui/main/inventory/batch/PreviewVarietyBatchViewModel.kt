package com.example.fieldsync_inventory_app.ui.main.inventory.batch

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.model.BatchCardData
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.state.BatchUiState
import com.example.fieldsync_inventory_app.ui.main.inventory.model.InventorySort
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.model.SeedBatchStockOutData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewVarietyBatchViewModel : IVarietyBatchViewModel {
    override val resourceUiState = MutableStateFlow(ResourceUiState()).asStateFlow()
    override val batchUiState = MutableStateFlow(
        BatchUiState(
            selectedVarietyName = "Sample Variety",
            batchList = listOf(
                BatchCardData(1,1,"RX-78-2", 2023, 1, "Yala", "",1, "R1", grading = true, germination = true, 100.0),
                BatchCardData(2,2,"RX-93", 2023, 1,"Yala", "", 2,"R2", grading = false, germination = true, 200.0),
                BatchCardData(3,3,"RX-78-3", 2023,1, "Maha", "", 1,"R1", grading = true, germination = false, 300.0),
                BatchCardData(4,1,"RX-78-2", 2024, 1,"Yala", "", 2,"R2", grading = false, germination = false, 400.0),
            )
        )
    ).asStateFlow()
    override val isFirstLaunch = MutableStateFlow(true).asStateFlow()
    override val saleCartItems: SnapshotStateList<SeedBatchStockOutData> = mutableStateListOf(
        // Dummy data
        SeedBatchStockOutData(
            id = 0,
            seedBatchId = 1,
            varietyName = "Sample Variety",
            varietyId = 1,
            generation = "R1",
            generationId = 1,
            season = "Yala",
            seasonId = 1,
            year = 2020,
            grading = "Yes",
            gradingValue = true,
            germination = "Yes",
            germinationValue = true,
            totalStock = 100.0,
            quantity = 10.0,
            price = 1000.0)
    )

    override fun firstLaunch(varietyId: Int) {}
    override fun onSortChange(sort: InventorySort) {}
    override fun onFilterChange(filter: String) {}
    override fun onHideZeroStockChange(hide: Boolean) {}
    override fun clearSortAndFilter() {}
    override fun loadBatchData(varietyId: Int) {}
    override fun refreshScreen() {}
    override fun onAddToSaleCartClick(
        batchCard: BatchCardData,
        quantity: Double,
        price: Double
    ) {
        TODO("Not yet implemented")
    }

    override fun onAddToAdjustStockClick(batchCard: BatchCardData) {
        TODO("Not yet implemented")
    }
}
