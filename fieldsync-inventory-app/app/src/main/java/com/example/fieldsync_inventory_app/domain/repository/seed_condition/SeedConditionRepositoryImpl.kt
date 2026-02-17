package com.example.fieldsync_inventory_app.domain.repository.seed_condition

import android.util.Log
import com.example.fieldsync_inventory_app.data.local.dao.SeedConditionDao
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toEntity
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.SeedConditionApi
import com.example.fieldsync_inventory_app.domain.model.SeedCondition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeedConditionRepositoryImpl @Inject constructor(
    private val api: SeedConditionApi,
    private val dao: SeedConditionDao
) : SeedConditionRepository {

    override suspend fun syncSeedConditions() {
        // Fetch from network
        try {
            val networkSeedConditions = api.getSeedConditions()
            //Log.d(\"SeedConditionRepository\", \"Fetched from network: ${networkSeedConditions.map { it.name }}\")
            dao.insertAll(networkSeedConditions.map { it.toEntity() })
            Log.d("SeedConditionRepository", "Seed conditions synced successfully")
        } catch (e: Exception) {
            // Handle error
            Log.e("SeedConditionRepository", "Error fetching from network", e)
            throw e // Re-throw to allow the ViewModel to handle the error state
        }
    }

    override suspend fun saveSeedCondition(seedCondition: SeedCondition) {
        if (seedCondition.id == 0) {
            val response = api.createSeedCondition(seedCondition.toRequestDto())
            dao.insert(response.toEntity())
        } else {
            val response = api.updateSeedCondition(seedCondition.id, seedCondition.toRequestDto())
            dao.insert(response.toEntity())
        }
    }

    override suspend fun deleteSeedCondition(id: Int) {
        api.deleteSeedCondition(id)
        dao.deleteById(id)
    }

    // -- Local Database --
    override fun getLocalSeedConditions(): Flow<List<SeedCondition>> = flow {
        dao.getSeedConditions().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }

    override fun getLocalSeedConditionByName(name: String): Flow<SeedCondition?> = // Added this method
        dao.getByName(name).map { entity -> entity?.toDomain() } // Added this line
}
