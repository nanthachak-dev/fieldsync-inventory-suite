package com.example.fieldsync_inventory_app.domain.repository.stock_movement_type

import com.example.fieldsync_inventory_app.domain.model.StockMovementType
import kotlinx.coroutines.flow.Flow

interface StockMovementTypeRepository {
    suspend fun syncStockMovementTypes()
    suspend fun saveStockMovementType(stockMovementType: StockMovementType)
    suspend fun deleteStockMovementType(id: Int)
    fun getLocalStockMovementTypes(): Flow<List<StockMovementType>>
}