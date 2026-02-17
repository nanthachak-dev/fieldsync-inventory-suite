package com.example.fieldsync_inventory_app.ui.reference.entity_management.customer.state

import com.example.fieldsync_inventory_app.domain.model.CustomerType

data class CustomerFormUiState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val selectedCustomerType: CustomerType? = null,
    val customerTypes: List<CustomerType> = emptyList(),
    val isEditing: Boolean = false
)
