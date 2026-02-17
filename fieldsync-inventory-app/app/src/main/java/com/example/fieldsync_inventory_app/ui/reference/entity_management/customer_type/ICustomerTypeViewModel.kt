package com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type

import com.example.fieldsync_inventory_app.domain.model.CustomerType
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type.state.CustomerTypeFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type.state.CustomerTypeUiState
import kotlinx.coroutines.flow.StateFlow

interface ICustomerTypeViewModel {
    // Properties
    val resourceUiState: StateFlow<ResourceUiState>
    val customerTypeUiState: StateFlow<CustomerTypeUiState>
    val customerTypeFormUiState: StateFlow<CustomerTypeFormUiState>
    val selectedCustomerType: CustomerType?
    val isFirstLaunch: StateFlow<Boolean>

    // CustomerType screen functions
    fun onEditCustomerTypeItemClick(selectedCustomerType: CustomerType)
    fun onDeleteCustomerTypeItemClick(selectedCustomerType: CustomerType)
    fun onAddNewCustomerTypeClick()

    // CustomerType form functions
    fun onNameChange(name: String)
    fun onDescriptionChange(description: String)
    fun onSubmitClick()

    // General functions
    fun firstLaunch()
}
