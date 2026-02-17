package com.example.fieldsync_inventory_app.domain.repository.season

import android.util.Log
import com.example.fieldsync_inventory_app.data.local.dao.SeasonDao
import com.example.fieldsync_inventory_app.data.mapper.toDomain
import com.example.fieldsync_inventory_app.data.mapper.toEntity
import com.example.fieldsync_inventory_app.data.mapper.toRequestDto
import com.example.fieldsync_inventory_app.data.remote.api.SeasonApi
import com.example.fieldsync_inventory_app.domain.model.Season
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeasonRepositoryImpl @Inject constructor(
    private val api: SeasonApi,
    private val dao: SeasonDao
) : SeasonRepository {

    override suspend fun syncSeasons(){
        // Fetch from network
        try {
            val networkSeasons = api.getSeasons()
            //Log.d(\"StockMovementTypeRepo\", \"Fetched from network: ${networkSeasons.map { it.name }}\")
            dao.insertAll(networkSeasons.map { it.toEntity() })
            Log.d("SeasonRepository", "Seasons synced successfully")
        } catch (e: Exception) {
            // Handle error
            Log.e("SeasonRepository", "Error fetching from network", e)
            throw e // Re-throw to allow the ViewModel to handle the error state
        }
    }

    override suspend fun saveSeason(season: Season) {
        if (season.id == 0) {
            val response = api.createSeason(season.toRequestDto())
            dao.insert(response.toEntity())
        } else {
            val response = api.updateSeason(season.id, season.toRequestDto())
            dao.insert(response.toEntity())
        }
    }

    override suspend fun deleteSeason(id: Int) {
        api.deleteSeason(id)
        dao.deleteById(id)
    }

    // -- Local Database --
    override fun getLocalSeasons(): Flow<List<Season>> = flow {
        dao.getSeasons().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }

    override fun getLocalSeasonByName(name: String): Flow<Season?> = // Added this method
        dao.getByName(name).map { entity -> entity?.toDomain() } // Added this line
}
