package com.example.fieldsync_inventory_app.ui.main.history.model

enum class MainMovementType(val rawValue: String, val nickname: String) {
    ADJUSTMENT_OUT("ADJUSTMENT_OUT", "Adjustment"),
    PURCHASE("PURCHASE", "Purchase"),
    SALE("SALE", "Sale");

    companion object {
        fun fromRawValue(rawValue: String): String {
            return entries.find { it.rawValue == rawValue }?.nickname ?: rawValue
        }

        fun toRawValue(nickname: String): String {
            return entries.find { it.nickname == nickname }?.rawValue ?: nickname
        }
    }
}
