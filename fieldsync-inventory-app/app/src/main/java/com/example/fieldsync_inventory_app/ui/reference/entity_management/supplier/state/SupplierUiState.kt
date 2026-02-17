package com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier.state

import com.example.fieldsync_inventory_app.domain.model.Supplier

data class SupplierUiState(
    val suppliers: List<Supplier> = emptyList(),
    val searchQuery: String? = null,
)
