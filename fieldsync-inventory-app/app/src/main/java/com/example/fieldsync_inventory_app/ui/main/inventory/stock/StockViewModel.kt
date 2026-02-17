package com.example.fieldsync_inventory_app.ui.main.inventory.stock

import com.example.fieldsync_inventory_app.ui.main.inventory.model.InventorySort
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.use_case.stock.GetVarietySummaryUseCase
import com.example.fieldsync_inventory_app.domain.use_case.seed_batch.SyncSeedBatchesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details.SyncStockMovementDetailsUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.inventory.model.SortOrder
import com.example.fieldsync_inventory_app.ui.main.inventory.model.VarietyCardData
import com.example.fieldsync_inventory_app.ui.main.inventory.state.InventoryUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val getVarietySummaryUseCase: GetVarietySummaryUseCase,
    private val syncSeedBatchesUseCase: SyncSeedBatchesUseCase,
    private val syncStockMovementDetailsUseCase: SyncStockMovementDetailsUseCase
): ViewModel(), IStockViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _inventoryUiState = MutableStateFlow(InventoryUiState())
    override val inventoryUiState: StateFlow<InventoryUiState> = _inventoryUiState.asStateFlow()

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    private var allVarieties: List<VarietyCardData> = emptyList()
    private var currentQuery: String = ""
    private var currentSort: InventorySort = InventorySort.NONE
    private var currentSortOrder: SortOrder = SortOrder.DESC

    override fun firstLaunch() {
        if (_isFirstLaunch.value) {
            refreshScreen()
        }
        loadVarietyData()
        _isFirstLaunch.value = false
    }

    override fun refreshScreen() {
        loadVarietyData()
        viewModelScope.launch {
            try {
                _resourceUiState.update { it.copy(isLoading = true) }
                val syncSeedBatchesDeferred = async { syncSeedBatchesUseCase.sync() }
                val syncStockMovementDeferred = async { syncStockMovementDetailsUseCase.sync() }
                awaitAll(syncSeedBatchesDeferred, syncStockMovementDeferred)
            } catch (e: Exception) {
                // Background sync errors shouldn't necessarily show error in UI if we fetched the list successfully
            } finally {
                _resourceUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    override fun onSearchQueryChange(query: String) {
        currentQuery = query
        _inventoryUiState.update { it.copy(searchQuery = query) }
        applyFiltersAndSort()
    }

    override fun onSortChange(sort: InventorySort) {
        if (currentSort == sort) {
            currentSortOrder = if (currentSortOrder == SortOrder.DESC) SortOrder.ASC else SortOrder.DESC
        } else {
            currentSort = sort
            currentSortOrder = SortOrder.DESC
        }
        _inventoryUiState.update { it.copy(currentSort = currentSort) }
        applyFiltersAndSort()
    }

    private fun applyFiltersAndSort() {
        val filteredList = if (currentQuery.isBlank()) {
            allVarieties
        } else {
            allVarieties.filter {
                it.varietyName.contains(currentQuery, ignoreCase = true)
            }
        }

        val sortedList = when (currentSort) {
            InventorySort.NONE -> filteredList
            InventorySort.STOCK -> if (currentSortOrder == SortOrder.DESC) filteredList.sortedByDescending { it.stock } else filteredList.sortedBy { it.stock }
            InventorySort.GRADED -> if (currentSortOrder == SortOrder.DESC) filteredList.sortedByDescending { it.graded } else filteredList.sortedBy { it.graded }
            InventorySort.UNGRADED -> if (currentSortOrder == SortOrder.DESC) filteredList.sortedByDescending { it.ungraded } else filteredList.sortedBy { it.ungraded }
            InventorySort.GERMINATED -> if (currentSortOrder == SortOrder.DESC) filteredList.sortedByDescending { it.germinated } else filteredList.sortedBy { it.germinated }
            InventorySort.UNGERMINATED -> if (currentSortOrder == SortOrder.DESC) filteredList.sortedByDescending { it.ungerminated } else filteredList.sortedBy { it.ungerminated }
        }
        _inventoryUiState.update { it.copy(varieties = sortedList) }
    }


    private fun loadVarietyData(){
        viewModelScope.launch {
            _resourceUiState.update { it.copy(isLoading = true, error = null) }
            try {
                val inventory = getVarietySummaryUseCase()
                
                val varietyCardData = inventory.content.map { variety ->
                    VarietyCardData(
                        varietyId = variety.varietyId,
                        varietyName = variety.varietyName,
                        stock = variety.stock,
                        r1Stock = variety.r1Stock,
                        r2Stock = variety.r2Stock,
                        r3Stock = variety.r3Stock,
                        graded = variety.graded,
                        r1Graded = variety.r1Graded,
                        r2Graded = variety.r2Graded,
                        r3Graded = variety.r3Graded,
                        ungraded = variety.ungraded,
                        r1Ungraded = variety.r1Ungraded,
                        r2Ungraded = variety.r2Ungraded,
                        r3Ungraded = variety.r3Ungraded,
                        germinated = variety.germinated,
                        r1Germinated = variety.r1Germinated,
                        r2Germinated = variety.r2Germinated,
                        r3Germinated = variety.r3Germinated,
                        ungerminated = variety.ungerminated,
                        r1Ungerminated = variety.r1Ungerminated,
                        r2Ungerminated = variety.r2Ungerminated,
                        r3Ungerminated = variety.r3Ungerminated
                    )
                }
                allVarieties = varietyCardData
                applyFiltersAndSort()
            } catch (e: Exception) {
                _resourceUiState.update { it.copy(error = e.message) }
            } finally {
                _resourceUiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
