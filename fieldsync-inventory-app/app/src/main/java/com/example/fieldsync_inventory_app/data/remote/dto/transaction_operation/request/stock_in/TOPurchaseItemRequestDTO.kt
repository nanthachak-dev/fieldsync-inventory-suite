package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.stock_in

data class TOPurchaseItemRequestDTO(
    val stockMovementId: Int?,
    val purchaseId: Int?,
    val price: Double,
    val description: String?
)
