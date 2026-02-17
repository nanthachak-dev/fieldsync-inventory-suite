package com.example.fieldsync_inventory_app.data.remote.dto.auth.request

data class ChangePasswordRequestDto(
    val currentPassword: String,
    val newPassword: String,
    val confirmationPassword: String
)
