package com.example.fieldsync_inventory_app.data.remote.dto.seed_batch

data class SeedBatchRequestDto(
    val varietyId: Int,
    val generationId: Int,
    val seasonId: Int,
    val year: Int,
    val grading: Boolean,
    val germination: Boolean,
    val description: String?
)