package com.example.fieldsync_inventory_app.data.remote.dto.season

// SeasonResponseDto for the server's response
data class SeasonResponseDto(
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)
