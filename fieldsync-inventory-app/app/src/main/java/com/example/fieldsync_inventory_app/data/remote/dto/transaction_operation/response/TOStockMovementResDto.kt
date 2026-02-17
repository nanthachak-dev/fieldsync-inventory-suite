package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.response

import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.IdNamePairDto

data class TOStockMovementResDto(
    val id: Int,
    val movementType: IdNamePairDto, // The response shows only { "id": 101, "name": "Something" }
    val seedBatch: TOSeedBatchResDto, // Flattened seed batch data which shows only { "id": 101, "description": "Something" }
    val saleItem: TOSaleItemResDto?,
    val quantity: Double,
    val description: String?
)