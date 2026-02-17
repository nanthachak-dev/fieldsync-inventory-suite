package com.example.fieldsync_inventory_app.data.remote.dto.role

data class RoleResponseDto(
    val id: Int,
    val name: String,
    val description: String?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)
