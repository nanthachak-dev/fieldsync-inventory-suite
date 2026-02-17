package com.example.fieldsync_inventory_app.ui.main.history

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.history.state.HistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewHistoryViewModel : ViewModel(), IHistoryViewModel {
    override val resourceUiState: StateFlow<ResourceUiState> = MutableStateFlow(ResourceUiState())
    override val historyUiState: StateFlow<HistoryUiState> =
        MutableStateFlow(HistoryUiState(transactions = sampleTransactions, totalElements = sampleTransactions.size.toLong()))
    override val isFirstLaunch: StateFlow<Boolean> = MutableStateFlow(true)

    override fun firstLaunch() {}

    override fun onFromDateChanged(date: String?) {}

    override fun onToDateChanged(date: String?) {}


    override fun onMainMovementTypeNicknameChanged(nickname: String?) {}

    override fun onUsernameChanged(username: String?) {}

    override fun onCustomerNameChanged(customerName: String?) {}

    override fun applyFilters() {}

    override fun clearFilters() {}

    override fun onSupplierNameChanged(supplierName: String?) {}

    override fun loadNextPage() {}
    override fun loadTransactionDetails(transactionId: Long) {}
    override fun onDeleteTransaction(id: Long) {}
    override fun clearResourceState() {}
    override fun refreshScreen() {}
}