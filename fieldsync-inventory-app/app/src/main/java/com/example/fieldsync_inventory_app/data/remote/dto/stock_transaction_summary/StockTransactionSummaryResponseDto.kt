package com.example.fieldsync_inventory_app.data.remote.dto.stock_transaction_summary

data class StockTransactionSummaryResponseDto(
    val transactionId: Long,
    val transactionDate: String, // ISO 8601 string
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
