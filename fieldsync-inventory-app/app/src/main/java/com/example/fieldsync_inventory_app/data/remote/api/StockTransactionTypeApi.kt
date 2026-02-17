package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.transaction_type.StockTransactionTypeRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_type.StockTransactionTypeResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StockTransactionTypeApi {
    @GET("api/stock-transaction-types")
    suspend fun getStockTransactionTypes(): List<StockTransactionTypeResponseDto>

    // POST request to create a new stock transaction type
    @POST("api/stock-transaction-types")
    suspend fun createStockTransactionType(
        @Body request: StockTransactionTypeRequestDto
    ): StockTransactionTypeResponseDto

    // PUT request to update an existing stock transaction type
    @PUT("api/stock-transaction-types/{id}")
    suspend fun updateStockTransactionType(
        @Path("id") id: Int,
        @Body request: StockTransactionTypeRequestDto
    ): StockTransactionTypeResponseDto

    // DELETE request to delete a stock transaction type
    @DELETE("api/stock-transaction-types/{id}")
    suspend fun deleteStockTransactionType(
        @Path("id") id: Int
    ): Response<Unit>

    // GET request to get a specific stock transaction type by ID
    @GET("api/stock-transaction-types/{id}")
    suspend fun getStockTransactionTypeById(
        @Path("id") id: Int
    ): StockTransactionTypeResponseDto
}