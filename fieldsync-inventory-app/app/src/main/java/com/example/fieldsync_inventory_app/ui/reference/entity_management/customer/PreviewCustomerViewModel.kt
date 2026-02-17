package com.example.fieldsync_inventory_app.ui.reference.entity_management.customer

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.domain.model.Customer
import com.example.fieldsync_inventory_app.domain.model.CustomerType
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer.state.CustomerFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer.state.CustomerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewCustomerViewModel() : ViewModel(), ICustomerViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _customerUiState = MutableStateFlow(
        CustomerUiState(
            customers = listOf(
                Customer(
                    id = 1,
                    customerTypeId = 1,
                    customerTypeName = "Wholesale",
                    fullName = "John Doe",
                    email = "john@example.com",
                    phone = "12345678",
                    address = "Vientiane",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    deletedAt = null
                ),
                Customer(
                    id = 2,
                    customerTypeId = 2,
                    customerTypeName = "Retail",
                    fullName = "Jane Smith",
                    email = "jane@example.com",
                    phone = "87654321",
                    address = "Luang Prabang",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    deletedAt = null
                )
            )
        )
    )
    override val customerUiState: StateFlow<CustomerUiState> = _customerUiState.asStateFlow()

    private val _customerFormUiState = MutableStateFlow(CustomerFormUiState())
    override val customerFormUiState: StateFlow<CustomerFormUiState> = _customerFormUiState.asStateFlow()

    override var selectedCustomer: Customer? = null

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun onEditCustomerItemClick(selectedCustomer: Customer) {
        this.selectedCustomer = selectedCustomer
    }

    override fun onDeleteCustomerItemClick(selectedCustomer: Customer) {}

    override fun onAddNewCustomerClick() {
        this.selectedCustomer = null
    }

    override fun onFullNameChange(fullName: String) {}
    override fun onEmailChange(email: String) {}
    override fun onPhoneChange(phone: String) {}
    override fun onAddressChange(address: String) {}
    override fun onCustomerTypeChange(customerType: CustomerType) {}
    override fun onSubmitClick() {}

    override fun firstLaunch() {
        _isFirstLaunch.value = false
    }
}
