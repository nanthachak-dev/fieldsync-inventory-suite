package com.example.fieldsync_inventory_app.domain.model

data class TotalTransaction(
    val totalTransactions: Int,
    val startDate: Long, // Epoch Milliseconds
    val endDate: Long    // Epoch Milliseconds
)
