package com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type.state

import com.example.fieldsync_inventory_app.domain.model.CustomerType

data class CustomerTypeUiState(
    val customerTypes: List<CustomerType> = emptyList(),
    val searchQuery: String? = null,
)
