package com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details

data class StockMovementDetailsSyncDto(
    val action: String, // e.g., "CREATED", "UPDATED", "DELETED"
    val record: StockMovementDetailsResponseDto
)