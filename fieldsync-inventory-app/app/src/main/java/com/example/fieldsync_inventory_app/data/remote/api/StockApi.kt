package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.stock.StockSummaryResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock.VarietySummaryResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("api/v1/stock")
    suspend fun getStockSummary(
        @Query("lastDate") lastDate: String? = null
    ): StockSummaryResponseDto

    @GET("api/v1/stock/varieties")
    suspend fun getVarietySummary(
        @Query("lastDate") lastDate: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): VarietySummaryResponseDto
}
