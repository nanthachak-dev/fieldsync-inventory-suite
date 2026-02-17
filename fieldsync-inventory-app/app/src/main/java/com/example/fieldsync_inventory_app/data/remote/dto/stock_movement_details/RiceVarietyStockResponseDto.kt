package com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details

data class RiceVarietyStockResponseDto(
    val riceVarietyId: Long,
    val riceVarietyName: String,
    val riceVarietyImageUrl: String?,
    val totalQuantity: Double
)
