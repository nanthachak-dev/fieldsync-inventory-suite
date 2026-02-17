package com.example.fieldsync_inventory_app.domain.repository.seed_batch

import com.example.fieldsync_inventory_app.domain.model.SeedBatch
import kotlinx.coroutines.flow.Flow

interface SeedBatchRepository {
    suspend fun syncSeedBatches()
    suspend fun saveSeedBatch(seedBatch: SeedBatch)
    suspend fun deleteSeedBatch(id: Int)

    // -- Local Database --
    fun getLocalSeedBatches(): Flow<List<SeedBatch>>
}