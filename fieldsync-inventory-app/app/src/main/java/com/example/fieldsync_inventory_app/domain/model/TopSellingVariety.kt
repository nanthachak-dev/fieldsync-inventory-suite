package com.example.fieldsync_inventory_app.domain.model

data class TopSellingVariety(
    val riceVarietyId: Long,
    val riceVarietyName: String,
    val riceVarietyImageUrl: String?,
    val totalSoldQuantity: Double
)
