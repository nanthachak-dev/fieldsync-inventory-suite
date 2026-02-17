package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request

data class TOSaleItemReqDto(
    val stockMovementId: Int?,
    val saleId: Int?,
    val price: Double,
    val description: String?
)