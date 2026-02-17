package com.example.fieldsync_inventory_app.domain.use_case.stock_movement_type

import com.example.fieldsync_inventory_app.domain.repository.stock_movement_type.StockMovementTypeRepository
import javax.inject.Inject

class GetLocalStockMovementTypesUseCase @Inject constructor(
    private val repository: StockMovementTypeRepository
) {
    operator fun invoke() = repository.getLocalStockMovementTypes()
}