package com.example.fieldsync_inventory_app.data.remote.dto.supplier

import com.example.fieldsync_inventory_app.data.remote.dto.supplier_type.SupplierTypeResponseDto

data class SupplierResponseDto(
    val id: Int,
    val fullName: String,
    val email: String?,
    val phone: String?,
    val address: String?,
    val description: String?,
    val supplierType: SupplierTypeResponseDto
)
