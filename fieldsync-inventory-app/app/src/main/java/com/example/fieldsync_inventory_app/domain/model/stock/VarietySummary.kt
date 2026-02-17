package com.example.fieldsync_inventory_app.domain.model.stock

data class VarietySummary(
    val varietyId: Int,
    val varietyName: String,
    val stock: Double,
    val r1Stock: Double,
    val r2Stock: Double,
    val r3Stock: Double,
    val graded: Double,
    val r1Graded: Double,
    val r2Graded: Double,
    val r3Graded: Double,
    val ungraded: Double,
    val r1Ungraded: Double,
    val r2Ungraded: Double,
    val r3Ungraded: Double,
    val germinated: Double,
    val r1Germinated: Double,
    val r2Germinated: Double,
    val r3Germinated: Double,
    val ungerminated: Double,
    val r1Ungerminated: Double,
    val r2Ungerminated: Double,
    val r3Ungerminated: Double
)

data class VarietySummaryPaged(
    val content: List<VarietySummary>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean
)
