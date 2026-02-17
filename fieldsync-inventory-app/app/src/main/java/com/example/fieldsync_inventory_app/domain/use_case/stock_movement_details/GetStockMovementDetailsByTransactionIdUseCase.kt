package com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details

import com.example.fieldsync_inventory_app.domain.model.StockMovementDetails
import com.example.fieldsync_inventory_app.domain.repository.stock_movement_details.StockMovementDetailsRepository
import javax.inject.Inject

class GetStockMovementDetailsByTransactionIdUseCase @Inject constructor(
    private val repository: StockMovementDetailsRepository
) {
    suspend operator fun invoke(transactionId: Long): List<StockMovementDetails> {
        return repository.getStockMovementDetailsByTransactionId(transactionId)
    }
}
