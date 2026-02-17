package com.example.fieldsync_inventory_app.domain.model

data class ChangePassword(
    val currentPassword: String,
    val newPassword: String,
    val confirmationPassword: String
)
