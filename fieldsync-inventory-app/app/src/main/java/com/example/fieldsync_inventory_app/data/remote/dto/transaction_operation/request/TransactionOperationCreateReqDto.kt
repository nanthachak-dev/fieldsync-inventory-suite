package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request

data class TransactionOperationCreateReqDto(
    val transactionTypeId: Int,
    val performedById: Int,
    val transactionDate: String, // ISO 8601 string
    val description: String?,
    val saleRequestDTO: TOSaleReqDto?,
    val stockMovements: List<TOStockMovementReqDto>
)