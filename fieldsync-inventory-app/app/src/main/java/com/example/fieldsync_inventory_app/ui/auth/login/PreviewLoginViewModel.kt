package com.example.fieldsync_inventory_app.ui.auth.login

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.ui.auth.login.state.LoginUiState
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewLoginViewModel : ViewModel(), ILoginViewModel {
    override val resourceUiState: StateFlow<ResourceUiState> = MutableStateFlow(ResourceUiState())
    override val loginUiState: StateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    override val hasPasscode: StateFlow<Boolean> = MutableStateFlow(false)
    override val isTokenUsable: StateFlow<Boolean> = MutableStateFlow(false)

    override fun onUsernameChange(newUsername: String) {}
    override fun onPasswordChange(newPassword: String) {}
    override fun onPasscodeChange(newPasscode: String) {}

    override fun onLoginClick() {}
    override fun setErrorMessage(message: String?) {}
}