package com.example.fieldsync_inventory_app.domain.repository.auth

import android.util.Log
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.AuthApi
import com.example.fieldsync_inventory_app.data.remote.dto.auth.request.LoginRequest
import com.example.fieldsync_inventory_app.data.remote.dto.auth.response.LoginResponse
import com.example.fieldsync_inventory_app.domain.model.ChangePassword
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(private val authApi: AuthApi) : AuthRepository {
    override suspend fun login(loginRequest: LoginRequest): LoginResponse {
        try {
            return authApi.login(loginRequest)
        } catch (e: HttpException) { // Show error response from backend
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AuthRepositoryImpl.login", "Authentication failed: $errorBody")
            throw Exception(errorBody)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl.login", "Unknown error: ${e.message}")
            throw e
        }
    }

    override suspend fun changePassword(changePassword: ChangePassword) {
        try {
            authApi.changePassword(changePassword.toRequestDto())
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AuthRepositoryImpl.changePassword", "Change password failed: $errorBody")
            throw Exception(errorBody)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl.changePassword", "Unknown error: ${e.message}")
            throw e
        }
    }
}