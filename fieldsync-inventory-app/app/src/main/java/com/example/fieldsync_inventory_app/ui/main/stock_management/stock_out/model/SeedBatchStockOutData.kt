package com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.model

import java.text.NumberFormat
import java.util.Locale

// Define a data class to represent the seed batch card data on stock out screen
data class SeedBatchStockOutData(
    val id: Int = 0, // ID of card
    val seedBatchId: Long?=null, // ID of seed batch in database
    val varietyName: String,
    val varietyId: Int,
    val generation: String,
    val generationId: Int,
    val season: String,
    val seasonId: Int,
    val year: Int,
    val grading: String,
    val gradingValue: Boolean,
    val germination: String,
    val germinationValue: Boolean,
    val totalStock: Double, // e.g., "300 Kg"
    val quantity: Double, // e.g., "300 Kg"
    val price: Double
) {
    val priceDisplay: String
        get() {
            return try {
                // Set currency format
                val laoLocale = Locale.Builder().setLanguage("en").setRegion("US").build()
                val format = NumberFormat.getCurrencyInstance(laoLocale)
                val currency = format.currency

                if (currency != null) {
                    val symbol = currency.getSymbol(laoLocale)
                    if (symbol != null) {
                        return format.format(price*quantity).replace(symbol, "").trim() + " LAK"
                    }
                }
                // Fallback if currency or symbol is null
                "%.2f LAK".format(price*quantity)
            } catch (e: Exception) {
                // Fallback for any formatting issue
                "%.2f LAK".format(price*quantity)
            }
        }

    val quantityDisplay: String
        get() {
            return try {
                // Set currency format
                val laoLocale = Locale.Builder().setLanguage("en").setRegion("US").build()
                val format = NumberFormat.getCurrencyInstance(laoLocale)
                val currency = format.currency

                if (currency != null) {
                    val symbol = currency.getSymbol(laoLocale)
                    if (symbol != null) {
                        return format.format(quantity).replace(symbol, "").trim() + " Kg"
                    }
                }
                // Fallback if currency or symbol is null
                "%.2f Kg".format(quantity)
            } catch (e: Exception) {
                // Fallback for any formatting issue
                "%.2f Kg".format(quantity)
            }
        }
}