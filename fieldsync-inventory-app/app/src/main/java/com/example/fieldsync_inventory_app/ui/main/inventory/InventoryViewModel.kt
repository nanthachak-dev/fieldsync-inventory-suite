package com.example.fieldsync_inventory_app.ui.main.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.use_case.stock.GetStockSummaryUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val getStockSummaryUseCase: GetStockSummaryUseCase
): ViewModel(), IInventoryViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _stockSummaryState = MutableStateFlow(ResourceUiState())
    override val stockSummaryState: StateFlow<ResourceUiState> = _stockSummaryState.asStateFlow()

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun firstLaunch() {
        if (_isFirstLaunch.value) {
            fetchStockSummary()
            _isFirstLaunch.value = false
        }
    }

    override fun refreshScreen() {
        fetchStockSummary()
    }

    private fun fetchStockSummary() {
        viewModelScope.launch {
            _stockSummaryState.value = ResourceUiState(isLoading = true)
            try {
                val summary = getStockSummaryUseCase()
                _stockSummaryState.value = ResourceUiState(data = summary)
            } catch (e: Exception) {
                _stockSummaryState.value = ResourceUiState(error = e.message ?: "Unknown error")
            }
        }
    }
}