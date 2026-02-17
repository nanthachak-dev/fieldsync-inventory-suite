package com.example.fieldsync_inventory_app.domain.model

data class LastSync(
    val id: Int,
    val name: String,
    val description: String,
    val lastSyncTime: Long
)