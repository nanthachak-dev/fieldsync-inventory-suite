package com.example.fieldsync_inventory_app.domain.model

data class TotalStock(
    val totalStock: Double,
    val asOfDate: Long // Epoch Milliseconds
)
