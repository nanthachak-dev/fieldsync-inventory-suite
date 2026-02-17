package com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.state

import com.example.fieldsync_inventory_app.domain.model.StockMovementType
import com.example.fieldsync_inventory_app.util.constans.StockInTask
import com.example.fieldsync_inventory_app.util.number.formatCurrency
import java.time.LocalDateTime

data class StockInUiState (
    val taskList: List<StockInTask> = StockInTask.entries,
    val selectedTask: StockInTask? = StockInTask.PURCHASE,
    val stockMovementTypes: List<StockMovementType> = emptyList(),
    val supplierList: List<String> = emptyList(),// Replace with: List<Supplier> = emptyList(),
    val selectedSupplier: String? = null,
    val supplierId: Int? = null,
    val transactionTime: LocalDateTime = LocalDateTime.now(), // Holds the current/default date-time
    val note: String? = null,
    val totalQuantity: Double = 0.0,
    val totalPrice: Double = 0.0,
){
    val isFormValid: Boolean
        get() = selectedTask != null

    val totalQuantityDisplay: String
        get() = formatCurrency(totalQuantity)
    val totalPriceDisplay: String
        get() = formatCurrency(totalPrice)
}