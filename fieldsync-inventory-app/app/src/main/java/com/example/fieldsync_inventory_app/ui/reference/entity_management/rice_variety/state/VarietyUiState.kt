package com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.state

import com.example.fieldsync_inventory_app.domain.model.RiceVariety

data class VarietyUiState(
    val varieties: List<RiceVariety> = emptyList(),
    val searchQuery: String? = null,
)
