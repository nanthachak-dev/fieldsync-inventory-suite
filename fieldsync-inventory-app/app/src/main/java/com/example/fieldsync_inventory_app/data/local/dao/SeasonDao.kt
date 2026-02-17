package com.example.fieldsync_inventory_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fieldsync_inventory_app.data.local.entity.SeasonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeasonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(seasons: List<SeasonEntity>)

    @Query("SELECT * FROM season ORDER BY name ASC")
    fun getSeasons(): Flow<List<SeasonEntity>>

    @Query("SELECT * FROM season WHERE name = :name") // Added this line
    fun getByName(name: String): Flow<SeasonEntity?> // Added this line

    @Query("DELETE FROM season WHERE id = :id")
    suspend fun deleteById(id: Int)

    // The repository also calls this for a single insert/update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(season: SeasonEntity)
}
