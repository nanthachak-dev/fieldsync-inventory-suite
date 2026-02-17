package com.example.fieldsync_inventory_app.domain.model

data class StockTransactionSummary(
    val transactionId: Long,
    val transactionDate: Long, // Epoch Milliseconds
    val transactionTypeId: Int,
    val transactionTypeName: String,
    val transactionDescription: String?,
    val username: String,
    val mainMovementType: String,
    val itemCount: Int,
    val totalQuantity: Double,
    val totalSalePrice: Double?,
    val totalPurchasePrice: Double?,
    val customerName: String?,
    val supplierName: String?,
    val fromBatchId: Long?,
    val toBatchId: Long?
)
