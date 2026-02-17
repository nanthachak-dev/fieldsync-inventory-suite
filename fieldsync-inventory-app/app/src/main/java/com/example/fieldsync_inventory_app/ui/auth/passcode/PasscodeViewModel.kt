package com.example.fieldsync_inventory_app.ui.auth.passcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.repository.data_store.PasscodeSettings
import com.example.fieldsync_inventory_app.domain.use_case.data_store.passcode.SavePasscodeUseCase
import com.example.fieldsync_inventory_app.domain.use_case.data_store.passcode.GetPasscodeSettingsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.data_store.passcode.VerifyPasscodeUseCase
import com.example.fieldsync_inventory_app.ui.auth.passcode.state.PasscodeUiState
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasscodeViewModel @Inject constructor(
    getPasscodeSettingsUseCase: GetPasscodeSettingsUseCase,
    private val savePasscodeUseCase: SavePasscodeUseCase,
    private val verifyPasscodeUseCase: VerifyPasscodeUseCase,
) : ViewModel(), IPasscodeViewModel {
    val passcodeSettings: StateFlow<PasscodeSettings> =
        getPasscodeSettingsUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PasscodeSettings()
        )
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState
    private val _passcodeUiState = MutableStateFlow(PasscodeUiState())
    override val passcodeUiState: StateFlow<PasscodeUiState> = _passcodeUiState

    // -- UI Events
    override fun onCurrentPasscodeChange(passcode: String) {
        _passcodeUiState.value = _passcodeUiState.value.copy(currentPasscode = passcode)
    }

    override fun onPasscodeChange(passcode: String) {
        _passcodeUiState.value = _passcodeUiState.value.copy(passcode = passcode)
    }

    override fun onConfirmPasscodeChange(confirmPasscode: String) {
        _passcodeUiState.value = _passcodeUiState.value.copy(confirmPasscode = confirmPasscode)
    }

    override fun onCreatePasscodeClicked() {
        createPasscode()
    }

    override fun onChangePasscodeClicked() {
        changePasscode()
    }

    override fun resetPasscodeState() {
        _passcodeUiState.value = PasscodeUiState()
        _resourceUiState.value = ResourceUiState()
    }

    // -- Business Operations
    private fun createPasscode() {
        // Validate Inputs --
        if (!formValidate())
            return

        // Create passcode
        viewModelScope.launch {
            try {
                savePasscodeUseCase(passcodeUiState.value.passcode!!)
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to create passcode: ${e.message}")
            }
        }
    }

    private fun changePasscode() {
        // Validate Inputs --
        if (!formValidate())
            return

        viewModelScope.launch {
            try {
                // Verify current passcode
                val currentPasscode = passcodeUiState.value.currentPasscode ?: ""
                if (currentPasscode.isEmpty()) {
                    _resourceUiState.endLoading("Current passcode can't be empty")
                    return@launch
                }

                val verificationResult = verifyPasscodeUseCase(currentPasscode)
                
                if (!verificationResult.isCorrect) {
                    _resourceUiState.endLoading("Current passcode is incorrect")
                    return@launch
                }

                // Save new passcode
                savePasscodeUseCase(passcodeUiState.value.passcode!!)
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to change passcode: ${e.message}")
            }
        }
    }

    // -- Helper Functions
    private fun formValidate(): Boolean {
        if (!passcodeUiState.value.isEmpty) {
            _resourceUiState.endLoading("Passcode can't be empty")
            return false
        }
        if (!passcodeUiState.value.isPasswordLengthValid) {
            _resourceUiState.endLoading("Passcode must be 4 digits")
            return false
        }
        if (!passcodeUiState.value.isPasswordDigit) {
            _resourceUiState.endLoading("Passcode must contain only numbers")
            return false
        }
        if (!passcodeUiState.value.isPasswordMatch) {
            _resourceUiState.endLoading("Passcode doesn't match")
            return false
        }

        if (!passcodeUiState.value.isPasswordNotSequential){
            _resourceUiState.endLoading("Passcode must not be sequential")
            return false
        }

        if (!passcodeUiState.value.isPasswordNotRepeating){
            _resourceUiState.endLoading("Passcode must not be repeating")
            return false
        }

        return true
    }
}