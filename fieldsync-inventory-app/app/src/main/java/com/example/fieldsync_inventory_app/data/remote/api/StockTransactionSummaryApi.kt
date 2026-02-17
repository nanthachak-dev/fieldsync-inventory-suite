package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.stock_transaction_summary.StockTransactionSummaryPagedResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_transaction_summary.TotalStockResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_transaction_summary.TotalTransactionResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock_transaction_summary.TotalSaleResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface StockTransactionSummaryApi {
    @GET("api/v1/stock-transaction-summaries")
    suspend fun getStockTransactionSummaries(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sort: String? = "transactionDate,desc",
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): StockTransactionSummaryPagedResponseDto

    @GET("api/stock-movement-details/total-stock")
    suspend fun getTotalStock(
        @Query("lastDate") lastDate: String? = null
    ): TotalStockResponseDto

    @GET("api/v1/stock-transaction-summaries/count")
    suspend fun getTotalTransactionCount(
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): TotalTransactionResponseDto

    @GET("api/v1/stock-transaction-summaries/total-sold-out")
    suspend fun getTotalSale(
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): TotalSaleResponseDto
}
