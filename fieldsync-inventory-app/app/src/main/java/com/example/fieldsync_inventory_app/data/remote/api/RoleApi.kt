package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.role.RoleRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.role.RoleResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RoleApi {
    @GET("api/roles")
    suspend fun getRoles(): List<RoleResponseDto>

    @POST("api/roles")
    suspend fun createRole(
        @Body request: RoleRequestDto
    ): RoleResponseDto

    @GET("api/roles/{id}")
    suspend fun getRoleById(
        @Path("id") id: Int
    ): RoleResponseDto

    @PUT("api/roles/{id}")
    suspend fun updateRole(
        @Path("id") id: Int,
        @Body request: RoleRequestDto
    ): RoleResponseDto

    @DELETE("api/roles/{id}")
    suspend fun deleteRole(
        @Path("id") id: Int
    ): Response<Unit>
}
