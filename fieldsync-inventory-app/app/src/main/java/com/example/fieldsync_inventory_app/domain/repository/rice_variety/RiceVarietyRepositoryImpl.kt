package com.example.fieldsync_inventory_app.domain.repository.rice_variety

import android.util.Log
import com.example.fieldsync_inventory_app.data.local.dao.RiceVarietyDao
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toEntity
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.RiceVarietyApi
import com.example.fieldsync_inventory_app.domain.model.RiceVariety
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RiceVarietyRepositoryImpl @Inject constructor(
    private val api: RiceVarietyApi,
    private val dao: RiceVarietyDao
) : RiceVarietyRepository {

    override suspend fun syncVarieties() {
        // Fetch from network
        try {
            val networkVarieties = api.getRiceVarieties()
            //Log.d("StockMovementTypeRepo", "Fetched from network: ${networkVarieties.map { it.name }}")
            dao.insertAll(networkVarieties.map { it.toEntity() })
            Log.d("RiceVarietyRepository", "Rice varieties synced successfully")
        } catch (e: HttpException) {
            // Handle error
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("RiceVarietyRepository", "Error fetching from network: $errorBody")
            throw e
        } catch (e: Exception) {
            // Handle error
            Log.e("RiceVarietyRepository", "Error fetching from network: ${e.message}")
            throw e // Re-throw to allow the ViewModel to handle the error state
        }
    }

    override suspend fun saveVariety(variety: RiceVariety) {
        try {
            if (variety.id == 0) {
                //Log.d("RiceVarietyRepository", "saveVariety >>> create new variety with data: $variety")
                val response = api.createRiceVariety(variety.toRequestDto())
                Log.d(
                    "RiceVarietyRepository",
                    "saveVariety >>> created successfully with response: $response"
                )
                dao.insert(response.toEntity())
            } else {
                //Log.d("RiceVarietyRepository", "saveVariety >>> update variety with data: $variety")
                val response = api.updateRiceVariety(variety.id, variety.toRequestDto())
                Log.d(
                    "RiceVarietyRepository",
                    "saveVariety >>> updated successfully with response: $response"
                )
                dao.insert(response.toEntity())
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("RiceVarietyRepository", "saveVariety >>> created failed with error: $errorBody")
            throw Exception(errorBody)
        } catch (e: Exception) {
            Log.e(
                "RiceVarietyRepository",
                "saveVariety >>> create failed with unknown error: ${e.message}"
            )
            throw e
        }
    }

    override suspend fun deleteVariety(id: Int) {
        try {
            Log.d("RiceVarietyRepository", "deleteVariety >>> delete variety with id: $id")
            api.deleteRiceVariety(id)
            Log.d("RiceVarietyRepository", "deleteVariety >>> deleted successfully")
            dao.deleteById(id)
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("RiceVarietyRepository", "deleteVariety >>> delete failed with error: $errorBody")
            throw Exception(errorBody)
        }catch (e: Exception){
            Log.e("RiceVarietyRepository", "deleteVariety >>> delete failed with unknown error: ${e.message}")
            throw e
        }
    }

    // -- Local Database --
    override fun getLocalVarieties(): Flow<List<RiceVariety>> = flow {
        dao.getRiceVarieties().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }

    // Get a single variety by name
    override fun getLocalVarietyByName(name: String): Flow<RiceVariety> = flow {
        dao.getRiceVarietyByName(name).collect { entity ->
            emit(entity.toDomain())
        }
    }
}