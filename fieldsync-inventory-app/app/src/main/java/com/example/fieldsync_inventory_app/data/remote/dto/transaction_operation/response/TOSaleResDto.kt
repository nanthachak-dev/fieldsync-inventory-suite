package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.response

data class TOSaleResDto (
    val customer: TOCustomerResDto?,
    val totalSale: Double
)