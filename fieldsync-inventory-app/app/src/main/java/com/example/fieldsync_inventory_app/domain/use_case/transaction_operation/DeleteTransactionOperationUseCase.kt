package com.example.fieldsync_inventory_app.domain.use_case.transaction_operation

import com.example.fieldsync_inventory_app.domain.repository.transaction_operation.TransactionOperationRepository
import javax.inject.Inject

class DeleteTransactionOperationUseCase @Inject constructor(
    private val repository: TransactionOperationRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteTransactionOperation(id)
}
