package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.stock_in

data class TOPurchaseRequestDTO(
    val stockTransactionId: Int?,
    val supplierId: Int?,
    val description: String?
)