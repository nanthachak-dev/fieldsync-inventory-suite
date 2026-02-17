package com.example.fieldsync_inventory_app.data.remote.dto.stock

import com.google.gson.annotations.SerializedName

data class StockSummaryResponseDto(
    @SerializedName("totalStock")
    val totalStock: Double,
    @SerializedName("totalR1Stock")
    val totalR1Stock: Double,
    @SerializedName("totalR2Stock")
    val totalR2Stock: Double,
    @SerializedName("totalR3Stock")
    val totalR3Stock: Double,
    @SerializedName("totalGraded")
    val totalGraded: Double,
    @SerializedName("totalR1Graded")
    val totalR1Graded: Double,
    @SerializedName("totalR2Graded")
    val totalR2Graded: Double,
    @SerializedName("totalR3Graded")
    val totalR3Graded: Double,
    @SerializedName("totalUngraded")
    val totalUngraded: Double,
    @SerializedName("totalR1Ungraded")
    val totalR1Ungraded: Double,
    @SerializedName("totalR2Ungraded")
    val totalR2Ungraded: Double,
    @SerializedName("totalR3Ungraded")
    val totalR3Ungraded: Double,
    @SerializedName("totalGerminated")
    val totalGerminated: Double,
    @SerializedName("totalR1Germinated")
    val totalR1Germinated: Double,
    @SerializedName("totalR2Germinated")
    val totalR2Germinated: Double,
    @SerializedName("totalR3Germinated")
    val totalR3Germinated: Double,
    @SerializedName("totalUngerminated")
    val totalUngerminated: Double,
    @SerializedName("totalR1Ungerminated")
    val totalR1Ungerminated: Double,
    @SerializedName("totalR2Ungerminated")
    val totalR2Ungerminated: Double,
    @SerializedName("totalR3Ungerminated")
    val totalR3Ungerminated: Double
)
