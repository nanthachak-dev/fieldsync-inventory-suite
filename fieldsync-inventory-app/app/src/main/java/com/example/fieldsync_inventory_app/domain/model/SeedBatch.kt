package com.example.fieldsync_inventory_app.domain.model

data class SeedBatch(
    val id: Long,
    val varietyId: Int,
    val varietyName: String,
    val generationId: Int,
    val generationName: String,
    val seasonId: Int,
    val seasonName: String,
    val year: Int,
    val grading: Boolean,
    val germination: Boolean,
    val description: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val deletedAt: Long?
)