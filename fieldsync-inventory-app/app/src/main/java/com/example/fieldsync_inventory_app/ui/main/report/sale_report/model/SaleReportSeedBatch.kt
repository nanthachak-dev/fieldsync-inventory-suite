package com.example.fieldsync_inventory_app.ui.main.report.sale_report.model

data class SaleReportSeedBatch(
    val varietyId: Int,
    val varietyName: String,
    val year: Int,
    val seasonId: Int,
    val seasonName: String,
    val seasonDescription: String,
    val generationId: Int,
    val generationName: String,
    val germinated: Double, // Total movement's quantity whose germination is true and sale item is not null
    val ungerminated: Double, // Total movement's quantity stock germination is false and item is not null
    val germinatedSale: Double, // Total movement's whose sale item is not null and germination is true
    val ungerminatedSale: Double, // Total movement's whose sale item is not null and germination is false
    val transactionDate: Long
){
    val totalGermination: Double = germinated + ungerminated
    val totalGerminationSale: Double = germinatedSale + ungerminatedSale
}
