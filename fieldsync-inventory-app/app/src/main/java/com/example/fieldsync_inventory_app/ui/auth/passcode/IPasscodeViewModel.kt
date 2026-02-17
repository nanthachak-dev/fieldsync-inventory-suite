package com.example.fieldsync_inventory_app.ui.auth.passcode

import com.example.fieldsync_inventory_app.ui.auth.passcode.state.PasscodeUiState
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.StateFlow

interface IPasscodeViewModel {
    val resourceUiState: StateFlow<ResourceUiState>
    val passcodeUiState: StateFlow<PasscodeUiState>

    fun onCurrentPasscodeChange(passcode: String)
    fun onPasscodeChange(passcode: String)
    fun onConfirmPasscodeChange(confirmPasscode: String)
    fun onCreatePasscodeClicked()
    fun onChangePasscodeClicked()
    fun resetPasscodeState()
}