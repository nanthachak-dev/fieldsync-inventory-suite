package com.example.fieldsync_inventory_app.domain.model.inventory

data class Inventory(
    val totalStock: Double,
    val totalGraded: Double,
    val totalUngraded: Double,
    val totalGerminated: Double,
    val totalUngerminated: Double,
    val varieties: List<VarietyStock>
)

data class VarietyStock(
    val varietyId: Int,
    val varietyName: String,
    val stock: Double,
    val graded: Double,
    val ungraded: Double,
    val germinated: Double,
    val ungerminated: Double
)
