package com.example.fieldsync_inventory_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fieldsync_inventory_app.data.local.entity.RiceGenerationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RiceGenerationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(riceGenerations: List<RiceGenerationEntity>)

    @Query("SELECT * FROM rice_generation ORDER BY name ASC")
    fun getRiceGenerations(): Flow<List<RiceGenerationEntity>>

    @Query("SELECT * FROM rice_generation WHERE name = :name") // Added this line
    fun getByName(name: String): Flow<RiceGenerationEntity?> // Added this line

    @Query("DELETE FROM rice_generation WHERE id = :id")
    suspend fun deleteById(id: Int)

    // The repository also calls this for a single insert/update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(riceGeneration: RiceGenerationEntity)
}
