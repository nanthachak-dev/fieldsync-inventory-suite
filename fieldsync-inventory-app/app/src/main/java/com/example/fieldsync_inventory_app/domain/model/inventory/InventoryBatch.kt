package com.example.fieldsync_inventory_app.domain.model.inventory

data class InventoryBatch(
    val totalStock: Double,
    val totalGraded: Double,
    val totalUngraded: Double,
    val totalGerminated: Double,
    val totalUngerminated: Double,
    val batches: List<BatchStock>
)

data class BatchStock(
    val batchId: Int,
    val varietyId: Int,
    val varietyName: String,
    val year: Int,
    val seasonId: Int,
    val seasonName: String,
    val seasonDescription: String,
    val generationId: Int,
    val generationName: String,
    val grading: Boolean,
    val germination: Boolean,
    val stock: Double
)
