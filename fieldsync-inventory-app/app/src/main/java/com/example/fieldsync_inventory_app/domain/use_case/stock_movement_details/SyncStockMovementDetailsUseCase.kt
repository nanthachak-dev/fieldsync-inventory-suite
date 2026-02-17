package com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details

import com.example.fieldsync_inventory_app.domain.repository.stock_movement_details.StockMovementDetailsRepository
import javax.inject.Inject

class SyncStockMovementDetailsUseCase @Inject constructor(
    private val repository: StockMovementDetailsRepository
) {
    suspend fun sync(){
        repository.syncStockMovementDetails()
    }
}