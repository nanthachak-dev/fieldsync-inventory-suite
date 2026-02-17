package com.example.fieldsync_inventory_app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supplier")
data class SupplierEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "supplier_type_id")
    val supplierTypeId: Int,
    @ColumnInfo(name = "supplier_type_name")
    val supplierTypeName: String,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    val email: String?,
    val phone: String?,
    val address: String?,
    val description: String?,
)
