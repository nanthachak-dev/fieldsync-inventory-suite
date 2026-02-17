package com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.seed_batch

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Define a data class to represent the seed batch details
@Parcelize
data class SeedBatchResult(
    val seedBatchId: Long? = null,
    val varietyName: String,
    val year: String,
    val season: String,
    val grading: String,
    val generation: String,
    val germination: String,
    val stockDisplay: String // e.g., "Stock: 12,000 Kg - 300 Kg = 11,700 Kg"
) : Parcelable // Must be Parcelable for SavedStateHandle