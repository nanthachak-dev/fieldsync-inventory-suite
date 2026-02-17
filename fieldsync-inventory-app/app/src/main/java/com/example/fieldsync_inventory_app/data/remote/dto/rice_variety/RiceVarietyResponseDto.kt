package com.example.fieldsync_inventory_app.data.remote.dto.rice_variety

// RiceVarietyResponseDto for the server's response
data class RiceVarietyResponseDto(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)
