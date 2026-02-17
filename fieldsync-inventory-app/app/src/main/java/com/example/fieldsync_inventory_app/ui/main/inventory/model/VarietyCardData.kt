package com.example.fieldsync_inventory_app.ui.main.inventory.model

data class VarietyCardData(
    val varietyId: Int,
    val varietyName: String,
    val stock: Double,
    val r1Stock: Double = 0.0,
    val r2Stock: Double = 0.0,
    val r3Stock: Double = 0.0,
    val graded: Double,
    val r1Graded: Double = 0.0,
    val r2Graded: Double = 0.0,
    val r3Graded: Double = 0.0,
    val ungraded: Double,
    val r1Ungraded: Double = 0.0,
    val r2Ungraded: Double = 0.0,
    val r3Ungraded: Double = 0.0,
    val germinated: Double,
    val r1Germinated: Double = 0.0,
    val r2Germinated: Double = 0.0,
    val r3Germinated: Double = 0.0,
    val ungerminated: Double,
    val r1Ungerminated: Double = 0.0,
    val r2Ungerminated: Double = 0.0,
    val r3Ungerminated: Double = 0.0,
)


/**
 * @stock: total of StockTransactionDetail.stockMovementQuantity of the same variety when StockTransactionDetail.movementTypeEffectOnStock=="IN" is + and "OUT" is -
 * @grading: the same as @stock but filtered by StockTransactionDetail.seedBatchGrading==true
 * @germination: the same as @stock but filtered by StockTransactionDetail.seedBatchGermination==true
 */