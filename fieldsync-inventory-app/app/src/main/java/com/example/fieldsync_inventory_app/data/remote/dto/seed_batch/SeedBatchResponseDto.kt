package com.example.fieldsync_inventory_app.data.remote.dto.seed_batch

data class SeedBatchResponseDto(
    val id: Long,
    val variety: IdNamePair,
    val generation: IdNamePair,
    val season: IdNamePair,
    val year: Int,
    val grading: Boolean,
    val germination: Boolean,
    val description: String?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)