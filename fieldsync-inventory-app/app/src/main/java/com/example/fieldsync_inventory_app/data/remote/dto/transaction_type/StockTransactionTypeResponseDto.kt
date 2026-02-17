package com.example.fieldsync_inventory_app.data.remote.dto.transaction_type

data class StockTransactionTypeResponseDto(
    val id: Int,
    val name: String,
    val description: String?,
    val createdAt: String, // Use String for ISO 8601 timestamps
    val updatedAt: String,
    val deletedAt: String?
)