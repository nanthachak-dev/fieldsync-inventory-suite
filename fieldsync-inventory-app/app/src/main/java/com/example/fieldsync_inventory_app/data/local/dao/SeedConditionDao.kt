package com.example.fieldsync_inventory_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fieldsync_inventory_app.data.local.entity.SeedConditionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeedConditionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(seedConditions: List<SeedConditionEntity>)

    @Query("SELECT * FROM seed_condition ORDER BY name ASC")
    fun getSeedConditions(): Flow<List<SeedConditionEntity>>

    @Query("SELECT * FROM seed_condition WHERE name = :name") // Added this line
    fun getByName(name: String): Flow<SeedConditionEntity?> // Added this line

    @Query("DELETE FROM seed_condition WHERE id = :id")
    suspend fun deleteById(id: Int)

    // The repository also calls this for a single insert/update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(seedCondition: SeedConditionEntity)
}
