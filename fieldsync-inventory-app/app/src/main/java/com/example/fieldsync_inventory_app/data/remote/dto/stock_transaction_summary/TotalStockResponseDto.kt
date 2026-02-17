package com.example.fieldsync_inventory_app.data.remote.dto.stock_transaction_summary

data class TotalStockResponseDto(
    val totalStock: Double,
    val asOfDate: String // ISO 8601 string
)
