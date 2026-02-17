package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details.RiceVarietyStockPagedResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details.TopSellingVarietyPagedResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details.StockMovementDetailsResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details.StockMovementDetailsSyncDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StockMovementDetailsApi {
    @GET("api/stock-movement-details")
    suspend fun getAllTransactionDetails(): List<StockMovementDetailsResponseDto>

    @GET("api/stock-movement-details/sync")
    suspend fun syncTransactionDetails(
        @Query("lastSyncTime") lastSyncTime: String
    ): List<StockMovementDetailsSyncDto>

    @GET("api/stock-movement-details/{transactionId}")
    suspend fun getStockMovementDetailsByTransactionId(
        @Path("transactionId") transactionId: Long
    ): List<StockMovementDetailsResponseDto>

    @GET("api/stock-movement-details/rice-variety-stock")
    suspend fun getRiceVarietyStock(
        @Query("lastDate") lastDate: String? = null
    ): RiceVarietyStockPagedResponseDto

    @GET("api/stock-movement-details/top-selling-varieties")
    suspend fun getTopSellingVarieties(
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): TopSellingVarietyPagedResponseDto
}