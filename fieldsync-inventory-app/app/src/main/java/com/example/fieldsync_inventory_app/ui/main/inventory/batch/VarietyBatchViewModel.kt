package com.example.fieldsync_inventory_app.ui.main.inventory.batch

import com.example.fieldsync_inventory_app.ui.main.inventory.model.InventorySort
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.use_case.inventory.GetInventoryBatchUseCase
import com.example.fieldsync_inventory_app.domain.use_case.seed_batch.SyncSeedBatchesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details.SyncStockMovementDetailsUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.model.BatchCardData
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.state.BatchUiState
import com.example.fieldsync_inventory_app.ui.main.inventory.model.SortOrder
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.model.StockAdjustmentBatch
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.model.SaleCartDataHolder
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.model.SeedBatchStockOutData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VarietyBatchViewModel @Inject constructor(
    private val getInventoryBatchUseCase: GetInventoryBatchUseCase,
    private val syncSeedBatchesUseCase: SyncSeedBatchesUseCase,
    private val syncStockMovementDetailsUseCase: SyncStockMovementDetailsUseCase
) : ViewModel(), IVarietyBatchViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _batchUiState = MutableStateFlow(BatchUiState())
    override val batchUiState: StateFlow<BatchUiState> = _batchUiState.asStateFlow()

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    private var allBatches: List<BatchCardData> = emptyList()
    private var currentQuery: String = ""
    private var currentSort: InventorySort = InventorySort.NONE
    private var currentSortOrder: SortOrder = SortOrder.DESC
    private var currentFilter: String = "None"
    private var hideZeroStock: Boolean = false

    // Sale cart items - delegate to SaleCartDataHolder
    override val saleCartItems = SaleCartDataHolder.getCartItems()

    override fun firstLaunch(varietyId: Int) {
        loadBatchData(varietyId)
        _isFirstLaunch.value = false
    }

    override fun onSortChange(sort: InventorySort) {
        if (currentSort == sort) {
            currentSortOrder = if (currentSortOrder == SortOrder.DESC) SortOrder.ASC else SortOrder.DESC
        } else {
            currentSort = sort
            currentSortOrder = SortOrder.DESC
        }
        _batchUiState.update { it.copy(currentSort = currentSort) }
        applyFiltersAndSort()
    }

    override fun onFilterChange(filter: String) {
        currentFilter = filter
        _batchUiState.update { it.copy(currentFilter = currentFilter) }
        applyFiltersAndSort()
    }

    override fun onHideZeroStockChange(hide: Boolean) {
        hideZeroStock = hide
        applyFiltersAndSort()
    }

    private fun applyFiltersAndSort() {
        val filteredList = allBatches.filter { batch ->
            val queryMatch = if (currentQuery.isBlank()) {
                true
            } else {
                batch.seasonName.contains(currentQuery, ignoreCase = true) ||
                batch.generationName.contains(currentQuery, ignoreCase = true)
            }

            val filterMatch = when (currentFilter) {
                "None" -> true
                "R1" -> batch.generationName == "R1"
                "R2" -> batch.generationName == "R2"
                "R3" -> batch.generationName == "R3"
                "Graded" -> batch.grading
                "Ungraded" -> !batch.grading
                "Germinated" -> batch.germination
                "Ungerminated" -> !batch.germination
                else -> true
            }

            val stockMatch = if (hideZeroStock) {
                batch.stock > 0
            } else {
                true
            }

            queryMatch && filterMatch && stockMatch
        }

        val sortedGroups = filteredList
            .groupBy { it.year }
            .mapValues { (_, batches) ->
                when (currentSort) {
                    InventorySort.NONE -> batches
                    InventorySort.STOCK -> {
                        if (currentSortOrder == SortOrder.DESC) {
                            batches.sortedByDescending { it.stock }
                        } else {
                            batches.sortedBy { it.stock }
                        }
                    }
                    else -> batches
                }
            }
            .entries
            .sortedByDescending { it.key }

        val sortedList = sortedGroups.flatMap { it.value }

        _batchUiState.update { it.copy(batchList = sortedList) }
    }

    override fun clearSortAndFilter(){
        Log.d("BatchViewModel", "clearSortAndFilter called")
        currentSort = InventorySort.NONE
        currentSortOrder = SortOrder.DESC
        currentFilter = "None"
        _batchUiState.update { it.copy(currentSort = currentSort, currentFilter = currentFilter) }
        applyFiltersAndSort()
    }

    override fun refreshScreen() {
        val varietyId = _batchUiState.value.selectedVarietyId
        if (varietyId != 0) {
            loadBatchData(varietyId)
        }
        viewModelScope.launch {
            try {
                val syncSeedBatchesDeferred = async { syncSeedBatchesUseCase.sync() }
                val syncStockMovementDeferred = async { syncStockMovementDetailsUseCase.sync() }
                awaitAll(syncSeedBatchesDeferred, syncStockMovementDeferred)
            } catch (e: Exception) {
                // Background sync errors ignored
            }
        }
    }

    override fun loadBatchData(varietyId: Int) {
        viewModelScope.launch {
            _resourceUiState.update { it.copy(isLoading = true, error = null) }
            try {
                val inventoryBatch = getInventoryBatchUseCase(varietyId)
                
                val batchCardData = inventoryBatch.batches.map { batch ->
                    BatchCardData(
                        batchId = batch.batchId.toLong(),
                        varietyId = batch.varietyId,
                        varietyName = batch.varietyName,
                        year = batch.year,
                        seasonId = batch.seasonId,
                        seasonName = batch.seasonName,
                        seasonDescription = batch.seasonDescription,
                        generationId = batch.generationId,
                        generationName = batch.generationName,
                        grading = batch.grading,
                        germination = batch.germination,
                        stock = batch.stock
                    )
                }
                
                allBatches = batchCardData
                applyFiltersAndSort()
                
                val varietyName = allBatches.firstOrNull()?.varietyName ?: ""
                _batchUiState.update { it.copy(selectedVarietyName = varietyName, selectedVarietyId = varietyId) }
            } catch (e: Exception) {
                _resourceUiState.update { it.copy(error = e.message) }
            } finally {
                _resourceUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    override fun onAddToSaleCartClick(
        batchCard: BatchCardData,
        quantity: Double,
        price: Double
    ) {
        // Find the batch data from allBatches
        val batch = allBatches.find { it.batchId == batchCard.batchId }
        if (batch == null) {
            Log.e("BatchViewModel", "Batch not found for batchId: ${batchCard.batchId}")
            return
        }

        // Create SeedBatchStockOutData
        val saleItem = SeedBatchStockOutData(
            id = 0, // Will be assigned by SaleCartDataHolder
            seedBatchId = batchCard.batchId,
            varietyName = _batchUiState.value.selectedVarietyName,
            varietyId = _batchUiState.value.selectedVarietyId,
            generation = batch.generationName,
            generationId = batch.generationId,
            season = batch.seasonDescription,
            seasonId = batch.seasonId,
            year = batch.year,
            grading = if (batch.grading) "Yes" else "No",
            gradingValue = batch.grading,
            germination = if (batch.germination) "Yes" else "No",
            germinationValue = batch.germination,
            totalStock = batch.stock,
            quantity = quantity,
            price = price
        )

        // Add to sale cart using SaleCartDataHolder
        SaleCartDataHolder.addItem(saleItem)
        Log.d("BatchViewModel", "Added item to sale cart. Total items: ${saleCartItems.size}")
    }

    override fun onAddToAdjustStockClick(batchCard: BatchCardData) {
        Log.d("BatchViewModel", "onAddToAdjustStockClick called for batch: $batchCard")
        StockAdjustmentBatch.clear() // Set to default
        StockAdjustmentBatch.loadBatch(
            StockAdjustmentBatch(
                seedBatchId = batchCard.batchId,
                varietyName = batchCard.varietyName,
                varietyId = batchCard.varietyId,
                generation = batchCard.generationName,
                generationId = batchCard.generationId,
                season = batchCard.seasonName,
                seasonDescription = batchCard.seasonDescription,
                seasonId = batchCard.seasonId,
                year = batchCard.year,
                grading = batchCard.grading,
                germination = batchCard.germination
            )
        )
    }
}
