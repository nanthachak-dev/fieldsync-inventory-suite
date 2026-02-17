package com.example.fieldsync_inventory_app.data.remote.dto.stock_transaction_summary

data class TotalSaleResponseDto(
    val totalSoldOut: Double,
    val startDate: String, // ISO 8601 string
    val endDate: String   // ISO 8601 string
)
