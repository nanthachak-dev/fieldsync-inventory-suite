package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request

data class TOSaleReqDto (
    val stockTransactionId: Int?,
    val customerId: Int?,
    val description: String?
)