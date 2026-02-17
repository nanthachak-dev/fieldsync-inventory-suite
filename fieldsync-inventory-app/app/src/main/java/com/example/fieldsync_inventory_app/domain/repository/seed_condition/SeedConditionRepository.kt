package com.example.fieldsync_inventory_app.domain.repository.seed_condition

import com.example.fieldsync_inventory_app.domain.model.SeedCondition
import kotlinx.coroutines.flow.Flow

interface SeedConditionRepository {
    suspend fun syncSeedConditions()
    suspend fun saveSeedCondition(seedCondition: SeedCondition)
    suspend fun deleteSeedCondition(id: Int)

    // -- Local Database --
    fun getLocalSeedConditions(): Flow<List<SeedCondition>>
    fun getLocalSeedConditionByName(name: String): Flow<SeedCondition?> // Added this line
}
