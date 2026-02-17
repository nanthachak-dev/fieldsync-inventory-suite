package com.example.fieldsync_inventory_app.data.remote.dto.stock

import com.google.gson.annotations.SerializedName

data class VarietySummaryResponseDto(
    @SerializedName("content")
    val content: List<VarietySummaryItemDto>,
    @SerializedName("pageNumber")
    val pageNumber: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("totalElements")
    val totalElements: Long,
    @SerializedName("totalPages")
    val totalPages: Int,
    @SerializedName("last")
    val last: Boolean
)

data class VarietySummaryItemDto(
    @SerializedName("varietyId")
    val varietyId: Int,
    @SerializedName("varietyName")
    val varietyName: String,
    @SerializedName("stock")
    val stock: Double,
    @SerializedName("r1Stock")
    val r1Stock: Double,
    @SerializedName("r2Stock")
    val r2Stock: Double,
    @SerializedName("r3Stock")
    val r3Stock: Double,
    @SerializedName("graded")
    val graded: Double,
    @SerializedName("r1Graded")
    val r1Graded: Double,
    @SerializedName("r2Graded")
    val r2Graded: Double,
    @SerializedName("r3Graded")
    val r3Graded: Double,
    @SerializedName("ungraded")
    val ungraded: Double,
    @SerializedName("r1Ungraded")
    val r1Ungraded: Double,
    @SerializedName("r2Ungraded")
    val r2Ungraded: Double,
    @SerializedName("r3Ungraded")
    val r3Ungraded: Double,
    @SerializedName("germinated")
    val germinated: Double,
    @SerializedName("r1Germinated")
    val r1Germinated: Double,
    @SerializedName("r2Germinated")
    val r2Germinated: Double,
    @SerializedName("r3Germinated")
    val r3Germinated: Double,
    @SerializedName("ungerminated")
    val ungerminated: Double,
    @SerializedName("r1Ungerminated")
    val r1Ungerminated: Double,
    @SerializedName("r2Ungerminated")
    val r2Ungerminated: Double,
    @SerializedName("r3Ungerminated")
    val r3Ungerminated: Double
)
