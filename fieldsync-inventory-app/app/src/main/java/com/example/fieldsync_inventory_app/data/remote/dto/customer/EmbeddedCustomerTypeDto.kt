package com.example.fieldsync_inventory_app.data.remote.dto.customer

// Customer Type DTO for the embedded object within CustomerResponseDto
data class EmbeddedCustomerTypeDto(
    val id: Int,
    val name: String
)