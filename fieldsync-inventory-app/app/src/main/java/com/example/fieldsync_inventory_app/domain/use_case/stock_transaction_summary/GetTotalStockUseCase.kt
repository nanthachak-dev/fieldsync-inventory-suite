package com.example.fieldsync_inventory_app.domain.use_case.stock_transaction_summary

import com.example.fieldsync_inventory_app.domain.model.TotalStock
import com.example.fieldsync_inventory_app.domain.repository.stock_transaction_summary.StockTransactionSummaryRepository
import javax.inject.Inject

class GetTotalStockUseCase @Inject constructor(
    private val repository: StockTransactionSummaryRepository
) {
    suspend operator fun invoke(
        lastDate: String? = null
    ): TotalStock {
        return repository.getTotalStock(lastDate)
    }
}
