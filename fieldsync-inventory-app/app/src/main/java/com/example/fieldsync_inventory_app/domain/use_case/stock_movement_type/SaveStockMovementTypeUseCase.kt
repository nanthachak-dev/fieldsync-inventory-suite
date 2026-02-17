package com.example.fieldsync_inventory_app.domain.use_case.stock_movement_type

import com.example.fieldsync_inventory_app.domain.repository.stock_movement_type.StockMovementTypeRepository
import com.example.fieldsync_inventory_app.domain.model.StockMovementType
import javax.inject.Inject

class SaveStockMovementTypeUseCase @Inject constructor(
    private val repository: StockMovementTypeRepository
) {
    suspend operator fun invoke(stockMovementType: StockMovementType) {
        repository.saveStockMovementType(stockMovementType)
    }
}