package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.response

import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.IdNamePairDto

data class StockTransactionOperationResDto(
    val transactionId: Int,
    val transactionType: IdNamePairDto,
    val performedBy: TOPerformedByResDto,
    val stockMovements: List<TOStockMovementResDto>,
    val sale: TOSaleResDto?,
    val transactionDate: String, // ISO 8601 string
    val description: String?,
)