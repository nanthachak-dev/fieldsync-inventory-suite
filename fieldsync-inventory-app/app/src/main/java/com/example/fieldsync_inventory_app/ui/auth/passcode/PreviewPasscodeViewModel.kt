package com.example.fieldsync_inventory_app.ui.auth.passcode

import com.example.fieldsync_inventory_app.ui.auth.passcode.state.PasscodeUiState
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewPasscodeViewModel : IPasscodeViewModel {
    override val resourceUiState: StateFlow<ResourceUiState> = MutableStateFlow(ResourceUiState())
    override val passcodeUiState: StateFlow<PasscodeUiState> = MutableStateFlow(PasscodeUiState())

    override fun onCurrentPasscodeChange(passcode: String) {}
    override fun onPasscodeChange(passcode: String) {}
    override fun onConfirmPasscodeChange(confirmPasscode: String) {}
    override fun onCreatePasscodeClicked() {}
    override fun onChangePasscodeClicked() {}
    override fun resetPasscodeState() {}
}
