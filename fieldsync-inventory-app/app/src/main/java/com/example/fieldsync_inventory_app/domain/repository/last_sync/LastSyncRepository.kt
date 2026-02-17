package com.example.fieldsync_inventory_app.domain.repository.last_sync

import com.example.fieldsync_inventory_app.domain.model.LastSync
import kotlinx.coroutines.flow.Flow

interface LastSyncRepository {
    // -- Local Database
    suspend fun insertAll(lastSyncs: List<LastSync>)
    fun getLastSyncByName(name: String): Flow<LastSync?>
}
