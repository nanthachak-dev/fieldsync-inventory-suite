package com.example.fieldsync_inventory_app.domain.repository.season

import com.example.fieldsync_inventory_app.domain.model.Season
import kotlinx.coroutines.flow.Flow

interface SeasonRepository {
    suspend fun syncSeasons()
    suspend fun saveSeason(season: Season)
    suspend fun deleteSeason(id: Int)

    // -- Local Database --
    fun getLocalSeasons(): Flow<List<Season>>
    fun getLocalSeasonByName(name: String): Flow<Season?> // Added this line
}
