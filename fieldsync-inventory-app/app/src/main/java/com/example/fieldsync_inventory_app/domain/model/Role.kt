package com.example.fieldsync_inventory_app.domain.model

data class Role(
    val id: Int = 0,
    val name: String,
    val description: String?,
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
    val deletedAt: Long? = null
)
