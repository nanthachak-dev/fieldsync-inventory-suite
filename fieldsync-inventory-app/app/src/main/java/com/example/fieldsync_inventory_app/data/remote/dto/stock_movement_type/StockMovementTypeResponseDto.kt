package com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_type

data class StockMovementTypeResponseDto(
    val id: Int,
    val name: String,
    val effectOnStock: String,
    val description: String?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)