package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request

data class TOSeedBatchReqDto(
    val id: Long?,
    val varietyId: Int,
    val generationId: Int,
    val seasonId: Int,
    val year: Int,
    val grading: Boolean,
    val germination: Boolean,
    val description: String?
)