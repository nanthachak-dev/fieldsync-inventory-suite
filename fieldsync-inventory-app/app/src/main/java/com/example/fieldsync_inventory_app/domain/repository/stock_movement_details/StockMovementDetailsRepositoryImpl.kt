package com.example.fieldsync_inventory_app.domain.repository.stock_movement_details

import android.util.Log
import com.example.fieldsync_inventory_app.data.local.dao.LastSyncDao
import com.example.fieldsync_inventory_app.data.local.dao.StockMovementDetailsDao
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toEntity
import com.example.fieldsync_inventory_app.data.remote.api.StockMovementDetailsApi
import com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details.StockMovementDetailsSyncDto
import com.example.fieldsync_inventory_app.domain.model.Page
import com.example.fieldsync_inventory_app.domain.model.RiceVarietyStock
import com.example.fieldsync_inventory_app.domain.model.StockMovementDetails
import com.example.fieldsync_inventory_app.domain.model.TopSellingVariety
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockMovementDetailsRepositoryImpl @Inject constructor(
    private val api: StockMovementDetailsApi,
    private val dao: StockMovementDetailsDao,
    private val lastSyncDao: LastSyncDao
) : StockMovementDetailsRepository {

    override suspend fun syncStockMovementDetails() {
        try {
            val networkTransactionDetails = api.getAllTransactionDetails()
            dao.insertAll(networkTransactionDetails.map { it.toEntity() })
            Log.d("StockTransactionDetailRepo", "Transaction details synced successfully")
        } catch (e: Exception) {
            Log.e("StockTransactionDetailRepo", "Error fetching from network", e)
            throw e
        }
    }

    override suspend fun syncStockMovementDetailsChanges(lastSyncTime: String) {
        val tableName = "TRANSACTION_DETAILS";
        try {
            val networkTransactionDetails = api.syncTransactionDetails(lastSyncTime)

            if (networkTransactionDetails.isEmpty()) {
                Log.d("StockTransactionDetailRepo", "No changes to sync on $tableName")
                return
            }

            for (dto in networkTransactionDetails) {
                processSyncDto(dto)
            }

            // Find the latest timestamp from the synced records to update the last sync time.
            val maxTimestamp = networkTransactionDetails.maxOfOrNull { it.record.updatedAt }
            if (maxTimestamp != null) {
                // Parse the timestamp string to an Instant.
                val instant = Instant.parse(maxTimestamp)
                // Convert to epoch milliseconds and add 1 to avoid re-syncing the same record.
                // The next sync will start from the time just after this latest record.
                val newSyncTime = instant.toEpochMilli() + 1
                lastSyncDao.updateLastSync(
                    name = tableName,
                    lastSyncTime = newSyncTime
                )
                Log.d(
                    "StockTransactionDetailRepo",
                    "Synced changes of transaction details successfully for ${networkTransactionDetails.size} records."
                )
            }
        } catch (e: Exception) {
            Log.e("StockTransactionDetailRepo", "Error during incremental sync", e)
            throw e
        }
    }

    private suspend fun processSyncDto(dto: StockMovementDetailsSyncDto) {
        when (dto.action) {
            "CREATED" -> {
                dao.insert(dto.record.toEntity())
            }

            "UPDATED" -> {
                dao.update(dto.record.toEntity())
            }

            "DELETED" -> {
                dao.delete(dto.record.toEntity())
            }

            else -> {
                Log.e("StockTransactionDetailRepo", "Unknown action: ${dto.action}")
            }
        }
    }

    override fun getLocalStockMovementDetails(): Flow<List<StockMovementDetails>> = flow {
        dao.getAllTransactionDetails().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }

    override suspend fun getStockMovementDetailsByTransactionId(transactionId: Long): List<StockMovementDetails> {
        return api.getStockMovementDetailsByTransactionId(transactionId).map { it.toDomain() }
    }

    override suspend fun getRiceVarietyStock(lastDate: String?): Page<RiceVarietyStock> {
        return api.getRiceVarietyStock(lastDate).toDomain()
    }

    override suspend fun getTopSellingVarieties(
        startDate: String?,
        endDate: String?
    ): Page<TopSellingVariety> {
        return api.getTopSellingVarieties(startDate, endDate).toDomain()
    }
}
