package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.response.stock_in

data class TransactionOperationStockInResDTO(
    val transactionId: Long,
    val type: String,// transaction types
    val task: String, // movement types which are the same value each
    val supplier: String, // Purchase's supplier
    val stockMovements: List<TOStockMovementStockInResDTO>,
    val transactionDate: String, // ISO 8601 string
    val description: String,
    val performedBy: String
)
