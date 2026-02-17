package com.example.fieldsync_inventory_app.domain.use_case.transaction_operation

import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TransactionOperationCreateReqDto
import com.example.fieldsync_inventory_app.domain.repository.transaction_operation.TransactionOperationRepository
import javax.inject.Inject

class SaveTransactionOperationUseCase @Inject constructor(
    private val repository: TransactionOperationRepository
) {
    suspend operator fun invoke(reqDto: TransactionOperationCreateReqDto) = repository.saveTransactionOperation(reqDto)
}