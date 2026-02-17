package com.example.fieldsync_inventory_app.ui.reference.entity_management.user.state

import com.example.fieldsync_inventory_app.domain.model.Role

data class UserFormUiState(
    val username: String = "",
    val password: String = "",
    val isActive: Boolean = true,
    val roles: List<String> = emptyList(), // Selected roles (names)
    val availableRoles: List<Role> = emptyList(), // All roles from DB
    val isEditing: Boolean = false,
    val isFirstLaunch: Boolean = true
)
