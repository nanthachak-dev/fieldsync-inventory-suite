package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.remote.dto.auth.request.ChangePasswordRequestDto
import com.example.fieldsync_inventory_app.domain.model.ChangePassword

fun ChangePassword.toRequestDto(): ChangePasswordRequestDto {
    return ChangePasswordRequestDto(
        currentPassword = this.currentPassword,
        newPassword = this.newPassword,
        confirmationPassword = this.confirmationPassword
    )
}
