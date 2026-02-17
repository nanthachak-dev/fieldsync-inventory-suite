package com.example.fieldsync_inventory_app.ui.auth.login

import com.example.fieldsync_inventory_app.ui.auth.login.state.LoginUiState
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.StateFlow


interface ILoginViewModel {
    val resourceUiState: StateFlow<ResourceUiState>
    val loginUiState: StateFlow<LoginUiState>
    val hasPasscode: StateFlow<Boolean>

    fun onUsernameChange(newUsername: String)
    fun onPasswordChange(newPassword: String)
    fun onPasscodeChange(newPasscode: String)
    fun onLoginClick()
    fun setErrorMessage(message: String?)
    val isTokenUsable: StateFlow<Boolean>
}