package com.example.fieldsync_inventory_app.domain.repository.stock_movement_type

import android.util.Log
import com.example.fieldsync_inventory_app.data.local.dao.StockMovementTypeDao
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toEntity
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.StockMovementTypeApi
import com.example.fieldsync_inventory_app.domain.model.StockMovementType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockMovementTypeRepositoryImpl @Inject constructor(
    private val api: StockMovementTypeApi,
    private val dao: StockMovementTypeDao
) : StockMovementTypeRepository {

    override suspend fun syncStockMovementTypes() {
        // Fetch from network and save to local DB
        try {
            val networkTypes = api.getStockMovementTypes()
            //Log.d("StockMovementTypeRepo", "Fetched from network: ${networkTypes.map { it.name }}")
            dao.insertAll(networkTypes.map { it.toEntity() }) // Save to local database
            Log.d("StockMovementTypeRepo", "Stock movement types synced successfully")
        } catch (e: Exception) {
            // Handle error (e.g., log it or emit a wrapped error result)
            Log.e("StockMovementTypeRepo", "Error fetching from network", e)
            throw e // Re-throw to allow the ViewModel to handle the error state
        }
    }

    override suspend fun saveStockMovementType(stockMovementType: StockMovementType) {
        val response = if (stockMovementType.id == 0) {
            // Create (POST) if ID is 0 (new item)
            api.createStockMovementType(stockMovementType.toRequestDto())
        } else {
            // Update (PUT) if ID is present
            api.updateStockMovementType(stockMovementType.id, stockMovementType.toRequestDto())
        }
        dao.insert(response.toEntity()) // Save/update local database with the response
    }

    override suspend fun deleteStockMovementType(id: Int) {
        // Delete from network first
        api.deleteStockMovementType(id)
        // Then delete from local database
        dao.deleteById(id)
    }

    override fun getLocalStockMovementTypes(): Flow<List<StockMovementType>> = flow {
        // Collect entities from DAO and map to domain models
        dao.getStockMovementTypes().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }
}