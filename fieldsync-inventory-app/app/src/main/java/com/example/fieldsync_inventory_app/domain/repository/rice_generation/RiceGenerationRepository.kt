package com.example.fieldsync_inventory_app.domain.repository.rice_generation

import com.example.fieldsync_inventory_app.domain.model.RiceGeneration
import kotlinx.coroutines.flow.Flow

interface RiceGenerationRepository {
    suspend fun syncRiceGenerations()
    suspend fun saveRiceGeneration(riceGeneration: RiceGeneration)
    suspend fun deleteRiceGeneration(id: Int)

    // -- Local Database --
    fun getLocalRiceGenerations(): Flow<List<RiceGeneration>>
    fun getLocalRiceGenerationByName(name: String): Flow<RiceGeneration?> // Added this line
}
