package com.example.fieldsync_inventory_app.domain.model

data class Customer(
    val id: Int,
    val customerTypeId: Int,
    val customerTypeName: String,
    val fullName: String,
    val email: String?,
    val phone: String?,
    val address: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val deletedAt: Long?
)