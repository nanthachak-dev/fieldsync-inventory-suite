package com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.seed_batch

import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.StateFlow

interface IAddSeedBatchAdjustStockViewModel {
    val resourceUiState: StateFlow<ResourceUiState>
    val uiState: StateFlow<SeedBatchAdjustStockUiState>
    val isFormValid: StateFlow<Boolean>

    fun onVarietyChange(variety: String?)
    fun onYearChange(year: String)
    fun onSeasonChange(season: String?)
    fun onGenerationChange(generation: String?)
    fun onGradingChange(grading: String?)
    fun onGerminationChange(germination: String?)
    fun launchScreen()
    fun onOkClicked(navController: NavController? = null)
}
