package com.example.fieldsync_inventory_app.ui.common.view_model.user_session

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.use_case.data_store.token.ClearTokenUseCase
import com.example.fieldsync_inventory_app.domain.use_case.data_store.token.GetTokenFlowUseCase
import com.example.fieldsync_inventory_app.domain.use_case.data_store.token.GetTokenUseCase
import com.example.fieldsync_inventory_app.data.security.AuthEventBus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSessionViewModel @Inject constructor(
    private val getTokenFlowUseCase: GetTokenFlowUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val clearTokenUseCase: ClearTokenUseCase,
    val authEventBus: AuthEventBus
) : ViewModel(), IUserSessionViewModel {

    private val _username = MutableStateFlow("Guest")
    override val username: StateFlow<String> = _username.asStateFlow()

    private val _userId = MutableStateFlow(0)
    override val userId: StateFlow<Int> = _userId.asStateFlow()

    init {
        observeToken()
    }

    private fun observeToken() {
        viewModelScope.launch {
            getTokenFlowUseCase().collect { token ->
                if (token != null) {
                    decodeToken(token)
                } else {
                    resetUserData()
                }
            }
        }
    }

    private fun decodeToken(token: String) {
        try {
            val decodedJWT = JWT.decode(token)
            
            val name = decodedJWT.getClaim("sub").asString() ?: "User"
            _username.value = name
            
            val id = decodedJWT.getClaim("userId").asInt() ?: 0
            _userId.value = id
            
            //Log.d("UserSessionViewModel", "Loaded user: $name (ID: $id)")
        } catch (e: JWTDecodeException) {
            Log.e("UserSessionViewModel", "Failed to decode token", e)
            resetUserData()
        }
    }

    private fun resetUserData() {
        _username.value = "Guest"
        _userId.value = 0
        Log.d("UserSessionViewModel", "User data reset to Guest")
    }

    override fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            clearTokenUseCase()
            resetUserData()
            onSuccess()
        }
    }

    override fun loadUserData() {
        viewModelScope.launch {
            val token = getTokenUseCase()
            if (token != null) {
                decodeToken(token)
            } else {
                resetUserData()
            }
        }
    }
}