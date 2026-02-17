package com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class StockAdjustmentBatch (
    val seedBatchId: Long?=null, // ID of seed batch in database
    val varietyName: String,
    val varietyId: Int,
    val generation: String,
    val generationId: Int,
    val season: String,
    val seasonDescription: String,
    val seasonId: Int,
    val year: Int,
    val grading: Boolean,
    val germination: Boolean,
){
    companion object {
        // 1. Define the non-null DEFAULT state (the "cleared" data)
        private val DEFAULT_BATCH = StockAdjustmentBatch(
            seedBatchId = null,
            varietyName = "",
            varietyId = 0,
            generation = "",
            generationId = 0,
            season = "",
            seasonDescription = "",
            seasonId = 0,
            year = 0,
            grading = false,
            germination = false,
        )

        // 2. The mutable state holder (the private backing property)
        private var _instance = mutableStateOf(DEFAULT_BATCH)

        // 3. The public, observable singleton instance
        // Uses the 'by' delegate for clean access (StockAdjustmentBatch.INSTANCE)
        var INSTANCE: StockAdjustmentBatch by _instance

        // --- Core Functions for State Management ---

        /**
         * Clears the current batch data by resetting the INSTANCE to the default, empty state.
         */
        fun clear() {
            // Updating INSTANCE automatically notifies Compose listeners
            INSTANCE = DEFAULT_BATCH
            println("StockAdjustmentBatch cleared to default state.")
        }

        /**
         * Loads a new batch into the singleton, triggering a UI update.
         */
        fun loadBatch(newBatch: StockAdjustmentBatch) {
            INSTANCE = newBatch
            println("StockAdjustmentBatch loaded: ${newBatch.varietyName}")
        }

        /**
         * Checks if the current INSTANCE is exactly equal to the default, cleared state.
         *
         * @return True if the batch data is currently in the cleared state, false otherwise.
         */
        fun isCleared(): Boolean {
            // Data classes automatically implement the 'equals' function,
            // which compares the values of all properties.
            return INSTANCE == DEFAULT_BATCH
        }
    }
}