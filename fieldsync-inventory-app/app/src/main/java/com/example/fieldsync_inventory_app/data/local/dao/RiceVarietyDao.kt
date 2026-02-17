package com.example.fieldsync_inventory_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fieldsync_inventory_app.data.local.entity.RiceVarietyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RiceVarietyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(riceVarieties: List<RiceVarietyEntity>)

    @Query("SELECT * FROM rice_variety ORDER BY name ASC")
    fun getRiceVarieties(): Flow<List<RiceVarietyEntity>>

    @Query("SELECT * FROM rice_variety WHERE name = :name")
    fun getRiceVarietyByName(name: String): Flow<RiceVarietyEntity>

    @Query("DELETE FROM rice_variety WHERE id = :id")
    suspend fun deleteById(id: Int)

    // The repository also calls this for a single insert/update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(variety: RiceVarietyEntity)
}