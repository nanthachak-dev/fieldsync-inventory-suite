package com.example.fieldsync_inventory_app.util.constans

enum class StockOutTask(val displayName: String) {
    SALE("Sale");
    //TRANSFER_OUT("Transfer Out");

    companion object {
        fun fromDisplayName(displayName: String): StockOutTask? {
            return entries.find { it.displayName == displayName }
        }
    }
}