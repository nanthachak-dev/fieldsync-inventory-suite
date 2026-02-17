package com.example.fieldsync_inventory_app.domain.use_case.transaction_type

import com.example.fieldsync_inventory_app.domain.repository.transaction_type.StockTransactionTypeRepository
import com.example.fieldsync_inventory_app.domain.model.StockTransactionType
import javax.inject.Inject

class SaveStockTransactionTypeUseCase @Inject constructor(
    private val repository: StockTransactionTypeRepository
) {
    suspend operator fun invoke(stockTransactionType: StockTransactionType) {
        repository.saveStockTransactionType(stockTransactionType)
    }
}