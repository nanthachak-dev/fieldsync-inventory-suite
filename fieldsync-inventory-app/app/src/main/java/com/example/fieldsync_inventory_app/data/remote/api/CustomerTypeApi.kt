package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.customer_type.CustomerTypeRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.customer_type.CustomerTypeResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomerTypeApi {
    @GET("api/customer-types")
    suspend fun getCustomerTypes(): List<CustomerTypeResponseDto>

    @POST("api/customer-types")
    suspend fun createCustomerType(
        @Body request: CustomerTypeRequestDto
    ): CustomerTypeResponseDto

    @PUT("api/customer-types/{id}")
    suspend fun updateCustomerType(
        @Path("id") id: Int,
        @Body request: CustomerTypeRequestDto
    ): CustomerTypeResponseDto

    @DELETE("api/customer-types/{id}")
    suspend fun deleteCustomerType(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("api/customer-types/{id}")
    suspend fun getCustomerTypeById(
        @Path("id") id: Int
    ): CustomerTypeResponseDto
}