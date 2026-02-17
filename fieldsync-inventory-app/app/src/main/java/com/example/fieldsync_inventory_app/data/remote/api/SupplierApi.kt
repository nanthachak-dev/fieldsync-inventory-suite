package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.supplier.SupplierRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.supplier.SupplierResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SupplierApi {
    @GET("api/suppliers")
    suspend fun getSuppliers(): List<SupplierResponseDto>

    @POST("api/suppliers")
    suspend fun createSupplier(
        @Body request: SupplierRequestDto
    ): SupplierResponseDto

    @PUT("api/suppliers/{id}")
    suspend fun updateSupplier(
        @Path("id") id: Int,
        @Body request: SupplierRequestDto
    ): SupplierResponseDto

    @DELETE("api/suppliers/{id}")
    suspend fun deleteSupplier(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("api/suppliers/{id}")
    suspend fun getSupplierById(
        @Path("id") id: Int
    ): SupplierResponseDto
}
