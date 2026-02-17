package com.example.fieldsync_inventory_app.ui.main.report.stock_report.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun StockReportSeedBatchFilterMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onFilterChange: (String) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text("All") },
            onClick = {
                onFilterChange("All")
            }
        )
        DropdownMenuItem(
            text = { Text("Good Seed") },
            onClick = {
                onFilterChange("Good Seed")
            }
        )
        DropdownMenuItem(
            text = { Text("Bad Seed") },
            onClick = {
                onFilterChange("Bad Seed")
            }
        )
    }
}
