package com.example.fieldsync_inventory_app.domain.model.transaction_operation

data class TOStockMovement(
    val id: Int,
    val movementType: IdNamePair,
    val seedBatchId: Int,
    val saleItem: TOSaleItem?,
    val quantity: Double,
    val description: String?
)