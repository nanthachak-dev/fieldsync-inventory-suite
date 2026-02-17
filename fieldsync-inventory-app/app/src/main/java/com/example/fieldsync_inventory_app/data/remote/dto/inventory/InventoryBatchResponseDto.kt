package com.example.fieldsync_inventory_app.data.remote.dto.inventory

import com.google.gson.annotations.SerializedName

data class InventoryBatchResponseDto(
    @SerializedName("totalStock")
    val totalStock: Double,
    @SerializedName("totalGraded")
    val totalGraded: Double,
    @SerializedName("totalUngraded")
    val totalUngraded: Double,
    @SerializedName("totalGerminated")
    val totalGerminated: Double,
    @SerializedName("totalUngerminated")
    val totalUngerminated: Double,
    @SerializedName("batches")
    val batches: List<BatchStockDto>
)

data class BatchStockDto(
    @SerializedName("batchId")
    val batchId: Int,
    @SerializedName("varietyId")
    val varietyId: Int,
    @SerializedName("varietyName")
    val varietyName: String,
    @SerializedName("year")
    val year: Int,
    @SerializedName("seasonId")
    val seasonId: Int,
    @SerializedName("seasonName")
    val seasonName: String,
    @SerializedName("seasonDescription")
    val seasonDescription: String,
    @SerializedName("generationId")
    val generationId: Int,
    @SerializedName("generationName")
    val generationName: String,
    @SerializedName("grading")
    val grading: Boolean,
    @SerializedName("germination")
    val germination: Boolean,
    @SerializedName("stock")
    val stock: Double
)
