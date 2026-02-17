package com.example.fieldsync_inventory_app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fieldsync_inventory_app.data.local.entity.StockMovementDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockMovementDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<StockMovementDetailsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: StockMovementDetailsEntity)

    @Update
    suspend fun update(transaction: StockMovementDetailsEntity)

    @Delete
    suspend fun delete(transaction: StockMovementDetailsEntity)

    @Query("SELECT * FROM stock_movement_details WHERE deleted_at IS NULL ORDER BY transaction_date DESC, transactionId DESC")
    fun getAllTransactionDetails(): Flow<List<StockMovementDetailsEntity>>

    @Query("SELECT * FROM stock_movement_details WHERE transactionId = :transactionId AND stockMovementId = :stockMovementId")
    suspend fun getTransactionDetailByIds(transactionId: Int, stockMovementId: Int): StockMovementDetailsEntity?

    @Query("DELETE FROM stock_movement_details")
    suspend fun deleteAll()
}
