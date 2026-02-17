package com.example.fieldsync_inventory_app.util.number

import android.util.Log
import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(value: Double): String {
    return try {
        // Set currency format
        val laoLocale = Locale.Builder().setLanguage("en").setRegion("US").build()
        val format = NumberFormat.getCurrencyInstance(laoLocale)
        val currency = format.currency

        if (currency != null) {
            val symbol = currency.getSymbol(laoLocale)
            if (symbol != null) {
                return format.format(value).replace(symbol, "").trim()
            }
        }
        // Fallback if currency or symbol is null
        "%.2f".format(value)
    } catch (e: Exception) {
        // Fallback for any formatting issue
        Log.e("CurrencyUtils", "Error formatting currency: ${e.message}")
        "%.2f".format(value)
    }
}
