package com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock

import com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.seed_batch.SeedBatchResult
import java.time.LocalDateTime

data class AdjustStockUiState(
    // Nullable state for the selected seed batch from the 'From' card
    val seedBatchDataFrom: SeedBatchResult? = null,
    val seedBatchDataTo: SeedBatchResult? = null,
    // State for the selected task from the ComboBox
    val selectedTask: String? = null,
    // List of available tasks for the ComboBox
    val taskList: List<String> = listOf("Grading", "Germination"),
    // Add other input states here (quantity, loss, reason, time)
    val quantity: String = "",
    val loss: String = "",
    val reason: String = "",
    val dateTime: LocalDateTime = LocalDateTime.now(), // Holds the current/default date-time
)