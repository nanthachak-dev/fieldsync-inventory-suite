package com.example.fieldsync_inventory_app.domain.repository.transaction_type

import android.util.Log
import com.example.fieldsync_inventory_app.data.local.dao.StockTransactionTypeDao
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toEntity
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.StockTransactionTypeApi
import com.example.fieldsync_inventory_app.domain.model.StockTransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockTransactionTypeRepositoryImpl @Inject constructor(
    private val api: StockTransactionTypeApi,
    private val dao: StockTransactionTypeDao
) : StockTransactionTypeRepository {

    override suspend fun syncStockTransactionTypes(){
        // Fetch from network
        try {
            val networkResponse = api.getStockTransactionTypes()
            //Log.d("StockMovementTypeRepo", "Fetched from network: ${networkResponse.map { it.name }}")
            dao.insertAll(networkResponse.map { it.toEntity() })
            Log.d("StockTransactionTypeRepo", "Stock transaction types synced successfully")
        } catch (e: Exception) {
            // Handle error
            Log.e("StockTransactionTypeRepo", "Error fetching from network", e)
            throw e // Re-throw to allow the ViewModel to handle the error state
        }
    }

    override suspend fun saveStockTransactionType(stockTransactionType: StockTransactionType) {
        if (stockTransactionType.id == 0) {
            // Create (POST)
            val response = api.createStockTransactionType(stockTransactionType.toRequestDto())
            dao.insert(response.toEntity())
        } else {
            // Update (PUT)
            val response = api.updateStockTransactionType(stockTransactionType.id, stockTransactionType.toRequestDto())
            dao.insert(response.toEntity())
        }
    }

    override suspend fun deleteStockTransactionType(id: Int) {
        // Delete from API and local DB
        api.deleteStockTransactionType(id)
        dao.deleteById(id)
    }

    // -- Local Database --
    override fun getLocalStockTransactionTypes(): Flow<List<StockTransactionType>> = flow {
        dao.getStockTransactionTypes().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }
}