package com.example.fieldsync_inventory_app.domain.model

data class RiceVarietyStock(
    val riceVarietyId: Long,
    val riceVarietyName: String,
    val riceVarietyImageUrl: String?,
    val totalQuantity: Double
)
