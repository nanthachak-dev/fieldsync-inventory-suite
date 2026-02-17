package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.auth.request.ChangePasswordRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.auth.request.LoginRequest
import com.example.fieldsync_inventory_app.data.remote.dto.auth.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApi {

    @POST("/api/v1/auth/authenticate")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @PATCH("/api/v1/auth/change-password")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequestDto)

}
