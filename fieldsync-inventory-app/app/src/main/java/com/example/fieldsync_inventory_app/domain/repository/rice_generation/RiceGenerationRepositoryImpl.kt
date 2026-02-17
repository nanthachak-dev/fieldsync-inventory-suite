package com.example.fieldsync_inventory_app.domain.repository.rice_generation

import android.util.Log
import com.example.fieldsync_inventory_app.data.local.dao.RiceGenerationDao
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toEntity
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.RiceGenerationApi
import com.example.fieldsync_inventory_app.domain.model.RiceGeneration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RiceGenerationRepositoryImpl @Inject constructor(
    private val api: RiceGenerationApi,
    private val dao: RiceGenerationDao
) : RiceGenerationRepository {

    override suspend fun syncRiceGenerations() {
        // Fetch from network
        try {
            val networkRiceGenerations = api.getRiceGenerations()
            //Log.d(\"StockMovementTypeRepo\", \"Fetched from network: ${networkRiceGenerations.map { it.name }}\")
            dao.insertAll(networkRiceGenerations.map { it.toEntity() })
            Log.d("RiceGenerationRepository", "Rice generations synced successfully")
        } catch (e: Exception) {
            // Handle error
            Log.e("RiceGenerationRepository", "Error fetching from network", e)
            throw e // Re-throw to allow the ViewModel to handle the error state
        }
    }

    override suspend fun saveRiceGeneration(riceGeneration: RiceGeneration) {
        if (riceGeneration.id == 0) {
            val response = api.createRiceGeneration(riceGeneration.toRequestDto())
            dao.insert(response.toEntity())
        } else {
            val response = api.updateRiceGeneration(riceGeneration.id, riceGeneration.toRequestDto())
            dao.insert(response.toEntity())
        }
    }

    override suspend fun deleteRiceGeneration(id: Int) {
        api.deleteRiceGeneration(id)
        dao.deleteById(id)
    }

    // -- Local Database --
    override fun getLocalRiceGenerations(): Flow<List<RiceGeneration>> = flow {
        dao.getRiceGenerations().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }

    override fun getLocalRiceGenerationByName(name: String): Flow<RiceGeneration?> = // Added this method
        dao.getByName(name).map { entity -> entity?.toDomain() } // Added this line
}
