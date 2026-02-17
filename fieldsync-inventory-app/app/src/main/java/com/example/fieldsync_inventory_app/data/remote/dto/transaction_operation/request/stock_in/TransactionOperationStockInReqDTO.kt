package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.stock_in

import com.google.gson.annotations.SerializedName
import com.example.fieldsync_inventory_app.util.constans.StockInTask

data class TransactionOperationStockInReqDTO(
    val transactionTypeId: Int,
    val task: StockInTask,
    @SerializedName("performedBy") // Field name required by backend
    val performedById: Int,
    val transactionDate: String, // ISO 8601 string (none timezone)
    val description: String?,
    val purchase: TOPurchaseRequestDTO?,
    val stockMovements: List<TOStockMovementStockInReqDTO>
)