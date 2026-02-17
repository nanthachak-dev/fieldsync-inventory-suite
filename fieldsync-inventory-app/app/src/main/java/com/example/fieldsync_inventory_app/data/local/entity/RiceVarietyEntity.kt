package com.example.fieldsync_inventory_app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rice_variety")
data class RiceVarietyEntity(
    @PrimaryKey val id: Int, // Use the ID from the backend as the primary key
    val name: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    val description: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Long?
)