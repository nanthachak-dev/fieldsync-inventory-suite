package com.example.fieldsync_inventory_app.ui.main.history.model

import com.example.fieldsync_inventory_app.R
import com.example.fieldsync_inventory_app.domain.model.StockTransactionSummary
import com.example.fieldsync_inventory_app.util.number.formatCurrency
import java.util.Calendar
import java.util.Locale

data class TransactionCardData(
    val summary: StockTransactionSummary
) {
    val transactionTypeSymbol: Int
        get() = when (summary.transactionTypeName) {
            "STOCK_OUT" -> if (summary.totalSalePrice != null) R.drawable.ic_currency_kip else R.drawable.ic_out
            "ADJUSTMENT" -> R.drawable.ic_refresh
            "STOCK_IN" -> if (summary.totalPurchasePrice != null) R.drawable.ic_currency_kip else R.drawable.ic_in
            else -> R.drawable.ic_refresh
        }

    val transactionDescription: String
        get() {
            return if (summary.totalSalePrice != null) { // Sale case
                "Sold ${summary.itemCount} items of ${formatCurrency(summary.totalQuantity)} Kg for ${formatCurrency(summary.totalSalePrice)} LAK"
            } else if (summary.totalPurchasePrice != null) { // Purchase case
                "Purchased ${summary.itemCount} items of ${formatCurrency(summary.totalQuantity)} Kg for ${formatCurrency(summary.totalPurchasePrice)} LAK"
            } else if (summary.transactionTypeName == "ADJUSTMENT") { // Stock adjustment case
                try {
                    val fromSeedBatchId = summary.fromBatchId
                    val toSeedBatchId = summary.toBatchId
                    if (fromSeedBatchId != null && toSeedBatchId != null && toSeedBatchId != fromSeedBatchId) {
//                        "Adjusted from batch #$fromSeedBatchId to #$toSeedBatchId for ${
//                            formatCurrency(
//                                summary.totalQuantity
//                            )
//                        } Kg"
                        "Adjusted from batch #$fromSeedBatchId to #$toSeedBatchId " // Temporary
                    } else { // Rare case for adjustment whose only one stock movement record
                        "Adjusted batch #$fromSeedBatchId for ${formatCurrency(summary.totalQuantity)} Kg"
                    }
                } catch (e: Exception) {
                    "Invalid transaction for adjustment: ${e.message}"
                }
            } else { // Transfer in or out cases without sale
                if (summary.transactionTypeName == "STOCK_IN") {
                    "Transferred-in ${summary.itemCount} items of ${formatCurrency(summary.totalQuantity)} Kg"
                } else {
                    "Transferred-out ${summary.itemCount} items of ${formatCurrency(summary.totalQuantity)} Kg"
                }
            }
        }

    val transactionDate: String
        get() {
            val diff = System.currentTimeMillis() - summary.transactionDate
            val minutes = diff / (1000 * 60)
            val hours = diff / (1000 * 60 * 60)
            val days = diff / (1000 * 60 * 60 * 24)

            return when {
                hours < 1 -> "${minutes}m"
                days < 1 -> "${hours}H"
                days < 7 -> "$days days"
                else -> {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = summary.transactionDate
                    "${calendar.get(Calendar.YEAR)}/${
                        calendar.getDisplayName(
                            Calendar.MONTH,
                            Calendar.SHORT,
                            Locale.ENGLISH
                        )
                    }/${
                        calendar.get(
                            Calendar.DAY_OF_MONTH
                        )
                    }"
                }
            }
        }
}
