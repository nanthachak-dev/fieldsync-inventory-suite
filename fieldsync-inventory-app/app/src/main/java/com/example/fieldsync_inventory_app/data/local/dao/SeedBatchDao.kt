package com.example.fieldsync_inventory_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fieldsync_inventory_app.data.local.entity.SeedBatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeedBatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(seedBatches: List<SeedBatchEntity>)

    @Query("SELECT * FROM seed_batch ORDER BY year DESC, seasonName ASC")
    fun getSeedBatches(): Flow<List<SeedBatchEntity>>

    @Query("DELETE FROM seed_batch WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(seedBatch: SeedBatchEntity)
}