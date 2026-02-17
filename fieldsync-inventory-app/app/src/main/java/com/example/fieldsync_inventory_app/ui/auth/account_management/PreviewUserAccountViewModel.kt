package com.example.fieldsync_inventory_app.ui.auth.account_management

import com.example.fieldsync_inventory_app.ui.auth.account_management.state.ChangePasswordUiState
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewUserAccountViewModel : IUserAccountViewModel {
    override val changePasswordUiState: StateFlow<ChangePasswordUiState> = MutableStateFlow(ChangePasswordUiState())
    override val changePasswordResourceUiState: StateFlow<ResourceUiState> = MutableStateFlow(ResourceUiState())

    override fun onCurrentPasswordChange(password: String) {}
    override fun onNewPasswordChange(password: String) {}
    override fun onConfirmationPasswordChange(password: String) {}
    override fun onChangePasswordClicked() {}
    override fun resetChangePasswordState() {}
}
