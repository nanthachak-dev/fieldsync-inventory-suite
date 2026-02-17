package com.example.fieldsync_inventory_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fieldsync_inventory_app.data.local.entity.StockMovementTypeEntity as StockMovementTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockMovementTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stockMovementTypes: List<StockMovementTypeEntity>)

    @Query("SELECT * FROM stock_movement_type ORDER BY name ASC")
    fun getStockMovementTypes(): Flow<List<StockMovementTypeEntity>>

    @Query("DELETE FROM stock_movement_type WHERE id = :id")
    suspend fun deleteById(id: Int)

    // The repository also calls this for a single insert/update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stockMovementType: StockMovementTypeEntity)
}
