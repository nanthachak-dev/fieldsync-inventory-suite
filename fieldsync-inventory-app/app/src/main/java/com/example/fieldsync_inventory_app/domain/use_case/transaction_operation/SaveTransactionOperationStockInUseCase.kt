package com.example.fieldsync_inventory_app.domain.use_case.transaction_operation

import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.stock_in.TransactionOperationStockInReqDTO
import com.example.fieldsync_inventory_app.domain.repository.transaction_operation.TransactionOperationRepository
import javax.inject.Inject

class SaveTransactionOperationStockInUseCase @Inject constructor(
    private val repository: TransactionOperationRepository
) {
    suspend operator fun invoke(reqDto: TransactionOperationStockInReqDTO) = repository.saveTransactionOperationStockIn(reqDto)
}