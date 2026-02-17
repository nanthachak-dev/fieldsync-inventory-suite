package com.example.fieldsync_inventory_app.ui.main.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.model.StockTransactionSummary
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details.GetStockMovementDetailsByTransactionIdUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_transaction_summary.GetStockTransactionSummaryUseCase
import com.example.fieldsync_inventory_app.domain.use_case.transaction_operation.DeleteTransactionOperationUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.history.model.MainMovementType
import com.example.fieldsync_inventory_app.ui.main.history.model.TransactionCardData
import com.example.fieldsync_inventory_app.ui.main.history.state.HistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getStockTransactionSummaryUseCase: GetStockTransactionSummaryUseCase,
    private val getStockMovementDetailsByTransactionIdUseCase: GetStockMovementDetailsByTransactionIdUseCase,
    private val deleteTransactionOperationUseCase: DeleteTransactionOperationUseCase
) : ViewModel(), IHistoryViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState
    private val _historyUiState = MutableStateFlow(HistoryUiState())
    override val historyUiState: StateFlow<HistoryUiState> = _historyUiState
    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch

    private var allFetchedSummaries: MutableList<StockTransactionSummary> = mutableListOf()
    private var currentPage = 0
    private val pageSize = 20

    private fun updateFilterOptions() {
        val summaries = allFetchedSummaries
        val usernames = summaries.map { it.username }.filter { it.isNotBlank() }.distinct().sorted()
        val mainMovementTypeNicknames = summaries.map { MainMovementType.fromRawValue(it.mainMovementType) }.filter { it.isNotBlank() }.distinct().sorted()
        val customers = summaries.mapNotNull { it.customerName }.filter { it.isNotBlank() }.distinct().sorted()
        val suppliers = summaries.mapNotNull { it.supplierName }.filter { it.isNotBlank() }.distinct().sorted()

        _historyUiState.update { it.copy(
            usernames = usernames,
            mainMovementTypeNicknames = mainMovementTypeNicknames,
            customers = customers,
            suppliers = suppliers
        ) }
    }

    override fun firstLaunch() {
        if (_isFirstLaunch.value) {
            generateTransactionData(isNewSearch = true)
            _isFirstLaunch.value = false
        }
    }

    private fun generateTransactionData(isNewSearch: Boolean = true) {
        val targetPage = if (isNewSearch) 0 else currentPage + 1

        if (isNewSearch) {
            val currentTransactions = _historyUiState.value.transactions
            allFetchedSummaries.clear()
            _historyUiState.value = _historyUiState.value.copy(
                transactions = currentTransactions.ifEmpty { emptyList() },
                isLoading = true,
                isLastPage = false,
                hasError = false
            )
        } else {
            _historyUiState.value = _historyUiState.value.copy(
                isLoadingNextPage = true,
                hasError = false
            )
        }

        viewModelScope.launch {
            try {
                val formattedStartDate = _historyUiState.value.selectedFromDate?.let { "${it}T00:00:00Z" }
                val formattedEndDate = _historyUiState.value.selectedToDate?.let { "${it}T23:59:59Z" }

                val page = getStockTransactionSummaryUseCase(
                    page = targetPage,
                    size = pageSize,
                    sort = "transactionDate,desc",
                    startDate = formattedStartDate,
                    endDate = formattedEndDate
                )

                allFetchedSummaries.addAll(page.content)
                val mappedTransactions = prepareTransactions(allFetchedSummaries)

                currentPage = targetPage // Update only on success

                _historyUiState.value = _historyUiState.value.copy(
                    transactions = mappedTransactions,
                    isLoading = false,
                    isLoadingNextPage = false,
                    isLastPage = page.isLast,
                    hasError = false,
                    totalElements = page.totalElements
                )

                // Update filter options from the current cumulative fetched data
                updateFilterOptions()

                // Apply remaining local filters
                applyLocalFilters()

            } catch (e: Exception) {
                _historyUiState.value = _historyUiState.value.copy(
                    isLoading = false,
                    isLoadingNextPage = false,
                    hasError = true
                )
                Log.e("HistoryViewModel", "Error loading page $targetPage", e)
            }
        }
    }

    private fun prepareTransactions(summaries: List<StockTransactionSummary>): List<TransactionCardData> {
        return summaries.map { TransactionCardData(it) }
    }

    override fun loadTransactionDetails(transactionId: Long) {
        viewModelScope.launch {
            _historyUiState.update { it.copy(isDetailLoading = true, selectedTransactionDetails = emptyList()) }
            try {
                val details = getStockMovementDetailsByTransactionIdUseCase(transactionId)
                _historyUiState.update { it.copy(isDetailLoading = false, selectedTransactionDetails = details) }
            } catch (e: Exception) {
                _historyUiState.update { it.copy(isDetailLoading = false) }
                Log.e("HistoryViewModel", "Error loading details for transaction $transactionId", e)
            }
        }
    }

    override fun loadNextPage() {
        val state = _historyUiState.value
        if (!state.isLoading && !state.isLoadingNextPage && !state.isLastPage) {
            Log.d("HistoryViewModel", "Requesting next page: ${currentPage + 1}")
            generateTransactionData(isNewSearch = false)
        }
    }

    override fun onFromDateChanged(date: String?) {
        _historyUiState.value = _historyUiState.value.copy(selectedFromDate = date)
    }

    override fun onToDateChanged(date: String?) {
        _historyUiState.value = _historyUiState.value.copy(selectedToDate = date)
    }


    override fun onMainMovementTypeNicknameChanged(nickname: String?) {
        _historyUiState.value = _historyUiState.value.copy(selectedMainMovementTypeNickname = nickname)
    }

    override fun onUsernameChanged(username: String?) {
        _historyUiState.value = _historyUiState.value.copy(selectedUsername = username)
    }

    override fun onCustomerNameChanged(customerName: String?) {
        _historyUiState.value = _historyUiState.value.copy(selectedCustomerName = customerName)
    }

    override fun onSupplierNameChanged(supplierName: String?) {
        _historyUiState.value = _historyUiState.value.copy(selectedSupplierName = supplierName)
    }

    override fun applyFilters() {
        generateTransactionData(isNewSearch = true)
    }

    private fun applyLocalFilters() {
        var filteredList = prepareTransactions(allFetchedSummaries)

        val state = _historyUiState.value
        if (state.selectedMainMovementTypeNickname != null) {
            val rawValue = MainMovementType.toRawValue(state.selectedMainMovementTypeNickname)
            filteredList = filteredList.filter { it.summary.mainMovementType == rawValue }
        }
        if (state.selectedUsername != null) {
            filteredList = filteredList.filter { it.summary.username == state.selectedUsername }
        }
        if (state.selectedCustomerName != null) {
            filteredList = filteredList.filter { it.summary.customerName == state.selectedCustomerName }
        }
        if (state.selectedSupplierName != null) {
            filteredList = filteredList.filter { it.summary.supplierName == state.selectedSupplierName }
        }

        _historyUiState.value = _historyUiState.value.copy(
            transactions = filteredList,
            recordsCount = filteredList.size
        )
    }

    override fun clearFilters() {
        _historyUiState.value = _historyUiState.value.copy(
            selectedFromDate = null,
            selectedToDate = null,
            selectedMainMovementTypeNickname = null,
            selectedUsername = null,
            selectedCustomerName = null,
            selectedSupplierName = null
        )
        generateTransactionData(isNewSearch = true)
    }

    override fun onDeleteTransaction(id: Long) {
        viewModelScope.launch {
            _resourceUiState.update { it.copy(isLoading = true, isSuccess = false, error = null) }
            try {
                deleteTransactionOperationUseCase(id)
                _resourceUiState.update { it.copy(isLoading = false, isSuccess = true) }
                // Refresh data after deletion
                generateTransactionData(isNewSearch = true)
            } catch (e: Exception) {
                _resourceUiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Unknown error occurred") }
            }
        }
    }

    override fun clearResourceState() {
        _resourceUiState.update { ResourceUiState() }
    }

    override fun refreshScreen() {
        generateTransactionData(isNewSearch = true)
    }
}
