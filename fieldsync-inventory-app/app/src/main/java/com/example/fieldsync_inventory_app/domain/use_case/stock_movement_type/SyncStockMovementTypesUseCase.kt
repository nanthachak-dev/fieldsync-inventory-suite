package com.example.fieldsync_inventory_app.domain.use_case.stock_movement_type

import com.example.fieldsync_inventory_app.domain.repository.stock_movement_type.StockMovementTypeRepository
import javax.inject.Inject

class SyncStockMovementTypesUseCase @Inject constructor(
    private val repository: StockMovementTypeRepository
) {
    // No 'invoke' function needed if it doesn't return a Flow
    suspend fun sync() {
        repository.syncStockMovementTypes()
    }
}