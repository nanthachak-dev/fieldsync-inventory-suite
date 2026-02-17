package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request

data class TOStockMovementReqDto(
    val id: Long?,
    val movementTypeId: Int,
    val seedBatch: TOSeedBatchReqDto,
    val saleItem: TOSaleItemReqDto?,
    val quantity: Double,
    val description: String?
)