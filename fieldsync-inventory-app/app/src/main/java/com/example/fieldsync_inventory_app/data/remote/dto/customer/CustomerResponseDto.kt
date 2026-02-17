package com.example.fieldsync_inventory_app.data.remote.dto.customer

// Response DTO (Updated to include the embedded CustomerType object)
data class CustomerResponseDto(
    val id: Int,
    val fullName: String,
    val email: String?,
    val phone: String?,
    val address: String?,
    val customerType: EmbeddedCustomerTypeDto,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)