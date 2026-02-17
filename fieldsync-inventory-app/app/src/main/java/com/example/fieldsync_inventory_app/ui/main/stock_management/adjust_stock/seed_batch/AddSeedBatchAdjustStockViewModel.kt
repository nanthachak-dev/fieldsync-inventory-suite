package com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.seed_batch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.model.SeedBatch
import com.example.fieldsync_inventory_app.domain.use_case.rice_generation.GetLocalRiceGenerationsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.rice_variety.GetLocalRiceVarietiesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.season.GetLocalSeasonsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.seed_batch.GetLocalSeedBatchesUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.model.StockAdjustmentBatch
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AddSeedBatchAdjustStockViewModel @Inject constructor(
    private val getLocalRiceVarietiesUseCase: GetLocalRiceVarietiesUseCase,
    private val getLocalSeasonsUseCase: GetLocalSeasonsUseCase,
    private val getLocalRiceGenerationsUseCase: GetLocalRiceGenerationsUseCase,
    getLocalSeedBatchesUseCase: GetLocalSeedBatchesUseCase,
) : ViewModel(), IAddSeedBatchAdjustStockViewModel {

    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState

    private val _uiState = MutableStateFlow(SeedBatchAdjustStockUiState())
    override val uiState: StateFlow<SeedBatchAdjustStockUiState> = _uiState.asStateFlow()

    override val isFormValid: StateFlow<Boolean> = uiState.map {
        with(it) {
            !selectedVariety.isNullOrBlank() &&
                    !selectedYear.isBlank() &&
                    !selectedSeason.isNullOrBlank() &&
                    !selectedGeneration.isNullOrBlank() &&
                    !selectedGrading.isNullOrBlank() &&
                    !selectedGermination.isNullOrBlank()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // Load seed batch records from local database to seedBatchRecords
    private val seedBatchRecords: StateFlow<List<SeedBatch>> = getLocalSeedBatchesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private fun loadInitialData() {
        combine(
            getLocalRiceVarietiesUseCase(),
            getLocalSeasonsUseCase(),
            getLocalRiceGenerationsUseCase(),
        ) { varieties, seasons, generations ->
            _uiState.value = _uiState.value.copy(
                riceVarieties = varieties,
                seasons = seasons,
                riceGenerations = generations,
            )

            _resourceUiState.value = _resourceUiState.value.copy(
                isLoading = false,
                isSuccess = true
            )
        }.onEach {
            _resourceUiState.value = _resourceUiState.value.copy(isLoading = true)
        }.launchIn(viewModelScope)
    }

    override fun onVarietyChange(variety: String?) {
        _uiState.value = _uiState.value.copy(selectedVariety = variety)
        clearErrors()
    }

    override fun onYearChange(year: String) {
        _uiState.value = _uiState.value.copy(selectedYear = year)
        clearErrors()
    }

    override fun onSeasonChange(season: String?) {
        _uiState.value = _uiState.value.copy(selectedSeason = season)
        clearErrors()
    }

    override fun onGenerationChange(generation: String?) {
        _uiState.value = _uiState.value.copy(selectedGeneration = generation)
        clearErrors()
    }

    override fun onGradingChange(grading: String?) {
        _uiState.value = _uiState.value.copy(selectedGrading = grading)
        clearErrors()
    }

    override fun onGerminationChange(germination: String?) {
        _uiState.value = _uiState.value.copy(selectedGermination = germination)
        clearErrors()
    }

    override fun onOkClicked(navController: NavController?) {
        if (uiState.value.isFormValid) {
            _resourceUiState.value = _resourceUiState.value.copy(isSuccess = true)

            // Find seedBatchId
            val seedBatchId = findSeedBatch()
            if (seedBatchId == null) {
                Log.e(
                    "AddSeedBatchAdjustStockViewModel",
                    "Failed to find seed batch ID for the input data."
                )
                _resourceUiState.value = _resourceUiState.value.copy(
                    error = "Failed to find seed batch ID for the input data.",
                    isSuccess = false
                )
                return
            }
            Log.d(
                "AddSeedBatchAdjustStockViewModel",
                "seed batch id: $seedBatchId for from-seed-batch card"
            )

            navController?.popBackStack() // Go back to AdjustStockScreen
        } else {
            // SHOW ERROR
            _resourceUiState.value = _resourceUiState.value.copy(
                error = "Please select all required fields (Variety, Year, Season, Grading, Germination, Condition).",
                isSuccess = false
            )
        }
    }

    override fun launchScreen() {
        _uiState.value = SeedBatchAdjustStockUiState() // Clear data
        // Load data from local database
        loadInitialData()
        // Set initial inputs
        setInitInputs()
    }

    private fun setInitInputs() {
        if (!StockAdjustmentBatch.isCleared()) {
            onVarietyChange(StockAdjustmentBatch.INSTANCE.varietyName)
            onYearChange(StockAdjustmentBatch.INSTANCE.year.toString())
            onSeasonChange(StockAdjustmentBatch.INSTANCE.seasonDescription)
            onGenerationChange(StockAdjustmentBatch.INSTANCE.generation)
            onGradingChange(if (StockAdjustmentBatch.INSTANCE.grading) "Yes" else "No")
            onGerminationChange(if (StockAdjustmentBatch.INSTANCE.germination) "Yes" else "No")
        }
    }

    // -- Helper Functions --

    private fun findSeedBatch(): Long? {
        val uiState = _uiState.value
        val varietyId = uiState.riceVarieties.find { it.name == uiState.selectedVariety }?.id
        val generationId =
            uiState.riceGenerations.find { it.name == uiState.selectedGeneration }?.id
        val season = uiState.seasons.find { it.description == uiState.selectedSeason }
        val year = uiState.selectedYear.toInt()
        val grading = uiState.selectedGrading == "Yes"
        val germination = uiState.selectedGermination == "Yes"

        val seedBatchId = seedBatchRecords.value.find {
            it.varietyId == varietyId && it.generationId == generationId && it.seasonId == season?.id
                    && it.year == year && it.grading == grading && it.germination == germination
        }?.id

        // Set new data to StockAdjustmentBatch
        if (seedBatchId != null) {
            StockAdjustmentBatch.loadBatch(
                StockAdjustmentBatch(
                    seedBatchId = seedBatchId,
                    varietyName = uiState.selectedVariety!!,
                    varietyId = varietyId!!,
                    generation = uiState.selectedGeneration!!,
                    generationId = generationId!!,
                    season = season?.name!!,
                    seasonDescription = season.description,
                    seasonId = season.id,
                    year = year,
                    grading = grading,
                    germination = germination
                )
            )
        }

        return seedBatchId
    }

    // Clear errors
    private fun clearErrors() {
        _resourceUiState.endLoading()
    }

}
