package com.example.fieldsync_inventory_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fieldsync_inventory_app.data.local.entity.LastSyncEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LastSyncDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lastSyncEntities: List<LastSyncEntity>)

    @Query("UPDATE last_sync SET last_sync_time = :lastSyncTime WHERE name = :name")
    suspend fun updateLastSync(name: String, lastSyncTime: Long)

    @Query("SELECT * FROM last_sync WHERE name = :name")
    fun getByName(name: String): Flow<LastSyncEntity?>
}
