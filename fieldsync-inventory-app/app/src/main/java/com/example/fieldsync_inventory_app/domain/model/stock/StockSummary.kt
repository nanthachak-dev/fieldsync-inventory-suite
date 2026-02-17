package com.example.fieldsync_inventory_app.domain.model.stock

data class StockSummary(
    val totalStock: Double,
    val totalR1Stock: Double,
    val totalR2Stock: Double,
    val totalR3Stock: Double,
    val totalGraded: Double,
    val totalR1Graded: Double,
    val totalR2Graded: Double,
    val totalR3Graded: Double,
    val totalUngraded: Double,
    val totalR1Ungraded: Double,
    val totalR2Ungraded: Double,
    val totalR3Ungraded: Double,
    val totalGerminated: Double,
    val totalR1Germinated: Double,
    val totalR2Germinated: Double,
    val totalR3Germinated: Double,
    val totalUngerminated: Double,
    val totalR1Ungerminated: Double,
    val totalR2Ungerminated: Double,
    val totalR3Ungerminated: Double
)
