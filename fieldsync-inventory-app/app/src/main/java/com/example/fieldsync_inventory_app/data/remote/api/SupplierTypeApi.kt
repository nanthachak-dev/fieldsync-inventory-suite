package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.supplier_type.SupplierTypeRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.supplier_type.SupplierTypeResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SupplierTypeApi {
    // Get list
    @GET("api/supplier-types")
    suspend fun getSupplierTypes(): List<SupplierTypeResponseDto>

    // Create
    @POST("api/supplier-types")
    suspend fun createSupplierType(
        @Body request: SupplierTypeRequestDto
    ): SupplierTypeResponseDto

    // Update
    @PUT("api/supplier-types/{id}")
    suspend fun updateSupplierType(
        @Path("id") id: Int,
        @Body request: SupplierTypeRequestDto
    ): SupplierTypeResponseDto

    // Delete
    @DELETE("api/supplier-types/{id}")
    suspend fun deleteSupplierType(
        @Path("id") id: Int
    ): Response<Unit>

    // Get by id
    @GET("api/supplier-types/{id}")
    suspend fun getSupplierTypeById(
        @Path("id") id: Int
    ): SupplierTypeResponseDto
}