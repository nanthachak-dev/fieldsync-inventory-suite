package com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation

// DTO containing id and name to flatten data of request/response whose fields contain id and name
data class IdNamePairDto(
    val id: Int,
    val name: String
)