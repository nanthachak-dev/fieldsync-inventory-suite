package com.example.fieldsync_inventory_app.ui.auth.account_management.state

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmationPassword: String = ""
) {
    val isPasswordMatch: Boolean
        get() = newPassword == confirmationPassword && newPassword.isNotEmpty()

    val isNewPasswordNotEmpty: Boolean
        get() = newPassword.isNotEmpty()

    val isCurrentPasswordNotEmpty: Boolean
        get() = currentPassword.isNotEmpty()

    val isFormValid: Boolean
        get() = isCurrentPasswordNotEmpty && isNewPasswordNotEmpty && isPasswordMatch
}
