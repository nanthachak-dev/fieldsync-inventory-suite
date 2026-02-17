package com.example.fieldsync_inventory_app.domain.repository.auth

import com.example.fieldsync_inventory_app.data.remote.dto.auth.request.LoginRequest
import com.example.fieldsync_inventory_app.data.remote.dto.auth.response.LoginResponse
import com.example.fieldsync_inventory_app.domain.model.ChangePassword

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): LoginResponse
    suspend fun changePassword(changePassword: ChangePassword)
}