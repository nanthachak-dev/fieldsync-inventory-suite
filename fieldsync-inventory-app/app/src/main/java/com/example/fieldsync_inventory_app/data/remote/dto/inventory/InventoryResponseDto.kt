package com.example.fieldsync_inventory_app.data.remote.dto.inventory

import com.google.gson.annotations.SerializedName

data class InventoryResponseDto(
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
    @SerializedName("varieties")
    val varieties: List<VarietyStockDto>
)

data class VarietyStockDto(
    @SerializedName("varietyId")
    val varietyId: Int,
    @SerializedName("varietyName")
    val varietyName: String,
    @SerializedName("stock")
    val stock: Double,
    @SerializedName("graded")
    val graded: Double,
    @SerializedName("ungraded")
    val ungraded: Double,
    @SerializedName("germinated")
    val germinated: Double,
    @SerializedName("ungerminated")
    val ungerminated: Double
)
