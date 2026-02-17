package com.example.fieldsync_inventory_app.domain.use_case.stock_transaction_summary

import com.example.fieldsync_inventory_app.domain.model.Page
import com.example.fieldsync_inventory_app.domain.model.StockTransactionSummary
import com.example.fieldsync_inventory_app.domain.repository.stock_transaction_summary.StockTransactionSummaryRepository
import javax.inject.Inject

class GetStockTransactionSummaryUseCase @Inject constructor(
    private val repository: StockTransactionSummaryRepository
) {
    suspend operator fun invoke(
        page: Int? = null,
        size: Int? = null,
        sort: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Page<StockTransactionSummary> {
        return repository.getStockTransactionSummaries(
            page = page,
            size = size,
            sort = sort,
            startDate = startDate,
            endDate = endDate
        )
    }
}
