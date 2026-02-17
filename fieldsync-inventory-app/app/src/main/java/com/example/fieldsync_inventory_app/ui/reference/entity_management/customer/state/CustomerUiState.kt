package com.example.fieldsync_inventory_app.ui.reference.entity_management.customer.state

import com.example.fieldsync_inventory_app.domain.model.Customer

data class CustomerUiState(
    val customers: List<Customer> = emptyList(),
    val searchQuery: String? = null,
)
