package com.example.fieldsync_inventory_app.domain.repository.rice_variety

import com.example.fieldsync_inventory_app.domain.model.RiceVariety
import kotlinx.coroutines.flow.Flow

interface RiceVarietyRepository {
    suspend fun syncVarieties()
    suspend fun saveVariety(variety: RiceVariety)
    suspend fun deleteVariety(id: Int)

    // -- Local Database --
    fun getLocalVarieties(): Flow<List<RiceVariety>>
    fun getLocalVarietyByName(name: String): Flow<RiceVariety>
}
