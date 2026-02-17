package com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details

import com.example.fieldsync_inventory_app.domain.repository.stock_movement_details.StockMovementDetailsRepository
import javax.inject.Inject

class GetLocalStockMovementDetailsUseCase @Inject constructor(
    private val repository: StockMovementDetailsRepository
) {
    operator fun invoke() = repository.getLocalStockMovementDetails()
}