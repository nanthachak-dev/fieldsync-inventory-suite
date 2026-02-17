package com.example.fieldsync_inventory_app.data.remote.dto.supplier

data class SupplierRequestDto(
    val fullName: String,
    val email: String?,
    val phone: String?,
    val address: String?,
    val description: String?,
    val supplierTypeId: Int
)
