package com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TOSeedBatchReqDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TOStockMovementReqDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TransactionOperationCreateReqDto
import com.example.fieldsync_inventory_app.domain.model.StockMovementDetails
import com.example.fieldsync_inventory_app.domain.model.StockMovementType
import com.example.fieldsync_inventory_app.domain.use_case.auth.GetUserIdUseCase
import com.example.fieldsync_inventory_app.domain.use_case.last_sync.SyncChangesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.rice_generation.GetRiceGenerationByNameUseCase
import com.example.fieldsync_inventory_app.domain.use_case.rice_variety.GetRiceVarietyByNameUseCase
import com.example.fieldsync_inventory_app.domain.use_case.season.GetSeasonByNameUseCase
import com.example.fieldsync_inventory_app.domain.use_case.seed_batch.SyncSeedBatchesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details.GetLocalStockMovementDetailsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_type.GetLocalStockMovementTypesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.transaction_operation.SaveTransactionOperationUseCase
import com.example.fieldsync_inventory_app.domain.use_case.transaction_type.GetLocalStockTransactionTypesUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.model.StockAdjustmentBatch
import com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.seed_batch.SeedBatchResult
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class AdjustStockViewModel @Inject constructor(
    private val saveTransactionUseCase: SaveTransactionOperationUseCase,
    private val getRiceVarietyByNameUseCase: GetRiceVarietyByNameUseCase,
    private val getRiceGenerationByNameUseCase: GetRiceGenerationByNameUseCase,
    private val getSeasonByNameUseCase: GetSeasonByNameUseCase,
    private val syncChangesUseCase: SyncChangesUseCase,
    private val syncSeedBatchesUseCase: SyncSeedBatchesUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    getLocalStockTransactionTypesUseCase: GetLocalStockTransactionTypesUseCase,
    getLocalStockMovementDetailsUseCase: GetLocalStockMovementDetailsUseCase,
    getLocalStockMovementTypesUseCase: GetLocalStockMovementTypesUseCase
) : ViewModel(), IAdjustStockViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState

    private val _state = MutableStateFlow(AdjustStockUiState())
    override val state: StateFlow<AdjustStockUiState> = _state.asStateFlow()

    private val transactionType = "ADJUSTMENT"
    private val transactionTypeRecords = getLocalStockTransactionTypesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    // Load data to stockTransactionDetails at declaration
    private val stockMovementDetailsState: StateFlow<List<StockMovementDetails>> =
        getLocalStockMovementDetailsUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )

    // Load data to stockMovementTypes at declaration
    private val stockMovementTypesState: StateFlow<List<StockMovementType>> =
        getLocalStockMovementTypesUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )

    private var seedBatchResult: SeedBatchResult? = null

    override fun screenLaunch() {
        if (!StockAdjustmentBatch.isCleared())
            updateSeedBatchCards(StockAdjustmentBatch.INSTANCE)

        // Update transaction time
        _state.update { it.copy(dateTime = LocalDateTime.now()) }
    }

    override fun onSuccessShown() {
        _resourceUiState.value = _resourceUiState.value.copy(isSuccess = false)
    }

    private fun calculateSeedBatchDataTo(
        seedBatchResult: SeedBatchResult?,
        selectedTask: String?
    ): SeedBatchResult? {
        if (seedBatchResult == null) return null
        val task = selectedTask ?: return null

        return if (task in listOf("Grading", "Germination")) {
            val oppositeValue = when (task) {
                "Grading" -> if (seedBatchResult.grading.equals(
                        "YES",
                        ignoreCase = true
                    )
                ) "No" else "Yes"

                "Germination" -> if (seedBatchResult.germination.equals(
                        "YES",
                        ignoreCase = true
                    )
                ) "No" else "Yes"

                else -> ""
            }
            seedBatchResult.copy(
                grading = if (task == "Grading") oppositeValue else seedBatchResult.grading,
                germination = if (task == "Germination") oppositeValue else seedBatchResult.germination,
            )
        } else {
            null
        }
    }

    override fun onTaskSelected(task: String) {
        _state.update { currentState ->
            val seedBatchTo = calculateSeedBatchDataTo(currentState.seedBatchDataFrom, task)

            currentState.copy(
                selectedTask = task,
                seedBatchDataTo = seedBatchTo
            )
        }

        // Update seed batch cards
        seedBatchResult?.let { updateSeedBatchCards(it) }

        _resourceUiState.update {
            it.copy(
                isSuccess = false,
                error = null
            )
        }
    }

    override fun onSeedBatchResultReceived(result: SeedBatchResult) {
    }

    override fun onDateTimeSelected(newDateTime: LocalDateTime) {
        _state.update { it.copy(dateTime = newDateTime) }
        _resourceUiState.update {
            it.copy(
                isSuccess = false,
                error = null
            )
        }
    }

    private suspend fun createSeedBatchReqDto(
        seedBatchResult: SeedBatchResult?,
        isFrom: Boolean = false
    ): TOSeedBatchReqDto? {
        if (seedBatchResult == null) return null

        val variety = getRiceVarietyByNameUseCase(seedBatchResult.varietyName).firstOrNull()
        val generation = getRiceGenerationByNameUseCase(seedBatchResult.generation).firstOrNull()
        val season = getSeasonByNameUseCase(seedBatchResult.season).firstOrNull()

        val yearInt = seedBatchResult.year.toIntOrNull()

        if (variety == null || generation == null || season == null || yearInt == null) {
            Log.e(
                "AdjustStockViewModel",
                "Failed to fetch all required IDs or parse year for DTO creation. Variety: $variety, Gen: $generation, Season: $season, Year: $yearInt"
            )
            return null
        }

        var seedBatchId: Long? = null
        if (isFrom) {
            seedBatchId = seedBatchResult.seedBatchId
        }

        return TOSeedBatchReqDto(
            id = seedBatchId,
            varietyId = variety.id,
            generationId = generation.id,
            seasonId = season.id,
            year = yearInt,
            grading = seedBatchResult.grading.equals("YES", ignoreCase = true),
            germination = seedBatchResult.germination.equals("YES", ignoreCase = true),
            description = null // TODO: Add description if available/needed
        )
    }

    private fun validateInputs(): String? {
        val currentState = _state.value
        if (currentState.seedBatchDataFrom == null) {
            return "Source seed batch cannot be empty."
        }

        val quantity = currentState.quantity.toDoubleOrNull()
        if (quantity == null) {
            return "Invalid quantity entered."
        }

        if (quantity <= 0) {
            return "Quantity cannot be less than or equal to 0."
        }

        val loss = currentState.loss.toDoubleOrNull()
        if (loss != null) {
            if (loss > quantity) {
                return "Loss cannot be greater than quantity."
            }
            if (loss < 0) {
                return "Loss cannot be less than 0."
            }
        }
        return null
    }


    override fun onSubmitClicked() {
        _resourceUiState.update {
            it.copy(
                isLoading = true,
                isSuccess = false,
                error = null
            )
        }

        val validationError = validateInputs()
        if (validationError != null) {
            _resourceUiState.update {
                it.copy(
                    isLoading = false,
                    error = validationError
                )
            }
            return
        }



        // Check over stock
        val seedBatchId = seedBatchResult?.seedBatchId
        if (seedBatchId != null) {
            val (isOverStocked, message) = overStockCheck(
                seedBatchId,
                _state.value.quantity.toDouble()
            )
            if (isOverStocked) {
                _resourceUiState.update {
                    it.copy(
                        isLoading = false,
                        error = message
                    )
                }
                return
            }
        }

        val currentState = _state.value
        val quantity = currentState.quantity.toDouble()
        val loss = currentState.loss.toDoubleOrNull() ?: 0.0
        val quantityTo = quantity - loss


        viewModelScope.launch {
            // Get user id
            val userId = getUserIdUseCase()
            if (userId == 0) {
                Log.e("AdjustStockViewModel", "Failed to get user ID.")
                _resourceUiState.endLoading("Failed to get user ID.")
                return@launch
            }

            val fromSeedBatchDto = createSeedBatchReqDto(currentState.seedBatchDataFrom, true)
            if (fromSeedBatchDto == null) {
                Log.e("AdjustStockViewModel", "Failed to create DTO for seedBatchDataFrom.")
                _resourceUiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to prepare source seed batch data."
                    )
                }
                return@launch
            }

            // Get stock movement type id
            val fromMovementTypeId = getStockMovementTypeId(EnumStockMovementType.ADJUSTMENT_OUT)
            val toMovementTypeId = getStockMovementTypeId(EnumStockMovementType.ADJUSTMENT_IN)
            if (fromMovementTypeId == 0 || toMovementTypeId == 0) {
                Log.e("AdjustStockViewModel", "Failed to get stock movement type IDs.")
                _resourceUiState.endLoading("Failed to get stock movement type IDs.")
                return@launch
            }

            // Create stock movements

            val stockMovements = mutableListOf<TOStockMovementReqDto>()

            stockMovements.add(
                TOStockMovementReqDto(
                    id = null,
                    movementTypeId = fromMovementTypeId, // Adjust Stock-Out
                    seedBatch = fromSeedBatchDto,
                    saleItem = null,
                    quantity = quantity,
                    description = "Stock adjustment from: ${currentState.seedBatchDataFrom?.stockDisplay}"
                )
            )

            if (currentState.seedBatchDataTo != null) {
                val toSeedBatchDto = createSeedBatchReqDto(currentState.seedBatchDataTo)
                if (toSeedBatchDto == null) {
                    Log.e("AdjustStockViewModel", "Failed to create DTO for seedBatchDataTo.")
                    _resourceUiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Failed to prepare destination seed batch data."
                        )
                    }
                    return@launch
                }
                stockMovements.add(
                    TOStockMovementReqDto(
                        id = null,
                        movementTypeId = toMovementTypeId, // Adjust Stock-In
                        seedBatch = toSeedBatchDto,
                        saleItem = null,
                        quantity = quantityTo,
                        description = "Stock loss during adjustment: $loss"
                    )
                )
            } else {
                Log.e("AdjustStockViewModel", "Destination seed batch data is null.")
                _resourceUiState.update {
                    it.copy(
                        error = "Please select task.",
                        isLoading = false
                    )
                }
                return@launch
            }

            if (stockMovements.isEmpty()) {
                Log.e("AdjustStockViewModel", "No stock movements to process.")
                _resourceUiState.update {
                    it.copy(
                        error = "No stock movements to process.",
                        isLoading = false
                    )
                }
                return@launch
            }

            // Convert LocalDateTime to UTC in ISO format
            val isoDateTime =
                currentState.dateTime.atZone(ZoneId.systemDefault()).toInstant().toString()

            val transactionTypeId =
                transactionTypeRecords.value.find { it.name == transactionType }?.id
            if (transactionTypeId == null) {
                _resourceUiState.update {
                    it.copy(
                        error = "Failed to get transaction type",
                        isLoading = false
                    )
                }
                return@launch
            }

            val transactionDto = TransactionOperationCreateReqDto(
                transactionTypeId = transactionTypeId,
                performedById = userId,
                transactionDate = isoDateTime,
                description = currentState.reason.ifEmpty { null },
                saleRequestDTO = null,
                stockMovements = stockMovements
            )

            try {
                saveTransactionUseCase(transactionDto)
                Log.i("AdjustStockViewModel.onSubmitClicked", "Transaction save attempt successfully.")
                // Sync changes
                syncChangesUseCase()
                syncSeedBatchesUseCase.sync() // To be replaced with sync changes seed batch in future update
                _resourceUiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            } catch (e: Exception) {
                Log.e("AdjustStockViewModel", "Transaction save failed", e)
                _resourceUiState.update {
                    it.copy(
                        error = "Failed to save adjustment: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    override fun onQuantityChanged(newQuantity: String) {
        _state.update { it.copy(quantity = newQuantity) }

        // Update seed batch cards
        seedBatchResult?.let { updateSeedBatchCards(it) }

        _resourceUiState.update {
            it.copy(
                error = null,
                isSuccess = false
            )
        }
    }

    override fun onLossChanged(newLoss: String) {
        _state.update { it.copy(loss = newLoss) }

        // Update seed batch cards
        seedBatchResult?.let { updateSeedBatchCards(it) }

        _resourceUiState.update {
            it.copy(
                error = null,
                isSuccess = false
            )
        }
    }

    override fun onReasonChanged(newReason: String) {
        _state.update { it.copy(reason = newReason) }
        _resourceUiState.update {
            it.copy(
                error = null,
                isSuccess = false
            )
        }
    }

    override fun clearForm() {
        _state.value = AdjustStockUiState()
        StockAdjustmentBatch.clear()
    }

    private fun updateSeedBatchCards(result: SeedBatchResult) {
        seedBatchResult = result

        _state.update { currentState ->
            val seedBatchTo = calculateSeedBatchDataTo(result, currentState.selectedTask)
            currentState.copy(
                seedBatchDataFrom = result,
                seedBatchDataTo = seedBatchTo
            )
        }
        // Update stock display
        val stockDisplayFrom = getStockDisplay(true)
        val stockDisplayTo = getStockDisplay(false)
        _state.update {
            it.copy(
                seedBatchDataFrom = it.seedBatchDataFrom?.copy(stockDisplay = stockDisplayFrom),
                seedBatchDataTo = it.seedBatchDataTo?.copy(stockDisplay = stockDisplayTo)
            )
        }

        _resourceUiState.update {
            it.copy(
                isSuccess = false,
                error = null
            )
        }
    }

    // New version
    private fun updateSeedBatchCards(stockAdjustmentBatch: StockAdjustmentBatch) {
        // Transfer stock adjustment batch to seed batch result
        val result = SeedBatchResult(
            seedBatchId = stockAdjustmentBatch.seedBatchId,
            varietyName = stockAdjustmentBatch.varietyName,
            //varietyId = stockAdjustmentBatch.varietyId,
            generation = stockAdjustmentBatch.generation,
            //generationId = stockAdjustmentBatch.generationId,
            season = stockAdjustmentBatch.season,
            //seasonId = stockAdjustmentBatch.seasonId,
            year = stockAdjustmentBatch.year.toString(),
            grading = if (stockAdjustmentBatch.grading) "Yes" else "No",
            germination = if (stockAdjustmentBatch.germination) "Yes" else "No",
            stockDisplay = "N/A" // Placeholder
        )
        seedBatchResult = result

        _state.update { currentState ->
            val seedBatchTo = calculateSeedBatchDataTo(result, currentState.selectedTask)
            currentState.copy(
                seedBatchDataFrom = result,
                seedBatchDataTo = seedBatchTo
            )
        }
        // Update stock display
        val stockDisplayFrom = getStockDisplay(true)
        val stockDisplayTo = getStockDisplay(false)
        _state.update {
            it.copy(
                seedBatchDataFrom = it.seedBatchDataFrom?.copy(stockDisplay = stockDisplayFrom),
                seedBatchDataTo = it.seedBatchDataTo?.copy(stockDisplay = stockDisplayTo)
            )
        }

        _resourceUiState.update {
            it.copy(
                isSuccess = false,
                error = null
            )
        }
    }

    private fun overStockCheck(seedBatchId: Long, totalQuantity: Double): Pair<Boolean, String> {
        // Check each seed batch in the cart against the database
        val seedBatchFrom = _state.value.seedBatchDataFrom
        val availableStock = if (seedBatchFrom != null) getTotalStock(seedBatchFrom) else 0.0

        if (totalQuantity > availableStock) {
            val errorMessage =
                "Over stock for batch $seedBatchId. Available: $availableStock, Requested: $totalQuantity"
            return Pair(true, errorMessage)
        }

        return Pair(false, "") // No over stock issue
    }

    private fun getTotalStock(seedBatch: SeedBatchResult): Double {
        val varietyId =
            stockMovementDetailsState.value.find { it.riceVarietyName == seedBatch.varietyName }?.riceVarietyId
        val generationId =
            stockMovementDetailsState.value.find { it.generationName == seedBatch.generation }?.generationId
        val seasonId =
            stockMovementDetailsState.value.find { it.seasonName == seedBatch.season }?.seasonId
        val year =
            stockMovementDetailsState.value.find { it.seedBatchYear == seedBatch.year.toInt() }?.seedBatchYear
        val cardGrading = seedBatch.grading == "Yes" // Helper val
        val cardGermination = seedBatch.germination == "Yes" // Helper val
        val grading =
            stockMovementDetailsState.value.find { it.seedBatchGrading == cardGrading }?.seedBatchGrading
        val germination =
            stockMovementDetailsState.value.find { it.seedBatchGermination == cardGermination }?.seedBatchGermination

        val seedBatchSearchResult = stockMovementDetailsState.value.filter {
            it.riceVarietyId == varietyId && it.generationId == generationId
                    && it.seasonId == seasonId && it.seedBatchYear == year
                    && it.seedBatchGrading == grading && it.seedBatchGermination == germination
        }

        if (seedBatchSearchResult.isEmpty()) {
            Log.d(
                "AdjustStockViewModel.getTotalStock",
                "No seed batch record found with criteria -> varietyId: $varietyId, " +
                        "generationId: $generationId, seasonId: $seasonId, year: $year, grading: $grading, germination: $germination"
            )
            return 0.0
        } else {
            val availableStock = seedBatchSearchResult.sumOf { detail ->
                if (detail.movementTypeEffectOnStock == "IN") {
                    detail.stockMovementQuantity
                } else {
                    -detail.stockMovementQuantity
                }
            }
            // Prevent long decimal places e.g. make 124.4499999999999999999 to 124.45
            val roundedAvailableStock = round(availableStock * 100.0) / 100.0

            return roundedAvailableStock
        }
    }

    // Get stock display
    private fun getStockDisplay(isFrom: Boolean): String {
        val quantity = _state.value.quantity.toDoubleOrNull() ?: 0.0
        val loss = _state.value.loss.toDoubleOrNull() ?: 0.0

        val numberFormat = NumberFormat.getNumberInstance(Locale.US)

        if (isFrom) {
            val seedBatchDataFrom = _state.value.seedBatchDataFrom
            Log.d("AdjustStockViewModel.getStockDisplay", "seedBatchDataFrom: $seedBatchDataFrom")
            val totalStockFrom =
                if (seedBatchDataFrom != null) getTotalStock(seedBatchDataFrom) else 0.0

            return if (quantity == 0.0) "Stock: $totalStockFrom Kg"
            else "Stock:  $totalStockFrom - $quantity = ${numberFormat.format(totalStockFrom - quantity)} Kg"
        } else {
            val seedBatchDataTo = _state.value.seedBatchDataTo
            val totalStockTo =
                if (seedBatchDataTo != null) getTotalStock(seedBatchDataTo) else 0.0

            return if (quantity == 0.0) "Stock: $totalStockTo Kg"
            else "Stock: $totalStockTo + $quantity - $loss = ${numberFormat.format(totalStockTo + quantity - loss)} Kg"
        }
    }

    // Get stock movement type id
    private fun getStockMovementTypeId(movementType: EnumStockMovementType): Int {
        // Find movement type id in database based on the enum value
        return stockMovementTypesState.value.find { it.name == movementType.name }?.id ?: 0
    }
}

enum class EnumStockMovementType {
    ADJUSTMENT_IN, ADJUSTMENT_OUT
}
