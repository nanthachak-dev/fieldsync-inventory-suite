package com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.seed_batch

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.domain.model.RiceGeneration
import com.example.fieldsync_inventory_app.domain.model.RiceVariety
import com.example.fieldsync_inventory_app.domain.model.Season
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

class PreviewSelectSeedBatchViewModel() : ViewModel(), IAddSeedBatchAdjustStockViewModel {
    private val _uiState = MutableStateFlow(
        SeedBatchAdjustStockUiState(
            riceVarieties = listOf(
                RiceVariety(
                    id = 1, name = "Basmati", description = "Aromatic long-grain rice",
                    imageUrl = null, createdAt = 0, updatedAt = 0, deletedAt = null
                ),
                RiceVariety(
                    id = 2, name = "Jasmine", description = "Fragrant rice from Thailand",
                    imageUrl = null, createdAt = 0, updatedAt = 0, deletedAt = null
                )
            ),
            seasons = listOf(
                Season(
                    id = 1, name = "SEASON1", description = "Dummy season 1",
                    createdAt = 0, updatedAt = 0, deletedAt = null
                ),
                Season(
                    id = 2, name = "SEASON2", description = "Dummy season 2",
                    createdAt = 0, updatedAt = 0, deletedAt = null
                )
            ),
            riceGenerations = listOf(
                RiceGeneration(
                    id = 1, name = "Foundation", description = "Foundation seed",
                    createdAt = 0, updatedAt = 0, deletedAt = null
                ),
                RiceGeneration(
                    id = 2, name = "Registered", description = "Registered seed",
                    createdAt = 0, updatedAt = 0, deletedAt = null
                )
            ),
            selectedVariety = "Basmati",
            selectedYear = Calendar.getInstance().get(Calendar.YEAR).toString(),
            selectedSeason = "SEASON1",
            selectedGeneration = "Foundation",
            selectedGrading = "95",
            selectedGermination = "98"
        )
    )

    private val _resourceUiState = MutableStateFlow(
        ResourceUiState(
            error = "Please select all required fields (Variety, Year, Season, Grading, Germination, Condition)."
        )
    )
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState

    override val uiState: StateFlow<SeedBatchAdjustStockUiState> = _uiState
    override val isFormValid: StateFlow<Boolean> = MutableStateFlow(true)

    override fun onVarietyChange(variety: String?) {}

    override fun onYearChange(year: String) {}

    override fun onSeasonChange(season: String?) {}

    override fun onGenerationChange(generation: String?) {}

    override fun onGradingChange(grading: String?) {}

    override fun onGerminationChange(germination: String?) {}

    override fun onOkClicked(navController: NavController?) {}

    override fun launchScreen() {}
}