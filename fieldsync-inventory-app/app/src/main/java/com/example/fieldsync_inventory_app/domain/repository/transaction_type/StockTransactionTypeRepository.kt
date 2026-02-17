package com.example.fieldsync_inventory_app.domain.repository.transaction_type

import com.example.fieldsync_inventory_app.domain.model.StockTransactionType
import kotlinx.coroutines.flow.Flow

interface StockTransactionTypeRepository {
    suspend fun syncStockTransactionTypes()
    suspend fun saveStockTransactionType(stockTransactionType: StockTransactionType)
    suspend fun deleteStockTransactionType(id: Int)

    // -- Local Database --
    fun getLocalStockTransactionTypes(): Flow<List<StockTransactionType>>
}