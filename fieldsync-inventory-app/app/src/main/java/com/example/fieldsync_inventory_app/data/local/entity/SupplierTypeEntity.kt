package com.example.fieldsync_inventory_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supplier_type")
data class SupplierTypeEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
)