package com.example.fieldsync_inventory_app.domain.use_case.stock

import com.example.fieldsync_inventory_app.domain.model.stock.StockSummary
import com.example.fieldsync_inventory_app.domain.repository.stock.StockRepository
import javax.inject.Inject

class GetStockSummaryUseCase @Inject constructor(
    private val repository: StockRepository
) {
    suspend operator fun invoke(lastDate: String? = null): StockSummary {
        return repository.getStockSummary(lastDate)
    }
}
