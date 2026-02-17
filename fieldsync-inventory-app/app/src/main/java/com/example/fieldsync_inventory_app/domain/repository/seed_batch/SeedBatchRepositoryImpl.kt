package com.example.fieldsync_inventory_app.domain.repository.seed_batch

import android.util.Log
import com.example.fieldsync_inventory_app.data.local.dao.SeedBatchDao
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toEntity
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.SeedBatchApi
import com.example.fieldsync_inventory_app.domain.model.SeedBatch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeedBatchRepositoryImpl @Inject constructor(
    private val api: SeedBatchApi,
    private val dao: SeedBatchDao
) : SeedBatchRepository {

    override suspend fun syncSeedBatches() {
        try {
            val networkSeedBatches = api.getSeedBatches()
            //Log.d("StockMovementTypeRepo", "Fetched from network: $networkSeedBatches")
            dao.insertAll(networkSeedBatches.map { it.toEntity() })
            Log.d("SeedBatchRepository", "Seed batches synced successfully")
        } catch (e: Exception) {
            // Handle error
            Log.e("SeedBatchRepository", "Error fetching from network", e)
            throw e
        }
    }

    override suspend fun saveSeedBatch(seedBatch: SeedBatch) {
        if (seedBatch.id == 0L) {
            // Create
            val response = api.createSeedBatch(seedBatch.toRequestDto())
            dao.insert(response.toEntity())
        } else {
            // Update
            val response = api.updateSeedBatch(seedBatch.id, seedBatch.toRequestDto())
            dao.insert(response.toEntity())
        }
    }

    override suspend fun deleteSeedBatch(id: Int) {
        api.deleteSeedBatch(id)
        dao.deleteById(id)
    }

    // -- Local Database --
    override fun getLocalSeedBatches(): Flow<List<SeedBatch>> = flow {
        dao.getSeedBatches().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }
}