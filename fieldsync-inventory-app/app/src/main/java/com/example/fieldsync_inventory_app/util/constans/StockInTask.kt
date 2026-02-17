package com.example.fieldsync_inventory_app.util.constans

enum class StockInTask(val displayName: String) {
    //TRANSFER_IN("Transfer In"),
    PURCHASE("Purchase");

    companion object {
        fun fromDisplayName(displayName: String): StockInTask? {
            return entries.find { it.displayName == displayName }
        }
    }
}