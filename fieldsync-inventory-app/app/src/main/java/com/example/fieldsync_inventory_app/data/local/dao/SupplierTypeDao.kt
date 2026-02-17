package com.example.fieldsync_inventory_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fieldsync_inventory_app.data.local.entity.SupplierTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(supplierTypes: List<SupplierTypeEntity>)

    @Query("SELECT * FROM supplier_type ORDER BY name ASC")
    fun getSupplierTypes(): Flow<List<SupplierTypeEntity>>

    @Query("DELETE FROM supplier_type WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(supplierType: SupplierTypeEntity)
}