package com.example.fieldsync_inventory_app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seed_batch")
data class SeedBatchEntity(
    @PrimaryKey val id: Long, // Use the ID from the backend as the primary key
    @ColumnInfo(name = "variety_id")
    val varietyId: Int,
    val varietyName: String,
    @ColumnInfo(name = "generation_id")
    val generationId: Int,
    val generationName: String,
    @ColumnInfo(name = "season_id")
    val seasonId: Int,
    val seasonName: String,
    val year: Int,
    val grading: Boolean,
    val germination: Boolean,
    val description: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Long?
)