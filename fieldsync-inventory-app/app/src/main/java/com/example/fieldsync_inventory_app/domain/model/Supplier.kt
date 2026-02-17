package com.example.fieldsync_inventory_app.domain.model

data class Supplier(
    val id: Int,
    val fullName: String,
    val email: String?,
    val phone: String?,
    val address: String?,
    val description: String?,
    val supplierTypeId: Int,
    val supplierTypeName: String
)
