package com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TOSaleItemReqDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TOSaleReqDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TOSeedBatchReqDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TOStockMovementReqDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TransactionOperationCreateReqDto
import com.example.fieldsync_inventory_app.domain.model.Customer
import com.example.fieldsync_inventory_app.domain.model.SeedBatch
import com.example.fieldsync_inventory_app.domain.model.StockMovementDetails
import com.example.fieldsync_inventory_app.domain.use_case.auth.GetUserIdUseCase
import com.example.fieldsync_inventory_app.domain.use_case.customer.GetLocalCustomersUseCase
import com.example.fieldsync_inventory_app.domain.use_case.last_sync.SyncChangesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.rice_generation.GetLocalRiceGenerationsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.rice_variety.GetLocalRiceVarietiesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.season.GetLocalSeasonsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.seed_batch.GetLocalSeedBatchesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_type.GetLocalStockMovementTypesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details.GetLocalStockMovementDetailsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.transaction_operation.SaveTransactionOperationUseCase
import com.example.fieldsync_inventory_app.domain.use_case.transaction_type.GetLocalStockTransactionTypesUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.model.SaleCartDataHolder
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.model.SeedBatchStockOutData
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.seed_batch.state.SeedBatchStockOutUiState
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.state.StockOutUiState
import com.example.fieldsync_inventory_app.util.constans.StockOutTask
import com.example.fieldsync_inventory_app.util.view_model.endLoading
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
class StockOutViewModel @Inject constructor(
    private val getLocalRiceVarietiesUseCase: GetLocalRiceVarietiesUseCase,
    private val getLocalSeasonsUseCase: GetLocalSeasonsUseCase,
    private val getLocalRiceGenerationsUseCase: GetLocalRiceGenerationsUseCase,
    private val getLocalCustomersUseCase: GetLocalCustomersUseCase,
    private val getLocalStockMovementTypesUseCase: GetLocalStockMovementTypesUseCase,
    getLocalStockTransactionTypesUseCase: GetLocalStockTransactionTypesUseCase,
    private val saveTransactionUseCase: SaveTransactionOperationUseCase,
    private val syncChangesUseCase: SyncChangesUseCase,
    getLocalSeedBatchesUseCase: GetLocalSeedBatchesUseCase,
    getLocalStockMovementDetailsUseCase: GetLocalStockMovementDetailsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel(), IStockOutViewModel {

    // UI State for error, success, and loading states
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState
    private val _seedBatchUiState = MutableStateFlow(SeedBatchStockOutUiState())
    override val seedBatchUiState: StateFlow<SeedBatchStockOutUiState> = _seedBatchUiState

    private val _stockOutUiState = MutableStateFlow(StockOutUiState())
    override val stockOutUiState: StateFlow<StockOutUiState> = _stockOutUiState

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override val stockOutCartItems = SaleCartDataHolder.getCartItems()
    private var editingBatchCardId: Int? = null

    private val transactionType = "STOCK_OUT"
    private val transactionTypeRecords = getLocalStockTransactionTypesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    // Load data to seedBatchRecords upon initialization
    private val seedBatchRecords: StateFlow<List<SeedBatch>> = getLocalSeedBatchesUseCase()
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
        _stockOutUiState.update { it.copy(
            transactionTime = LocalDateTime.now(),
            selectedTask = StockOutTask.SALE
        ) }
        updateTotals()
    }

    private val stockMovementDetailsList: StateFlow<List<StockMovementDetails>> =
        getLocalStockMovementDetailsUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )

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
            getLocalCustomersUseCase(),
            getLocalStockMovementTypesUseCase()
        ) { customers, stockMovementTypes ->
            Pair(customers, stockMovementTypes)
        }

        // Since the combine of coroutines can only
        // handle a maximum of 5 flows but we have 6 use cases which will take up to 6 flows
        // so, we create 2 groups of flows and combine them together
        combine(
            flow1,
            flow2
        ) { (varieties, seasons, generations), (customers, stockMovementTypes) ->
            // Load to seedBatchUiState
            _seedBatchUiState.value = _seedBatchUiState.value.copy(
                riceVarieties = varieties,
                seasons = seasons,
                riceGenerations = generations
            )

            // Add an empty customer to the list for empty selection
            val emptyCustomer = Customer(
                id = -1,
                fullName = "",
                customerTypeId = 0,
                customerTypeName = "",
                email = null,
                phone = null,
                address = null,
                createdAt = 0,
                updatedAt = 0,
                deletedAt = null
            )
            val customersWithEmpty = listOf(emptyCustomer) + customers

            // Load to stockOutUiState
            _stockOutUiState.value = _stockOutUiState.value.copy(
                customerList = customersWithEmpty,
                stockMovementTypes = stockMovementTypes
            )
        }.launchIn(viewModelScope)

        _resourceUiState.value = _resourceUiState.value.copy(isLoading = false)
    }

    private fun addSeedBatch(seedBatch: SeedBatchStockOutData) {
        SaleCartDataHolder.addItem(seedBatch)
    }

    private fun updateTotals() {
        val totalQuantity = stockOutCartItems.sumOf { it.quantity }
        val totalPrice = stockOutCartItems.sumOf { it.price * it.quantity }
        _stockOutUiState.update {
            it.copy(
                totalQuantity = totalQuantity,
                totalPrice = totalPrice
            )
        }
    }

    // -- Add Seed Batch Screen --
    override fun onVarietyChange(variety: String?) {
        _resourceUiState.value = _resourceUiState.value.copy(error = null)
        _seedBatchUiState.value = _seedBatchUiState.value.copy(selectedVariety = variety)
    }

    override fun onYearChange(year: Int) {
        _resourceUiState.value = _resourceUiState.value.copy(error = null)
        _seedBatchUiState.value = _seedBatchUiState.value.copy(selectedYear = year)
    }

    override fun onSeasonChange(season: String?) {
        _resourceUiState.value = _resourceUiState.value.copy(error = null)
        _seedBatchUiState.value = _seedBatchUiState.value.copy(selectedSeason = season)
    }

    override fun onGenerationChange(generation: String?) {
        _resourceUiState.value = _resourceUiState.value.copy(error = null)
        _seedBatchUiState.value = _seedBatchUiState.value.copy(selectedGeneration = generation)
    }

    override fun onGradingChange(grading: String?) {
        _resourceUiState.value = _resourceUiState.value.copy(error = null)
        _seedBatchUiState.value = _seedBatchUiState.value.copy(selectedGrading = grading)
    }

    override fun onGerminationChange(germination: String?) {
        _resourceUiState.value = _resourceUiState.value.copy(error = null)
        _seedBatchUiState.value = _seedBatchUiState.value.copy(selectedGermination = germination)
    }

    override fun onQuantityChange(quantity: String?) {
        _resourceUiState.value = _resourceUiState.value.copy(error = null)
        _seedBatchUiState.value = _seedBatchUiState.value.copy(quantity = quantity)
    }

    override fun onPriceChange(price: String?) {
        _resourceUiState.value = _resourceUiState.value.copy(error = null)
        _seedBatchUiState.value = _seedBatchUiState.value.copy(price = price)
    }

    // Add new SeedBatchStockOutData or edit existing one
    override fun onAddOrEditSeedBatchClicked(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val uiState = _seedBatchUiState.value

        if (uiState.isFormValid()) {
            // Find seed batch id with data in uiState
            val seedBatchId = findSeedBatch()

            Log.d("StockOutViewModel", "seed batch id: $seedBatchId for creating card")

            if (seedBatchId == null) { // And effect on stock is OUT
                _resourceUiState.endLoading("Seed batch not found for entered data.")
                return
            }

            var seedBatch = SeedBatchStockOutData(
                id = editingBatchCardId ?: 0, // Will be replaced for new items
                seedBatchId = seedBatchId,
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
                quantity = uiState.quantity?.toDouble() ?: 0.0,
                totalStock = 0.0, // Update later
                price = uiState.price?.toDouble() ?: 0.0
            )

            // Update total stock
            seedBatch = seedBatch.copy(totalStock = getTotalStock(seedBatch))

            // Edit selected seed batch
            if (editingBatchCardId != null) {
                val index = stockOutCartItems.indexOfFirst { it.id == editingBatchCardId }
                if (index != -1) {
                    stockOutCartItems[index] = seedBatch
                }
            } else { // Create new seed batch
                addSeedBatch(seedBatch)
            }
            updateTotals()
            clearSeedBatchForm()
            onSuccess()
            _resourceUiState.value = _resourceUiState.value.copy(error = null)
        } else {
            _resourceUiState.value = _resourceUiState.value.copy(error = "Please select all required fields (Variety, Year, Season, Grading, Germination, Price).")
        }
    }

    // -- Stock Out Screen --
    override fun onTaskChange(task: StockOutTask?) {
        _stockOutUiState.update { it.copy(selectedTask = task) }
    }

    override fun onCustomerChange(customer: String?) {
        _stockOutUiState.update { it.copy(selectedCustomer = customer) }
    }

    override fun onDateTimeSelected(newDateTime: LocalDateTime) {
        _stockOutUiState.update { it.copy(transactionTime = newDateTime) }
    }

    override fun onNoteChange(note: String?) {
        _stockOutUiState.update { it.copy(note = note) }
    }

    override fun deleteSeedBatch(seedBatch: SeedBatchStockOutData) {
        SaleCartDataHolder.removeItem(seedBatch)
        updateTotals()
    }

    // Edit when seed batch card is clicked
    override fun onEditClicked(seedBatch: SeedBatchStockOutData) {
        editingBatchCardId = seedBatch.id
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
        // Reset resource UI state
        _resourceUiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

        // Show error if selected task is null
        if (stockOutUiState.value.selectedTask == null) {
            _resourceUiState.update {
                it.copy(
                    error = "Please select a task",
                    isLoading = false
                )
            }
            return
        }

        val (isOverStocked, message) = overStockCheck()
        if (isOverStocked) {
            _resourceUiState.update {
                it.copy(
                    isLoading = false,
                    error = message
                )
            }
            return
        }

        viewModelScope.launch {
            val userId = getUserIdUseCase()
            if (userId == 0) {
                Log.e("AdjustStockViewModel", "Failed to get user ID.")
                _resourceUiState.endLoading("Failed to get user ID.")
                return@launch
            }

            // Get stock movement type id
            val stockMovementId = getStockMovementTypeId()
            if (stockMovementId == -1) {
                _resourceUiState.update {
                    it.copy(
                        error = "Failed to get stock movement type ID for: $stockMovementId",
                        isLoading = false
                    )
                }
                return@launch
            }

            // Create transaction operation request DTO
            val stockMovementRequestDtoList = createStockMovementRequestDtoList(stockOutCartItems)

            // Convert LocalDateTime to UTC in ISO format (for server)
            val isoDateTime =
                stockOutUiState.value.transactionTime.atZone(ZoneId.systemDefault()).toInstant().toString()

            // Define sale request DTO if sell task is selected
            var saleRequestDto: TOSaleReqDto? = null
            val selectedTask = stockOutUiState.value.selectedTask
            val selectedCustomer = stockOutUiState.value.selectedCustomer
            if (selectedTask == StockOutTask.SALE || selectedCustomer != null) {
                saleRequestDto = createSaleRequestDto()
            }

            // Generate transaction operation request DTO
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

            // Generate transaction operation request DTO
            val transactionOperationRequestDto = TransactionOperationCreateReqDto(
                transactionTypeId = transactionTypeId, // Placeholder for transaction type
                performedById = userId, // Placeholder for user ID
                transactionDate = isoDateTime,
                description = stockOutUiState.value.note,
                saleRequestDTO = saleRequestDto,
                stockMovements = stockMovementRequestDtoList
            )
            if (stockMovementRequestDtoList.isEmpty()) {
                Log.e("AdjustStockViewModel", "At least one seed batch must be added.")
                _resourceUiState.update {
                    it.copy(
                        error = "At least one seed batch must be added.",
                        isLoading = false
                    )
                }
                return@launch
            }

            try {
                saveTransactionUseCase(transactionOperationRequestDto)
                Log.i("AdjustStockViewModel", "Transaction save attempt successful.")
                syncChangesUseCase()
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

    // ------------- Transaction Operation Request DTO Helper Functions -------------

    // Create sale item request DTO (To be updated for edit operation)
    private fun createSaleItemRequestDto(price: Double): TOSaleItemReqDto {
        val saleItemRequestDto = TOSaleItemReqDto(
            stockMovementId = null,
            saleId = null,
            price = price,
            description = null
        )

        return saleItemRequestDto
    }

    // Create sale request DTO (To be updated for edit operation)
    private fun createSaleRequestDto(): TOSaleReqDto {
        var customerId: Int? = null
        if (!stockOutUiState.value.selectedCustomer.isNullOrEmpty()) {
            customerId =
                stockOutUiState.value.customerList.find {
                    it.fullName ==
                            stockOutUiState.value.selectedCustomer
                }?.id
        }

        val saleRequestDto = TOSaleReqDto(
            stockTransactionId = null,
            customerId = customerId,
            description = null
        )

        return saleRequestDto
    }

    private fun createSeedBatchRequestDto(seedBatch: SeedBatchStockOutData): TOSeedBatchReqDto {
        val seedBatchReqDto = TOSeedBatchReqDto(
            id = seedBatch.seedBatchId,
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

    // Create stock movement request DTO
    private fun createStockMovementRequestDto(
        seedBatchReqDto: TOSeedBatchReqDto, seedBatchCard:
        SeedBatchStockOutData
    ): TOStockMovementReqDto {
        val stockMovementRequestDto = TOStockMovementReqDto(
            id = null,
            movementTypeId = getStockMovementTypeId(),
            seedBatch = seedBatchReqDto,
            saleItem = createSaleItemRequestDto(seedBatchCard.price),
            quantity = seedBatchCard.quantity,
            description = "Stock out of: ${seedBatchCard.varietyName}"
        )
        return stockMovementRequestDto
    }

    // Create list of stock movement request DTOs
    private fun createStockMovementRequestDtoList(
        seedBatchCards: List<SeedBatchStockOutData>
    ): List<TOStockMovementReqDto> {
        val stockMovementRequestDtoList = mutableListOf<TOStockMovementReqDto>()
        seedBatchCards.forEach { seedBatchCard ->
            val seedBatchReqDto = createSeedBatchRequestDto(seedBatchCard)
            val stockMovementRequestDto =
                createStockMovementRequestDto(seedBatchReqDto, seedBatchCard)
            stockMovementRequestDtoList.add(stockMovementRequestDto)
        }
        return stockMovementRequestDtoList
    }

    // Selected task must matches with stock movement type in database
    private fun getStockMovementTypeId(): Int {
        val selectedTask = stockOutUiState.value.selectedTask.toString()
        val movementType =
            stockOutUiState.value.stockMovementTypes.find { it.name == selectedTask }
        return movementType?.id ?: -1
    }

    // ------------- Helper Functions -------------
    override fun clearSeedBatchForm() {
        editingBatchCardId = null
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

    override fun clearStockOutScreen() {
        SaleCartDataHolder.clearCart()
        _stockOutUiState.update {
            it.copy(
                selectedTask = null,
                selectedCustomer = null,
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

    private fun findSeedBatch(): Long? {
        val uiState = _seedBatchUiState.value
        val varietyId = uiState.riceVarieties.find { it.name == uiState.selectedVariety }?.id
        val generationId =
            uiState.riceGenerations.find { it.name == uiState.selectedGeneration }?.id
        val seasonId = uiState.seasons.find { it.description == uiState.selectedSeason }?.id
        val year = uiState.selectedYear
        val grading = uiState.selectedGrading == "Yes"
        val germination = uiState.selectedGermination == "Yes"

        Log.d("StockOutViewModel.findSeedBatch", "varietyId: $varietyId, generationId: $generationId, seasonId: $seasonId, year: $year, grading: $grading, germination: $germination")

        return seedBatchRecords.value.find { it.varietyId == varietyId && it.generationId == generationId && it.seasonId == seasonId
                && it.year == year && it.grading == grading && it.germination == germination }?.id
    }

    /**
     * Check if the total quantity of any seed batch in the cart
     * exceeds the available stock.
     * @return A pair where the first element is a boolean (true if overstocked, false otherwise)
     *         and the second element is a message indicating which item is overstocked.
     */
    private fun overStockCheck(): Pair<Boolean, String> {
        // Group all items in the cart by seedBatchId and sum their quantities
        val quantitiesInCart = stockOutCartItems
            .groupBy { it.seedBatchId }
            .mapValues { entry -> entry.value.sumOf { it.quantity } }

        // Check each seed batch in the cart against the database
        for ((seedBatchId, totalQuantity) in quantitiesInCart) {
            val availableStock = stockMovementDetailsList.value
                .filter { it.seedBatchId == seedBatchId }
                .sumOf { detail ->
                    if (detail.movementTypeEffectOnStock == "IN") {
                        detail.stockMovementQuantity
                    } else {
                        -detail.stockMovementQuantity
                    }
                }

            if (totalQuantity > availableStock) {
                val varietyName = stockOutCartItems.first { it.seedBatchId == seedBatchId }.varietyName
                val errorMessage = "Over stock for $varietyName. Available: $availableStock, Requested: $totalQuantity"
                return Pair(true, errorMessage)
            }
        }

        return Pair(false, "") // No over stock issue
    }

    private fun getTotalStock(seedBatch: SeedBatchStockOutData): Double {
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

    override fun addSeedBatchesToCart(batches: List<SeedBatchStockOutData>) {
        // Add batches to cart using SaleCartDataHolder
        SaleCartDataHolder.addItems(batches)
        updateTotals()
        Log.d("StockOutViewModel", "Added ${batches.size} batches to cart. Total items: ${stockOutCartItems.size}")
    }
}
