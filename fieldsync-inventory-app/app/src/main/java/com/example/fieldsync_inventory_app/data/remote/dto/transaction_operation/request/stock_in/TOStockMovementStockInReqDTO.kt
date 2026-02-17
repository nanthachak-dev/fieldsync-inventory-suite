package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.stock_in

import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TOSeedBatchReqDto

data class TOStockMovementStockInReqDTO(
    val id: Long?,
    val movementTypeId: Int,
    val seedBatch: TOSeedBatchReqDto,
    val purchaseItem: TOPurchaseItemRequestDTO?,
    val quantity: Double,
    val description: String?
)