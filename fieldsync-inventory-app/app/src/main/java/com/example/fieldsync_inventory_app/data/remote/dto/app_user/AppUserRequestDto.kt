package com.example.fieldsync_inventory_app.data.remote.dto.app_user

data class AppUserRequestDto(
    val username: String,
    val password: String?,
    val isActive: Boolean,
    val roles: List<String>
)
