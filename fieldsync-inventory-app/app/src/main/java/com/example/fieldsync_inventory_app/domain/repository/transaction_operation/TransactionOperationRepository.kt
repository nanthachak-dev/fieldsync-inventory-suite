package com.example.fieldsync_inventory_app.domain.repository.transaction_operation

import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TransactionOperationCreateReqDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.stock_in.TransactionOperationStockInReqDTO

interface TransactionOperationRepository {
    suspend fun saveTransactionOperation(reqDto: TransactionOperationCreateReqDto)

    suspend fun saveTransactionOperationStockIn(reqDto: TransactionOperationStockInReqDTO)

    suspend fun deleteTransactionOperation(id: Long)
}