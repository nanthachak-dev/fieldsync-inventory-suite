package com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.seed_batch.state

import com.example.fieldsync_inventory_app.domain.model.RiceGeneration
import com.example.fieldsync_inventory_app.domain.model.RiceVariety
import com.example.fieldsync_inventory_app.domain.model.Season
import java.util.Calendar

data class SeedBatchStockOutUiState (
    val riceVarieties: List<RiceVariety> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val riceGenerations: List<RiceGeneration> = emptyList(),
    val selectedVariety: String? = null,
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val selectedSeason: String? = null,
    val selectedGeneration: String? = null,
    val selectedGrading: String? = null,
    val selectedGermination: String? = null,
    val quantity: String? = null,
    val price: String? = null,
    val isEditing: Boolean = false
){
    fun isFormValid(): Boolean {
        return !selectedVariety.isNullOrBlank() &&
                selectedYear.toString().isNotBlank() &&
                !selectedSeason.isNullOrBlank() &&
                !selectedGeneration.isNullOrBlank() &&
                !selectedGrading.isNullOrBlank() &&
                !selectedGermination.isNullOrBlank() &&
                (!quantity.isNullOrBlank() && quantity.toDouble() > 0.0) &&
                (!price.isNullOrBlank() && price.toDouble() >= 0.0)
    }
}