package com.example.fieldsync_inventory_app.ui.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.data.remote.dto.auth.request.LoginRequest
import com.example.fieldsync_inventory_app.domain.repository.data_store.PasscodeSettings
import com.example.fieldsync_inventory_app.domain.repository.data_store.TokenRepository
import com.example.fieldsync_inventory_app.domain.use_case.auth.LoginUseCase
import com.example.fieldsync_inventory_app.domain.use_case.data_store.passcode.VerifyPasscodeUseCase
import com.example.fieldsync_inventory_app.domain.use_case.data_store.passcode.GetPasscodeSettingsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.data_store.token.CheckTokenExpirationUseCase
import com.example.fieldsync_inventory_app.domain.use_case.data_store.token.ClearTokenUseCase
import com.example.fieldsync_inventory_app.domain.use_case.data_store.token.TokenStatus
import com.example.fieldsync_inventory_app.domain.use_case.data_store.token.isUsable
import com.example.fieldsync_inventory_app.ui.auth.login.state.LoginUiState
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import com.example.fieldsync_inventory_app.util.view_model.startLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenManager: TokenRepository,
    getPasscodeSettingsUseCase: GetPasscodeSettingsUseCase,
    private val verifyPasscodeUseCase: VerifyPasscodeUseCase,
    checkTokenExpirationUseCase: CheckTokenExpirationUseCase,
    private val clearTokenUseCase: ClearTokenUseCase
) : ViewModel(), ILoginViewModel {

    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState

    private val _loginUiState = MutableStateFlow(LoginUiState())
    override val loginUiState: StateFlow<LoginUiState> = _loginUiState

    val passcodeSettings: StateFlow<PasscodeSettings> =
        getPasscodeSettingsUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = PasscodeSettings()
        )

    // Derive hasPasscode from passcodeSettings automatically
    override val hasPasscode: StateFlow<Boolean> = passcodeSettings
        .map { it.passcodeHash.isNotEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )


    /**
     * Reactive state of the token status.
     * UI can collect this to show different screens based on token state.
     */
    val tokenStatus: StateFlow<TokenStatus> = checkTokenExpirationUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TokenStatus.NoToken
        )

    override val isTokenUsable: StateFlow<Boolean> = tokenStatus
        .map { it.isUsable() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    override fun onUsernameChange(newUsername: String) {
        _loginUiState.value = _loginUiState.value.copy(username = newUsername)
    }

    override fun onPasswordChange(newPassword: String) {
        _loginUiState.value = _loginUiState.value.copy(password = newPassword)
    }

    override fun onPasscodeChange(newPasscode: String) {
        _loginUiState.value = _loginUiState.value.copy(passcode = newPasscode)
    }

    override fun onLoginClick() {
        // Launch a coroutine to perform the login logic
        viewModelScope.launch {
            login()
        }
    }

    suspend fun login() {
        // Wait for passcode settings to load
        val hasPasscodeValue = passcodeSettings.first().passcodeHash.isNotEmpty()

        if (hasPasscodeValue && isTokenUsable.value) {
            // Login with passcode
            loginWithPasscode()
        } else {
            // Login with username/password
            loginWithCredentials()
        }
    }

    private suspend fun loginWithCredentials() {
        // Old token must be cleared else if token expired server will return token expiration error
        // and unable to login
        clearTokenUseCase()

        // Validate username and password
        val username = loginUiState.value.username
        val password = loginUiState.value.password
        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            _resourceUiState.endLoading("Please enter username and password")
            return
        }
        val loginRequest = LoginRequest(username, password)

        _resourceUiState.startLoading("Logging in...")
        try {
            val response = loginUseCase(loginRequest)
            tokenManager.saveToken(response.token)
            _resourceUiState.endLoading(null)
        } catch (e: Exception) {
            _resourceUiState.endLoading("${e.message}")
        }
    }

    private suspend fun loginWithPasscode() {
        val passcode = loginUiState.value.passcode

        if (passcode.isNullOrEmpty()) {
            _resourceUiState.endLoading("Please enter passcode")
            return
        }

        _resourceUiState.startLoading("Verifying passcode...")

        try {
            val result = verifyPasscodeUseCase(passcode)

            when {
                result.isCorrect -> {
                    Log.d("LoginViewModel", "Passcode verified successfully")
                    _resourceUiState.endLoading(null)
                }
                result.shouldClearPasscode -> {
                    Log.d("LoginViewModel", "Max attempts reached. Passcode cleared.")
                    _resourceUiState.endLoading(
                        "Too many failed attempts (${result.attemptCount}/${VerifyPasscodeUseCase.MAX_ATTEMPTS}). " +
                                "Passcode has been removed. Please login with username and password."
                    )
                }
                else -> {
                    val remaining = VerifyPasscodeUseCase.MAX_ATTEMPTS - result.attemptCount
                    Log.d("LoginViewModel", "Passcode verification failed. Attempts: ${result.attemptCount}")
                    _resourceUiState.endLoading(
                        "Incorrect passcode. $remaining attempt(s) remaining."
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Error verifying passcode", e)
            _resourceUiState.endLoading("Error verifying passcode: ${e.message}")
        }
    }

    override fun setErrorMessage(message: String?) {
        _resourceUiState.value = _resourceUiState.value.copy(error = message)
    }
}