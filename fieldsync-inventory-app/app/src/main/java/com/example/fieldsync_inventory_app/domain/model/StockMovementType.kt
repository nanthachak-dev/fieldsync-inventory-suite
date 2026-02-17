package com.example.fieldsync_inventory_app.domain.model

data class StockMovementType(
    val id: Int,
    val name: String,
    val effectOnStock: String, // e.g., "INCREASE", "DECREASE"
    val description: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val deletedAt: Long?
)