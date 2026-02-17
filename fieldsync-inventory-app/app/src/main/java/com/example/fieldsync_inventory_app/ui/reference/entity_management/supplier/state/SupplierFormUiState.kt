package com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier.state

import com.example.fieldsync_inventory_app.domain.model.SupplierType

data class SupplierFormUiState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val description: String = "",
    val selectedSupplierType: SupplierType? = null,
    val supplierTypes: List<SupplierType> = emptyList(),
    val isEditing: Boolean = false
)
