package com.example.fieldsync_inventory_app.ui.reference.entity_management.user.state

import com.example.fieldsync_inventory_app.domain.model.AppUser

data class UserUiState(
    val users: List<AppUser> = emptyList(),
    val searchQuery: String? = null,
    val hideInactive: Boolean = true
)
