package com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details

data class TopSellingVarietyResponseDto(
    val riceVarietyId: Long,
    val riceVarietyName: String,
    val riceVarietyImageUrl: String?,
    val totalSoldQuantity: Double
)
