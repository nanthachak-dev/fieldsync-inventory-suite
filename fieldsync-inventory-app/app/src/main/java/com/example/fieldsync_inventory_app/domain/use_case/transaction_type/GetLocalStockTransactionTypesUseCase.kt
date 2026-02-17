package com.example.fieldsync_inventory_app.domain.use_case.transaction_type

import com.example.fieldsync_inventory_app.domain.repository.transaction_type.StockTransactionTypeRepository
import javax.inject.Inject

class GetLocalStockTransactionTypesUseCase @Inject constructor(
    private val repository: StockTransactionTypeRepository
) {
    operator fun invoke() = repository.getLocalStockTransactionTypes()
}