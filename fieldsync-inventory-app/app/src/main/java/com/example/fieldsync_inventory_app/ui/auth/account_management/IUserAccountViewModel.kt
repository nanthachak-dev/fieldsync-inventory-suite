package com.example.fieldsync_inventory_app.ui.auth.account_management

import com.example.fieldsync_inventory_app.ui.auth.account_management.state.ChangePasswordUiState
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.StateFlow

interface IUserAccountViewModel {
    val changePasswordUiState: StateFlow<ChangePasswordUiState>
    val changePasswordResourceUiState: StateFlow<ResourceUiState>

    fun onCurrentPasswordChange(password: String)
    fun onNewPasswordChange(password: String)
    fun onConfirmationPasswordChange(password: String)
    fun onChangePasswordClicked()
    fun resetChangePasswordState()
}
