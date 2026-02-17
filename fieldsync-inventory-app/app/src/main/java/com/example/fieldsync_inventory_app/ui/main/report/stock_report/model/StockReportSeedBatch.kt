package com.example.fieldsync_inventory_app.ui.main.report.stock_report.model

data class StockReportSeedBatch(
    val varietyId: Int,
    val varietyName: String,
    val year: Int,
    val seasonId: Int,
    val seasonName: String,
    val seasonDescription: String,
    val generationId: Int,
    val generationName: String,
    val graded: Double, // Total movement's quantity whose grading is true
    val ungraded: Double, // Total movement's quantity stock whose grading is false
    val germination: Boolean,
)

fun StockReportSeedBatch.displayGermination(): String {
    return if (germination) "Germinated" else "Ungerminated"
}
