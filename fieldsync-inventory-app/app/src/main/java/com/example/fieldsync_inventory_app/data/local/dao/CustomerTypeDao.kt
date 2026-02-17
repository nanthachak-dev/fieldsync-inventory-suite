package com.example.fieldsync_inventory_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fieldsync_inventory_app.data.local.entity.CustomerTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(customerTypes: List<CustomerTypeEntity>)

    @Query("SELECT * FROM customer_type ORDER BY name ASC")
    fun getCustomerTypes(): Flow<List<CustomerTypeEntity>>

    @Query("DELETE FROM customer_type WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customerType: CustomerTypeEntity)
}