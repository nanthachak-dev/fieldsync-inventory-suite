package com.example.fieldsync_inventory_app.domain.use_case.last_sync

import android.util.Log
import com.example.fieldsync_inventory_app.data.local.entity.LastSyncEntity
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details.SyncStockMovementDetailsChangesUseCase
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException
import java.time.Instant
import javax.inject.Inject

class SyncChangesUseCase @Inject constructor(
    private val getLastSyncByNameUseCase: GetLastSyncByNameUseCase,
    private val syncTransactionDetailsChangesUseCase: SyncStockMovementDetailsChangesUseCase,
    private val saveAllLastSyncUseCase: SaveAllLastSyncUseCase
) {
    suspend operator fun invoke() {
        val LAST_SYNC_DATA = listOf(
            LastSyncEntity(
                name = "TRANSACTION_DETAILS",
                description = "Transaction Details",
                lastSyncTime = 0
            ).toDomain()
        )

        try {
            // Get the last sync time of transaction detail from the database
            Log.d("SyncChangesUseCase", "Start syncing changes")
            val lastSync = getLastSyncByNameUseCase("TRANSACTION_DETAILS").firstOrNull()
            if (lastSync == null) {
                Log.d("SyncChangesUseCase", "Last sync time is null, proceed to create new data")
                saveAllLastSyncUseCase(LAST_SYNC_DATA) // This will take effect in future calls not current
            }
            val lastSyncTimeString = lastSync?.lastSyncTime?.let {
                Instant.ofEpochMilli(it).toString()
            }

            // Start the sync process on features here
            syncTransactionDetailsChangesUseCase(lastSyncTimeString ?: Instant.ofEpochMilli(0).toString())
        } catch (e: HttpException) { // Show error response from backend
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("SyncChangesUseCase", "Http failed: $errorBody")
            throw Exception(errorBody)
        }
        catch (e: Exception) {
            Log.e("SyncChangesUseCase", "Unknown failed: ${e.message}", e)
            throw e
        }
    }
}