package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_type.StockMovementTypeRequestDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_type.StockMovementTypeResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StockMovementTypeApi {
    @GET("api/stock-movement-types")
    suspend fun getStockMovementTypes(): List<StockMovementTypeResponseDto>

    // POST request to create a new stock movement type
    @POST("api/stock-movement-types")
    suspend fun createStockMovementType(
        @Body request: StockMovementTypeRequestDto
    ): StockMovementTypeResponseDto

    // PUT request to update an existing stock movement type
    @PUT("api/stock-movement-types/{id}")
    suspend fun updateStockMovementType(
        @Path("id") id: Int,
        @Body request: StockMovementTypeRequestDto
    ): StockMovementTypeResponseDto

    // DELETE request to delete a stock movement type
    @DELETE("api/stock-movement-types/{id}")
    suspend fun deleteStockMovementType(
        @Path("id") id: Int
    ): Response<Unit>

    // GET request to get a specific stock movement type by ID
    @GET("api/stock-movement-types/{id}")
    suspend fun getStockMovementTypeById(
        @Path("id") id: Int
    ): StockMovementTypeResponseDto
}