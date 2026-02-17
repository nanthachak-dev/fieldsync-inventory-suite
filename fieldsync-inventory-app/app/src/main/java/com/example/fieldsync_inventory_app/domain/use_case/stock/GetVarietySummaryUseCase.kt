package com.example.fieldsync_inventory_app.domain.use_case.stock

import com.example.fieldsync_inventory_app.domain.model.stock.VarietySummaryPaged
import com.example.fieldsync_inventory_app.domain.repository.stock.StockRepository
import javax.inject.Inject

class GetVarietySummaryUseCase @Inject constructor(
    private val repository: StockRepository
) {
    suspend operator fun invoke(
        lastDate: String? = null,
        page: Int? = null,
        size: Int? = null
    ): VarietySummaryPaged {
        return repository.getVarietySummary(lastDate, page, size)
    }
}
