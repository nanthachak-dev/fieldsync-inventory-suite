package com.example.fieldsync_inventory_app.data.remote.dto.customer

// Request DTO (remains the same as it only needs the ID for creation/update)
data class CustomerRequestDto(
    val customerTypeId: Int,
    val fullName: String,
    val email: String?,
    val phone: String?,
    val address: String?
)