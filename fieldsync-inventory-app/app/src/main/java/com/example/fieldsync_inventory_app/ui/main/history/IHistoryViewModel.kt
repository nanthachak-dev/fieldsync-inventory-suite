package com.example.fieldsync_inventory_app.ui.main.history

import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.history.state.HistoryUiState
import kotlinx.coroutines.flow.StateFlow

interface IHistoryViewModel {
    val resourceUiState: StateFlow<ResourceUiState>
    val historyUiState: StateFlow<HistoryUiState>
    val isFirstLaunch: StateFlow<Boolean>
    fun firstLaunch()

    fun onFromDateChanged(date: String?)
    fun onToDateChanged(date: String?)
    fun onMainMovementTypeNicknameChanged(nickname: String?)
    fun onUsernameChanged(username: String?)
    fun onCustomerNameChanged(customerName: String?)
    fun applyFilters()
    fun clearFilters()
    fun onSupplierNameChanged(supplierName: String?)
    fun loadNextPage()
    fun loadTransactionDetails(transactionId: Long)
    fun onDeleteTransaction(id: Long)
    fun clearResourceState()
    fun refreshScreen()
}