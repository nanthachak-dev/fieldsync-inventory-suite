package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.app_user.AppUserRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.app_user.AppUserResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AppUserApi {
    @GET("api/app-users")
    suspend fun getAppUsers(): List<AppUserResponseDto>

    @POST("api/app-users")
    suspend fun createAppUser(
        @Body request: AppUserRequestDto
    ): AppUserResponseDto

    @GET("api/app-users/{id}")
    suspend fun getAppUserById(
        @Path("id") id: Int
    ): AppUserResponseDto

    @PUT("api/app-users/{id}")
    suspend fun updateAppUser(
        @Path("id") id: Int,
        @Body request: AppUserRequestDto
    ): AppUserResponseDto
}
