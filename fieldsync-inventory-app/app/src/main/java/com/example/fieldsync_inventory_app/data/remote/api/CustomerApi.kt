package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.customer.CustomerRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.customer.CustomerResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomerApi {
    @GET("api/customers")
    suspend fun getCustomers(): List<CustomerResponseDto>

    @POST("api/customers")
    suspend fun createCustomer(
        @Body request: CustomerRequestDto
    ): CustomerResponseDto

    @PUT("api/customers/{id}")
    suspend fun updateCustomer(
        @Path("id") id: Int,
        @Body request: CustomerRequestDto
    ): CustomerResponseDto

    @DELETE("api/customers/{id}")
    suspend fun deleteCustomer(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("api/customers/{id}")
    suspend fun getCustomerById(
        @Path("id") id: Int
    ): CustomerResponseDto
}
