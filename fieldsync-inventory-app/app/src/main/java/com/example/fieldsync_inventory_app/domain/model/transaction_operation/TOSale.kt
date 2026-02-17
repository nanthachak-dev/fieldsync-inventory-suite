package com.example.fieldsync_inventory_app.domain.model.transaction_operation

// Transaction Operation Sale
data class TOSale (
    val customer: TOCustomer?,
    val totalSale: Double?,
)