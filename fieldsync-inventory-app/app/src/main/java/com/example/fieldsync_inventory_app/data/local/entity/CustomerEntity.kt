package com.example.fieldsync_inventory_app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class CustomerEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "customer_type_id")
    val customerTypeId: Int,
    @ColumnInfo(name = "customer_type_name")
    val customerTypeName: String,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    val email: String?,
    val phone: String?,
    val address: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Long?
)