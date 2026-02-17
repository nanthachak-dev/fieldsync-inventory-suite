package com.example.fieldsync_inventory_app.domain.use_case.stock_movement_type

import com.example.fieldsync_inventory_app.domain.repository.stock_movement_type.StockMovementTypeRepository
import javax.inject.Inject

class DeleteStockMovementTypeUseCase @Inject constructor(
    private val repository: StockMovementTypeRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteStockMovementType(id)
    }
}