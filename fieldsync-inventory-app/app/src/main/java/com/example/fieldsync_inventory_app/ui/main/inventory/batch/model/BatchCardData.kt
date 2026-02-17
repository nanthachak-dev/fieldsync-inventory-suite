package com.example.fieldsync_inventory_app.ui.main.inventory.batch.model

data class BatchCardData(
    val batchId: Long,
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
    val stock: Double,
)

fun BatchCardData.displayGrading(): String {
    return if (grading) "Graded" else "Ungraded"
}

fun BatchCardData.displayGermination(): String {
    return if (germination) "Germinated" else "Ungerminated"
}
