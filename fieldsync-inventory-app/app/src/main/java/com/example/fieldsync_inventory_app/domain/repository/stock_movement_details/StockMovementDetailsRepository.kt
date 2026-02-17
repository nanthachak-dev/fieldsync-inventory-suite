package com.example.fieldsync_inventory_app.domain.repository.stock_movement_details

import com.example.fieldsync_inventory_app.domain.model.Page
import com.example.fieldsync_inventory_app.domain.model.RiceVarietyStock
import com.example.fieldsync_inventory_app.domain.model.StockMovementDetails
import com.example.fieldsync_inventory_app.domain.model.TopSellingVariety
import kotlinx.coroutines.flow.Flow

interface StockMovementDetailsRepository {
    suspend fun syncStockMovementDetails()
    suspend fun syncStockMovementDetailsChanges(lastSyncTime: String)
    fun getLocalStockMovementDetails(): Flow<List<StockMovementDetails>>

    suspend fun getStockMovementDetailsByTransactionId(
        transactionId: Long
    ): List<StockMovementDetails>

    suspend fun getRiceVarietyStock(
        lastDate: String? = null
    ): Page<RiceVarietyStock>

    suspend fun getTopSellingVarieties(
        startDate: String? = null,
        endDate: String? = null
    ): Page<TopSellingVariety>
}