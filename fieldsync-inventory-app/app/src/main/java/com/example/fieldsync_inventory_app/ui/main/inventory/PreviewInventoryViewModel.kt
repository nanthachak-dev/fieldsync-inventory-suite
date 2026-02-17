package com.example.fieldsync_inventory_app.ui.main.inventory

import com.example.fieldsync_inventory_app.domain.model.stock.StockSummary
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewInventoryViewModel : IInventoryViewModel {
    override val resourceUiState: StateFlow<ResourceUiState> = MutableStateFlow(ResourceUiState()).asStateFlow()
    override val isFirstLaunch: StateFlow<Boolean> = MutableStateFlow(false).asStateFlow()
    override fun firstLaunch() {}
    override fun refreshScreen() {}
    override val stockSummaryState: StateFlow<ResourceUiState> = MutableStateFlow(
        ResourceUiState(
            data = StockSummary(
                totalStock = 45000.0,
                totalR1Stock = 15000.0,
                totalR2Stock = 15000.0,
                totalR3Stock = 15000.0,
                totalGraded = 30000.0,
                totalUngraded = 15000.0,
                totalGerminated = 35000.0,
                totalUngerminated = 10000.0,
                totalR1Graded = 10000.0,
                totalR2Graded = 10000.0,
                totalR3Graded = 10000.0,
                totalR1Ungraded = 5000.0,
                totalR2Ungraded = 5000.0,
                totalR3Ungraded = 5000.0,
                totalR1Germinated = 12000.0,
                totalR2Germinated = 12000.0,
                totalR3Germinated = 11000.0,
                totalR1Ungerminated = 3000.0,
                totalR2Ungerminated = 3000.0,
                totalR3Ungerminated = 4000.0
            ),
            isSuccess = true
        )
    ).asStateFlow()
}
