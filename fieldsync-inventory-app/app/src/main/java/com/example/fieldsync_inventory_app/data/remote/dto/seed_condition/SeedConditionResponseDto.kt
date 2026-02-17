package com.example.fieldsync_inventory_app.data.remote.dto.seed_condition

// SeedConditionResponseDto for the server's response
data class SeedConditionResponseDto(
    val id: Int,
    val name: String,
    val description: String?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)
