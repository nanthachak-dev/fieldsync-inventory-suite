package com.example.fieldsync_inventory_app.domain.model

data class RiceVariety (
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val deletedAt: Long?
)
