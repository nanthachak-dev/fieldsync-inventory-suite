package com.example.fieldsync_inventory_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fieldsync_inventory_app.data.local.entity.StockTransactionTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockTransactionTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stockTransactionTypes: List<StockTransactionTypeEntity>)

    @Query("SELECT * FROM stock_transaction_type ORDER BY name ASC")
    fun getStockTransactionTypes(): Flow<List<StockTransactionTypeEntity>>

    @Query("DELETE FROM stock_transaction_type WHERE id = :id")
    suspend fun deleteById(id: Int)

    // The repository also calls this for a single insert/update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stockTransactionType: StockTransactionTypeEntity)
}