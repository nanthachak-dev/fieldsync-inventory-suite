package com.example.fieldsync_inventory_app.ui.reference.entity_management.customer

import com.example.fieldsync_inventory_app.domain.model.Customer
import com.example.fieldsync_inventory_app.domain.model.CustomerType
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer.state.CustomerFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer.state.CustomerUiState
import kotlinx.coroutines.flow.StateFlow

interface ICustomerViewModel {
    // Properties
    val resourceUiState: StateFlow<ResourceUiState>
    val customerUiState: StateFlow<CustomerUiState>
    val customerFormUiState: StateFlow<CustomerFormUiState>
    val selectedCustomer: Customer?
    val isFirstLaunch: StateFlow<Boolean>

    // Customer screen functions
    fun onEditCustomerItemClick(selectedCustomer: Customer)
    fun onDeleteCustomerItemClick(selectedCustomer: Customer)
    fun onAddNewCustomerClick()

    // Customer form functions
    fun onFullNameChange(fullName: String)
    fun onEmailChange(email: String)
    fun onPhoneChange(phone: String)
    fun onAddressChange(address: String)
    fun onCustomerTypeChange(customerType: CustomerType)
    fun onSubmitClick()

    // General functions
    fun firstLaunch()
}
