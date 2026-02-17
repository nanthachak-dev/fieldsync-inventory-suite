package com.example.fieldsync_inventory_app.domain.model.transaction_operation

data class TransactionOperation(
    val id: Int,
    val type: IdNamePair,
    val performedBy: TOUser,
    val sale: TOSale?,
    val stockMovements: List<TOStockMovement>,
    val date: String, // Or use a proper DateTime object (e.g., ZonedDateTime)
    val description: String?,
)