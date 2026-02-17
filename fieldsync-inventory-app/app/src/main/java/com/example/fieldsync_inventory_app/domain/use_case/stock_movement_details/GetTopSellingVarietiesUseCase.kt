package com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details

import com.example.fieldsync_inventory_app.domain.model.Page
import com.example.fieldsync_inventory_app.domain.model.TopSellingVariety
import com.example.fieldsync_inventory_app.domain.repository.stock_movement_details.StockMovementDetailsRepository
import javax.inject.Inject

class GetTopSellingVarietiesUseCase @Inject constructor(
    private val repository: StockMovementDetailsRepository
) {
    suspend operator fun invoke(
        startDate: String? = null,
        endDate: String? = null
    ): Page<TopSellingVariety> {
        return repository.getTopSellingVarieties(startDate, endDate)
    }
}
