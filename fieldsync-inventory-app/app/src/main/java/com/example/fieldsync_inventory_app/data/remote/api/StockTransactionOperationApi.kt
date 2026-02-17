package com.example.fieldsync_inventory_app.data.remote.api

import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TransactionOperationCreateReqDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.stock_in.TransactionOperationStockInReqDTO
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.response.StockTransactionOperationResDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.response.stock_in.TransactionOperationStockInResDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface StockTransactionOperationApi {
    @POST("api/transaction-operations")
    suspend fun createTransactionOperation(
        @Body request: TransactionOperationCreateReqDto
    ): StockTransactionOperationResDto

    @POST("api/transaction-operations/stock-in")
    suspend fun createTransactionOperationStockIn(
        @Body request: TransactionOperationStockInReqDTO
    ): TransactionOperationStockInResDTO

    @DELETE("api/transaction-operations/{id}")
    suspend fun deleteTransactionOperation(
        @Path("id") id: Long
    )
}