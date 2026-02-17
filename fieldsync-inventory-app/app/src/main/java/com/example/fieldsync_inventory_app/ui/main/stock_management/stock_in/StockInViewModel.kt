package com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TOSeedBatchReqDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.stock_in.TOPurchaseItemRequestDTO
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.stock_in.TOPurchaseRequestDTO
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.stock_in.TOStockMovementStockInReqDTO
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.stock_in.TransactionOperationStockInReqDTO
import com.example.fieldsync_inventory_app.domain.model.StockMovementDetails
import com.example.fieldsync_inventory_app.domain.model.Supplier
import com.example.fieldsync_inventory_app.domain.use_case.auth.GetUserIdUseCase
import com.example.fieldsync_inventory_app.domain.use_case.last_sync.SyncChangesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.rice_generation.GetLocalRiceGenerationsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.rice_variety.GetLocalRiceVarietiesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.season.GetLocalSeasonsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.seed_batch.SyncSeedBatchesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_type.GetLocalStockMovementTypesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.supplier.GetLocalSuppliersUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details.GetLocalStockMovementDetailsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.transaction_operation.SaveTransactionOperationStockInUseCase
import com.example.fieldsync_inventory_app.domain.use_case.transaction_type.GetLocalStockTransactionTypesUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.model.SeedBatchStockInData
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.seed_batch.state.SeedBatchStockInUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.state.StockInUiState
import com.example.fieldsync_inventory_app.util.constans.StockInTask
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import com.example.fieldsync_inventory_app.util.view_model.startLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Year
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class StockInViewModel @Inject constructor(
    private val getLocalRiceVarietiesUseCase: GetLocalRiceVarietiesUseCase,
    private val getLocalSeasonsUseCase: GetLocalSeasonsUseCase,
    private val getLocalRiceGenerationsUseCase: GetLocalRiceGenerationsUseCase,
    private val getLocalStockMovementTypesUseCase: GetLocalStockMovementTypesUseCase,
    getLocalSupplierUseCase: GetLocalSuppliersUseCase,
    getLocalStockTransactionTypesUseCase: GetLocalStockTransactionTypesUseCase,
    getLocalStockMovementDetailsUseCase: GetLocalStockMovementDetailsUseCase,
    private val saveTransactionStockInUseCase: SaveTransactionOperationStockInUseCase,
    private val syncChangesUseCase: SyncChangesUseCase,
    private val syncSeedBatchesUseCase: SyncSeedBatchesUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
) : ViewModel(), IStockInViewModel {
    // UI State for error, success, and loading states
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState

    // UI State for seed batch and stock in screen
    private val _seedBatchUiState = MutableStateFlow(SeedBatchStockInUiState())
    override val seedBatchUiState: StateFlow<SeedBatchStockInUiState> = _seedBatchUiState
    private val _stockInUiState = MutableStateFlow(StockInUiState())
    override val stockInUiState: StateFlow<StockInUiState> = _stockInUiState

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override val seedBatchDataList = mutableStateListOf<SeedBatchStockInData>()

    private var nextId = 1
    private var editingBatchId: Int? = null

    private val transactionType = "STOCK_IN"
    private val transactionTypeRecords = getLocalStockTransactionTypesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private val supplierRecords = getLocalSupplierUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    // Load data to stockMovementDetailsList at declaration
    private val stockMovementDetailsList: StateFlow<List<StockMovementDetails>> =
        getLocalStockMovementDetailsUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )

    override fun firstLaunch() {
        loadInitialData()
    }

    override fun screenLaunch() {
        // Update transaction time
        _stockInUiState.update { it.copy(
            transactionTime = LocalDateTime.now(),
            selectedTask = StockInTask.PURCHASE
        ) }
        updateTotals()
    }

    // Load data from local database to seedBatchUiState
    private fun loadInitialData() {
        _resourceUiState.value = _resourceUiState.value.copy(isLoading = true)

        // Load local database tables to UI states
        val flow1 = combine(
            getLocalRiceVarietiesUseCase(),
            getLocalSeasonsUseCase(),
            getLocalRiceGenerationsUseCase()
        ) { varieties, seasons, generations ->
            Triple(varieties, seasons, generations)
        }

        val flow2 = combine(
            getLocalStockMovementTypesUseCase(),
        ) { stockMovementTypes ->
            (stockMovementTypes)
        }

        // Since the combine of coroutines can only
        // handle a maximum of 5 flows but we have 6 use cases which will take up to 6 flows
        // so, we create 2 groups of flows and combine them together
        combine(
            flow1,
            flow2
        ) { (varieties, seasons, generations), (stockMovementTypes) ->
            // Load to seedBatchUiState
            _seedBatchUiState.value = _seedBatchUiState.value.copy(
                riceVarieties = varieties,
                seasons = seasons,
                riceGenerations = generations
            )

            // Add an empty supplier to the list for empty selection (Future implementation)
            val emptySupplier = Supplier(
                id = -1,
                fullName = "",
                supplierTypeId = -1,
                supplierTypeName = "",
                email = null,
                phone = null,
                address = null,
                description = null
            )
            val suppliersWithEmpty = listOf(emptySupplier) + supplierRecords.value


            // Load to stockInUiState
            _stockInUiState.value = _stockInUiState.value.copy(
                supplierList = suppliersWithEmpty.map { it.fullName },
                stockMovementTypes = stockMovementTypes
            )
        }.launchIn(viewModelScope)

        _resourceUiState.value = _resourceUiState.value.copy(isLoading = false)
    }

    private fun addSeedBatch(seedBatch: SeedBatchStockInData) {
        seedBatchDataList.add(seedBatch)
    }

    private fun updateTotals() {
        val totalQuantity = seedBatchDataList.sumOf { it.quantity }
        val totalPrice = seedBatchDataList.sumOf { it.price * it.quantity }
        _stockInUiState.update {
            it.copy(
                totalQuantity = totalQuantity,
                totalPrice = totalPrice
            )
        }
    }

    // -- Add Seed Batch Screen UI Events --
    override fun onVarietyChange(variety: String?) {
        clearValidationError()
        _seedBatchUiState.value = _seedBatchUiState.value.copy(selectedVariety = variety)
    }

    override fun onYearChange(year: Int) {
        clearValidationError()
        _seedBatchUiState.value = _seedBatchUiState.value.copy(selectedYear = year)
    }

    override fun onSeasonChange(season: String?) {
        clearValidationError()
        _seedBatchUiState.value = _seedBatchUiState.value.copy(selectedSeason = season)
    }

    override fun onGenerationChange(generation: String?) {
        clearValidationError()
        _seedBatchUiState.value = _seedBatchUiState.value.copy(selectedGeneration = generation)
    }

    override fun onGradingChange(grading: String?) {
        clearValidationError()
        _seedBatchUiState.value = _seedBatchUiState.value.copy(selectedGrading = grading)
    }

    override fun onGerminationChange(germination: String?) {
        clearValidationError()
        _seedBatchUiState.value = _seedBatchUiState.value.copy(selectedGermination = germination)
    }

    override fun onQuantityChange(quantity: String?) {
        clearValidationError()
        _seedBatchUiState.value = _seedBatchUiState.value.copy(quantity = quantity)
    }

    override fun onPriceChange(price: String?) {
        clearValidationError()
        _seedBatchUiState.value = _seedBatchUiState.value.copy(price = price)
    }

    // Add new SeedBatchStockInData or edit existing one
    override fun onAddOrEditSeedBatchClicked(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val uiState = _seedBatchUiState.value

        if (uiState.isFormValid()) {
            var seedBatch = SeedBatchStockInData(
                id = editingBatchId ?: 0, // Will be replaced for new items
                varietyName = uiState.selectedVariety ?: "",
                varietyId = uiState.riceVarieties.find { it.name == uiState.selectedVariety }?.id
                    ?: 0,
                generation = uiState.selectedGeneration ?: "",
                generationId = uiState.riceGenerations.find { it.name == uiState.selectedGeneration }?.id
                    ?: 0,
                season = uiState.selectedSeason ?: "",
                seasonId = uiState.seasons.find { it.description == uiState.selectedSeason }?.id
                    ?: 0,
                year = uiState.selectedYear,
                grading = uiState.selectedGrading ?: "No",
                gradingValue = uiState.selectedGrading == "Yes",
                germination = uiState.selectedGermination ?: "No",
                germinationValue = uiState.selectedGermination == "Yes",
                totalStock = 0.0, // Update later
                quantity = uiState.quantity?.toDouble() ?: 0.0,
                price = uiState.price?.toDouble() ?: 0.0
            )
            // Update total stock
            seedBatch = seedBatch.copy(totalStock = getTotalStock(seedBatch))

            // Edit selected seed batch
            if (editingBatchId != null) {
                val index = seedBatchDataList.indexOfFirst { it.id == editingBatchId }
                if (index != -1) {
                    seedBatchDataList[index] = seedBatch
                }
            } else { // Create new seed batch
                addSeedBatch(seedBatch.copy(id = nextId++))
            }
            updateTotals()
            clearSeedBatchForm()
            onSuccess()
        } else {
            _resourceUiState.update { it.copy(error = "Please select all required fields (Variety, Year, Season, Grading, Germination, Price).") }
        }
    }

    // -- Stock In Screen UI Events --
    override fun onTaskChange(task: StockInTask?) {
        clearValidationError()
        _stockInUiState.update { it.copy(selectedTask = task) }
    }

    override fun onSupplierChange(supplier: String?) {
        clearValidationError()
        _stockInUiState.update { it.copy(selectedSupplier = supplier) }
    }

    override fun onDateTimeSelected(newDateTime: LocalDateTime) {
        clearValidationError()
        _stockInUiState.update { it.copy(transactionTime = newDateTime) }
    }

    override fun onNoteChange(note: String?) {
        clearValidationError()
        _stockInUiState.update { it.copy(note = note) }
    }

    override fun deleteSeedBatch(seedBatch: SeedBatchStockInData) {
        seedBatchDataList.remove(seedBatch)
        updateTotals()
    }

    // Edit when seed batch card is clicked
    override fun onEditClicked(seedBatch: SeedBatchStockInData) {
        editingBatchId = seedBatch.id
        _seedBatchUiState.update {
            it.copy(
                selectedVariety = seedBatch.varietyName,
                selectedYear = seedBatch.year,
                selectedSeason = seedBatch.season,
                selectedGeneration = seedBatch.generation,
                selectedGrading = seedBatch.grading,
                selectedGermination = seedBatch.germination,
                quantity = seedBatch.quantity.toString(),
                price = seedBatch.price.toString(),
                isEditing = true
            )
        }
    }

    // Submit data to the backend
    override fun onSubmitClicked() {
        processStockInOperation()
    }

    // Selected task must matches with stock movement type in database
    private fun getStockMovementTypeId(): Int {
        val selectedTask = stockInUiState.value.selectedTask.toString()
        val movementType =
            stockInUiState.value.stockMovementTypes.find { it.name == selectedTask }
        return movementType?.id ?: -1
    }

    // Purchase task
    private fun processStockInOperation(){
        // Reset resource UI state
        _resourceUiState.startLoading()

        // Show error if selected task is null
        val selectedTask = stockInUiState.value.selectedTask
        if (selectedTask == null) {
            _resourceUiState.endLoading("Please select a task")
            return
        }
        Log.d("StockInViewModel", "Using new processStockInOperation() with task: $selectedTask")

        // Get stock movement type id
        val stockMovementId = getStockMovementTypeId()
        if (stockMovementId == -1) {
            _resourceUiState.endLoading("Failed to get stock movement type ID for: $stockMovementId")
            return
        }

        // Create transaction operation request DTO
        val stockMovementRequestDtoList = createTOStockMovementStockInReqDTOList(seedBatchDataList)

        // Convert LocalDateTime to UTC in ISO format (for server)
        val isoDateTime =
            stockInUiState.value.transactionTime.atZone(ZoneId.systemDefault()).toInstant()
                .toString()

        // Generate transaction operation request DTO
        val transactionTypeId =
            transactionTypeRecords.value.find { it.name == transactionType }?.id
        if (transactionTypeId == null) {
            _resourceUiState.endLoading("Failed to get transaction type")
            return
        }

        // Submit data to the backend
        viewModelScope.launch {
            val userId = getUserIdUseCase()
            if (userId == 0) {
                Log.e("AdjustStockViewModel", "Failed to get user ID.")
                _resourceUiState.endLoading("Failed to get user ID.")
                return@launch
            }

            val transactionOperationRequestDto = TransactionOperationStockInReqDTO(
                transactionTypeId = transactionTypeId, // Placeholder for transaction type
                performedById = userId, // Placeholder for user ID
                transactionDate = isoDateTime,
                description = stockInUiState.value.note,
                purchase = createPurchaseRequestDto(),
                task = selectedTask,
                stockMovements = stockMovementRequestDtoList
            )

            if (stockMovementRequestDtoList.isEmpty()) {
                Log.e("AdjustStockViewModel", "At least one seed batch must be added.")
                _resourceUiState.endLoading("At least one seed batch must be added.")
                return@launch
            }

            try {
                saveTransactionStockInUseCase(transactionOperationRequestDto)
                Log.i("AdjustStockViewModel", "Transaction save attempt successfully.")
                syncChangesUseCase()
                syncSeedBatchesUseCase.sync() // To be replaced with sync changes seed batch in future update
                _resourceUiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                Log.e("AdjustStockViewModel", "Transaction save failed", e)
                _resourceUiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to save transaction operation: ${e.message}"
                    )
                }
            }
        }
    }

    // Helper functions for createTransactionOperationStockInRequestDto()
    private fun createPurchaseRequestDto(): TOPurchaseRequestDTO? {
        // Get supplier id
        val supplierId = supplierRecords.value.find { it.fullName == stockInUiState.value.selectedSupplier }?.id
        var description: String? = null

        if (supplierId == null && stockInUiState.value.selectedTask == StockInTask.PURCHASE) {
            description = "Unspecified Supplier"
        }

        if (supplierId == null && stockInUiState.value.selectedTask != StockInTask.PURCHASE) {
            return null
        }

        return TOPurchaseRequestDTO(
            stockTransactionId = null, // No need for create operation
            supplierId = supplierId,
            description = description
        )
    }

    private fun createTOStockMovementStockInReqDTOList(
        seedBatchCards: List<SeedBatchStockInData>
    ):List<TOStockMovementStockInReqDTO> {
        val stockMovementRequestDtoList = mutableListOf<TOStockMovementStockInReqDTO>()
        seedBatchCards.forEach { seedBatchCard ->
            val seedBatchReqDto = createSeedBatchRequestDto(seedBatchCard)
            val stockMovementRequestDto =
                createTOStockMovementStockInReqDTO(seedBatchReqDto, seedBatchCard)
            stockMovementRequestDtoList.add(stockMovementRequestDto)
        }
        return stockMovementRequestDtoList
    }

    // Helper for createTOStockMovementStockInReqDTOList()
    private fun createSeedBatchRequestDto(seedBatch: SeedBatchStockInData): TOSeedBatchReqDto {
        // Find seed batch with info in seedBatch, if seed batch not found
        // and the effect on stock of stock movement type is OUT then set error

        // Find seed batch Id or null if not found
        val seedBatchReqDto = TOSeedBatchReqDto(
            id = null, // Or existing seedBatchId
            varietyId = seedBatch.varietyId,
            generationId = seedBatch.generationId,
            seasonId = seedBatch.seasonId,
            year = seedBatch.year,
            grading = seedBatch.gradingValue,
            germination = seedBatch.germinationValue,
            description = null
        )

        return seedBatchReqDto
    }

    // Helper for createTOStockMovementStockInReqDTOList()
    private fun createTOStockMovementStockInReqDTO(
        seedBatchReqDto: TOSeedBatchReqDto, seedBatchCard:
        SeedBatchStockInData
    ):TOStockMovementStockInReqDTO{
        // Create purchase Item
        val stockMovementRequestDto = TOStockMovementStockInReqDTO(
            id = null,
            movementTypeId = getStockMovementTypeId(),
            seedBatch = seedBatchReqDto,
            purchaseItem = createPurchaseItemRequestDTO(seedBatchCard.price),
            quantity = seedBatchCard.quantity,
            description = "Stock in of: ${seedBatchCard.varietyName}"
        )
        return stockMovementRequestDto
    }

    // Helper for createTOStockMovementStockInReqDTO()
    private fun createPurchaseItemRequestDTO(price: Double): TOPurchaseItemRequestDTO {
        return TOPurchaseItemRequestDTO(
            stockMovementId = null,
            purchaseId = null,
            price = price,
            description = null
        )
    }

    // ------------- Helper Functions -------------
    override fun clearSeedBatchForm() {
        editingBatchId = null
        _seedBatchUiState.update {
            it.copy(
                selectedVariety = null,
                selectedYear = Year.now().value,
                selectedSeason = null,
                selectedGeneration = null,
                selectedGrading = null,
                selectedGermination = null,
                quantity = null,
                price = null,
                isEditing = false
            )
        }
    }

    override fun clearStockInScreen() {
        seedBatchDataList.clear()
        _stockInUiState.update {
            it.copy(
                selectedTask = null,
                selectedSupplier = null,
                note = null
            )
        }
        updateTotals()

        _resourceUiState.update {
            it.copy(
                error = null,
                isSuccess = false
            )
        }
    }

    // -- Helper Functions --
    private fun getTotalStock(seedBatch: SeedBatchStockInData): Double {
        val varietyId =
            stockMovementDetailsList.value.find { it.riceVarietyName == seedBatch.varietyName }?.riceVarietyId
        val generationId =
            stockMovementDetailsList.value.find { it.generationName == seedBatch.generation }?.generationId
        val seasonId =
            stockMovementDetailsList.value.find { it.seasonId == seedBatch.seasonId }?.seasonId
        val year =
            stockMovementDetailsList.value.find { it.seedBatchYear == seedBatch.year }?.seedBatchYear
        val grading =
            stockMovementDetailsList.value.find { it.seedBatchGrading == seedBatch.gradingValue }?.seedBatchGrading
        val germination =
            stockMovementDetailsList.value.find { it.seedBatchGermination == seedBatch.germinationValue }?.seedBatchGermination

        val seedBatchSearchResult = stockMovementDetailsList.value.filter {
            it.riceVarietyId == varietyId && it.generationId == generationId
                    && it.seasonId == seasonId && it.seedBatchYear == year
                    && it.seedBatchGrading == grading && it.seedBatchGermination == germination
        }

        if (seedBatchSearchResult.isEmpty()) {
            Log.d(
                "StockInViewModel.getTotalStock",
                "No seed batch record found with criteria -> varietyId: $varietyId, " +
                        "generationId: $generationId, seasonId: $seasonId, year: $year, grading: $grading, germination: $germination"
            )
            return -1.0 // Mark as no seed batch
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

    private fun clearValidationError() {
        _resourceUiState.value = _resourceUiState.value.copy(error = null)
    }
}
