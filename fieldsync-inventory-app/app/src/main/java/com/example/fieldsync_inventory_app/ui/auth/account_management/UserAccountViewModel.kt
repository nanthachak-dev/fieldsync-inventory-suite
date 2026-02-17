package com.example.fieldsync_inventory_app.ui.auth.account_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.model.ChangePassword
import com.example.fieldsync_inventory_app.domain.use_case.auth.ChangePasswordUseCase
import com.example.fieldsync_inventory_app.ui.auth.account_management.state.ChangePasswordUiState
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import com.example.fieldsync_inventory_app.util.view_model.startLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel(), IUserAccountViewModel {

    private val _changePasswordUiState = MutableStateFlow(ChangePasswordUiState())
    override val changePasswordUiState: StateFlow<ChangePasswordUiState> = _changePasswordUiState.asStateFlow()

    private val _changePasswordResourceUiState = MutableStateFlow(ResourceUiState())
    override val changePasswordResourceUiState: StateFlow<ResourceUiState> = _changePasswordResourceUiState.asStateFlow()

    override fun onCurrentPasswordChange(password: String) {
        _changePasswordUiState.value = _changePasswordUiState.value.copy(currentPassword = password)
    }

    override fun onNewPasswordChange(password: String) {
        _changePasswordUiState.value = _changePasswordUiState.value.copy(newPassword = password)
    }

    override fun onConfirmationPasswordChange(password: String) {
        _changePasswordUiState.value = _changePasswordUiState.value.copy(confirmationPassword = password)
    }

    override fun onChangePasswordClicked() {
        if (!validateForm()) return

        _changePasswordResourceUiState.startLoading()
        viewModelScope.launch {
            try {
                val request = ChangePassword(
                    currentPassword = _changePasswordUiState.value.currentPassword,
                    newPassword = _changePasswordUiState.value.newPassword,
                    confirmationPassword = _changePasswordUiState.value.confirmationPassword
                )
                changePasswordUseCase(request)
                _changePasswordResourceUiState.endLoading()
            } catch (e: Exception) {
                _changePasswordResourceUiState.endLoading(e.message ?: "Failed to change password")
            }
        }
    }

    override fun resetChangePasswordState() {
        _changePasswordUiState.value = ChangePasswordUiState()
        _changePasswordResourceUiState.value = ResourceUiState()
    }

    private fun validateForm(): Boolean {
        val state = _changePasswordUiState.value
        if (!state.isCurrentPasswordNotEmpty) {
            _changePasswordResourceUiState.endLoading("Current password cannot be empty")
            return false
        }
        if (!state.isNewPasswordNotEmpty) {
            _changePasswordResourceUiState.endLoading("New password cannot be empty")
            return false
        }
        if (!state.isPasswordMatch) {
            _changePasswordResourceUiState.endLoading("Passwords do not match")
            return false
        }
        return true
    }
}
