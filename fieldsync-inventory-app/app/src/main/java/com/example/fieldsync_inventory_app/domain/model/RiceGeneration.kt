package com.example.fieldsync_inventory_app.domain.model

data class RiceGeneration(
    val id: Int,
    val name: String,
    val description: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val deletedAt: Long?
)
