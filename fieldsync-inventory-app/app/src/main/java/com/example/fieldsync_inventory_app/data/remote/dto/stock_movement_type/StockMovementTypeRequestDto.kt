package com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_type

data class StockMovementTypeRequestDto(
    val name: String,
    val effectOnStock: String,
    val description: String?
)