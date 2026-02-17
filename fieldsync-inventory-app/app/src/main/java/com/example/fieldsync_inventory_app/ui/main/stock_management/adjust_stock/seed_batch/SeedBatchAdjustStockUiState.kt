package com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.seed_batch

import com.example.fieldsync_inventory_app.domain.model.RiceGeneration
import com.example.fieldsync_inventory_app.domain.model.RiceVariety
import com.example.fieldsync_inventory_app.domain.model.Season
import java.util.Calendar

data class SeedBatchAdjustStockUiState(
    val riceVarieties: List<RiceVariety> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val riceGenerations: List<RiceGeneration> = emptyList(),
    val selectedVariety: String? = null,
    val selectedYear: String = Calendar.getInstance().get(Calendar.YEAR).toString(),
    val selectedSeason: String? = null,
    val selectedGeneration: String? = null,
    val selectedGrading: String? = null,
    val selectedGermination: String? = null
) {
    val isFormValid: Boolean
        get() = !selectedVariety.isNullOrBlank() &&
                selectedYear.isNotBlank() &&
                !selectedSeason.isNullOrBlank() &&
                !selectedGeneration.isNullOrBlank() &&
                !selectedGrading.isNullOrBlank() &&
                !selectedGermination.isNullOrBlank()
}