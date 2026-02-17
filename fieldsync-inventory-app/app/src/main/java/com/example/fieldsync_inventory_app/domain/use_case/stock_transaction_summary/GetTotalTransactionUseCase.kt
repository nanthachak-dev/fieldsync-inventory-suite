package com.example.fieldsync_inventory_app.domain.use_case.stock_transaction_summary

import com.example.fieldsync_inventory_app.domain.model.TotalTransaction
import com.example.fieldsync_inventory_app.domain.repository.stock_transaction_summary.StockTransactionSummaryRepository
import javax.inject.Inject

class GetTotalTransactionUseCase @Inject constructor(
    private val repository: StockTransactionSummaryRepository
) {
    suspend operator fun invoke(
        startDate: String? = null,
        endDate: String? = null
    ): TotalTransaction {
        return repository.getTotalTransactionCount(startDate, endDate)
    }
}
