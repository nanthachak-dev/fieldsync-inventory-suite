package com.example.fieldsync_inventory_app.domain.model.transaction_operation

// its id's shared with stock_movement and fk, sale_id is shared with transaction_id
data class TOSaleItem(
    val price: Double,
    val description: String?
)