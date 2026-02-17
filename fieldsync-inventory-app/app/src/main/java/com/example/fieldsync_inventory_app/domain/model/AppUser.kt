package com.example.fieldsync_inventory_app.domain.model

data class AppUser(
    val id: Int = 0,
    val username: String,
    val password: String? = null,
    val roles: List<String>,
    val isActive: Boolean,
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
    val deletedAt: Long? = null
)
