package com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.state

import android.util.Log
import com.example.fieldsync_inventory_app.domain.model.Customer
import com.example.fieldsync_inventory_app.domain.model.StockMovementType
import com.example.fieldsync_inventory_app.util.constans.StockOutTask
import java.text.NumberFormat
import java.time.LocalDateTime
import java.util.Locale

data class StockOutUiState (
    val taskList: List<StockOutTask> = StockOutTask.entries,
    val selectedTask: StockOutTask? = StockOutTask.SALE,
    val stockMovementTypes: List<StockMovementType> = emptyList(),
    val customerList: List<Customer> = emptyList(),
    val selectedCustomer: String? = null,
    val customerId: Int? = null,
    val transactionTime: LocalDateTime = LocalDateTime.now(), // Holds the current/default date-time
    val note: String? = null,
    val totalQuantity: Double = 0.0,
    val totalPrice: Double = 0.0,
){
    val isFormValid: Boolean
        get() = selectedTask != null

    val totalQuantityDisplay: String
        get() {
            return try {
                // Set currency format
                val laoLocale = Locale.Builder().setLanguage("en").setRegion("US").build()
                val format = NumberFormat.getCurrencyInstance(laoLocale)
                val currency = format.currency

                if (currency != null) {
                    val symbol = currency.getSymbol(laoLocale)
                    if (symbol != null) {
                        return format.format(totalQuantity).replace(symbol, "").trim()
                    }
                }
                // Fallback if currency or symbol is null
                "%.2f".format(totalQuantity)
            } catch (e: Exception) {
                // Fallback for any formatting issue
                Log.e("StockOutUiState", "Error formatting total quantity: ${e.message}")
                "%.2f".format(totalQuantity)
            }
        }
    val totalPriceDisplay: String
        get() {
            return try {
                // Set currency format
                val laoLocale = Locale.Builder().setLanguage("en").setRegion("US").build()
                val format = NumberFormat.getCurrencyInstance(laoLocale)
                val currency = format.currency

                if (currency != null) {
                    val symbol = currency.getSymbol(laoLocale)
                    if (symbol != null) {
                        return format.format(totalPrice).replace(symbol, "").trim()
                    }
                }
                // Fallback if currency or symbol is null
                "%.2f".format(totalPrice)
            } catch (e: Exception) {
                Log.e("StockOutUiState", "Error formatting total price: ${e.message}")
                // Fallback for any formatting issue
                "%.2f".format(totalPrice)
            }
        }
}