package com.example.fieldsync_inventory_app.ui.main.history.state

import com.example.fieldsync_inventory_app.domain.model.StockMovementDetails
import com.example.fieldsync_inventory_app.ui.main.history.model.TransactionCardData

data class HistoryUiState(
    val transactions: List<TransactionCardData> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val isLastPage: Boolean = false,
    val hasError: Boolean = false,
    val totalElements: Long = 0,
    val recordsCount: Int = 0,
    // Details
    val selectedTransactionDetails: List<StockMovementDetails> = emptyList(),
    val isDetailLoading: Boolean = false,
    // Filter data
    val mainMovementTypeNicknames: List<String> = emptyList(),
    val customers: List<String> = emptyList(), // unique customer names from fetched data
    val suppliers: List<String> = emptyList(), // unique supplier names from fetched data
    val usernames: List<String> = emptyList(),

    // Selected filter values
    val selectedFromDate: String? = null,
    val selectedToDate: String? = null,
    val selectedMainMovementTypeNickname: String? = null,
    val selectedUsername: String? = null,
    val selectedCustomerName: String? = null,
    val selectedSupplierName: String? = null
)