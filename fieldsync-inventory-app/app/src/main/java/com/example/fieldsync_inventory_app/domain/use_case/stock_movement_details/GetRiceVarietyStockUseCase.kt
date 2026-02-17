package com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details

import com.example.fieldsync_inventory_app.domain.model.Page
import com.example.fieldsync_inventory_app.domain.model.RiceVarietyStock
import com.example.fieldsync_inventory_app.domain.repository.stock_movement_details.StockMovementDetailsRepository
import javax.inject.Inject

class GetRiceVarietyStockUseCase @Inject constructor(
    private val repository: StockMovementDetailsRepository
) {
    suspend operator fun invoke(
        lastDate: String? = null
    ): Page<RiceVarietyStock> {
        return repository.getRiceVarietyStock(lastDate)
    }
}
