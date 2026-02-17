package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.response.stock_in

import java.math.BigDecimal

data class TOStockMovementStockInResDTO(
    val id: Long,
    val movementType: String,
    val seedBatchId: Long, // For create new if not found
    val purchaseItem: BigDecimal, // stockMovementId and purchaseId will be ignored
    val quantity: Double,
    val description: String
)
