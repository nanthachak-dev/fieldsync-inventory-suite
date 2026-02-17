package com.example.fieldsync_inventory_app.domain.model

data class TotalSale(
    val totalSale: Double,
    val startDate: Long, // Epoch Milliseconds
    val endDate: Long    // Epoch Milliseconds
)
