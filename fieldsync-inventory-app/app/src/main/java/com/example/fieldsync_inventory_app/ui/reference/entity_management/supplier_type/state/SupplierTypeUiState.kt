package com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type.state

import com.example.fieldsync_inventory_app.domain.model.SupplierType

data class SupplierTypeUiState(
    val supplierTypes: List<SupplierType> = emptyList(),
    val searchQuery: String? = null,
)
