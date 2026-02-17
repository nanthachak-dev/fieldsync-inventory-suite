package com.example.fieldsync_inventory_app.data.remote.dto.app_user

data class AppUserResponseDto(
    val id: Int,
    val username: String,
    val password: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
    val roles: List<String>
)
