package com.example.fieldsync_inventory_app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_sync")
data class LastSyncEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    @ColumnInfo(name = "last_sync_time")
    val lastSyncTime: Long
)
