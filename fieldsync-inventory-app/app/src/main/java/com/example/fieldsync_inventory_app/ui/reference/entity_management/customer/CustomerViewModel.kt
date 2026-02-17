package com.example.fieldsync_inventory_app.ui.reference.entity_management.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.model.Customer
import com.example.fieldsync_inventory_app.domain.model.CustomerType
import com.example.fieldsync_inventory_app.domain.use_case.customer.DeleteCustomerUseCase
import com.example.fieldsync_inventory_app.domain.use_case.customer.GetLocalCustomersUseCase
import com.example.fieldsync_inventory_app.domain.use_case.customer.SaveCustomerUseCase
import com.example.fieldsync_inventory_app.domain.use_case.customer.SyncCustomersUseCase
import com.example.fieldsync_inventory_app.domain.use_case.customer_type.GetLocalCustomerTypesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.customer_type.SyncCustomerTypesUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer.state.CustomerFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer.state.CustomerUiState
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import com.example.fieldsync_inventory_app.util.view_model.startLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val getLocalCustomersUseCase: GetLocalCustomersUseCase,
    private val syncCustomersUseCase: SyncCustomersUseCase,
    private val saveCustomerUseCase: SaveCustomerUseCase,
    private val deleteCustomerUseCase: DeleteCustomerUseCase,
    private val getLocalCustomerTypesUseCase: GetLocalCustomerTypesUseCase,
    private val syncCustomerTypesUseCase: SyncCustomerTypesUseCase
) : ViewModel(), ICustomerViewModel {

    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _customerUiState = MutableStateFlow(CustomerUiState())
    override val customerUiState: StateFlow<CustomerUiState> = _customerUiState.asStateFlow()

    private val _customerFormUiState = MutableStateFlow(CustomerFormUiState())
    override val customerFormUiState: StateFlow<CustomerFormUiState> =
        _customerFormUiState.asStateFlow()

    override var selectedCustomer: Customer? = null

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun firstLaunch() {
        loadDataToCards()
        loadCustomerTypes()
        _isFirstLaunch.update { false }
    }

    override fun onEditCustomerItemClick(selectedCustomer: Customer) {
        _resourceUiState.update { it.copy(isSuccess = false) }
        this.selectedCustomer = selectedCustomer
        _customerFormUiState.update {
            it.copy(
                fullName = selectedCustomer.fullName,
                email = selectedCustomer.email ?: "",
                phone = selectedCustomer.phone ?: "",
                address = selectedCustomer.address ?: "",
                selectedCustomerType = _customerFormUiState.value.customerTypes.find { type -> type.id == selectedCustomer.customerTypeId },
                isEditing = true
            )
        }
    }

    override fun onDeleteCustomerItemClick(selectedCustomer: Customer) {
        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Deleting customer...")
                deleteCustomerUseCase.invoke(selectedCustomer.id)
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to delete customer: ${e.message}")
            }
        }
    }

    override fun onAddNewCustomerClick() {
        _resourceUiState.update { it.copy(isSuccess = false) }
        _customerFormUiState.update {
            it.copy(
                fullName = "",
                email = "",
                phone = "",
                address = "",
                selectedCustomerType = null,
                isEditing = false
            )
        }
    }

    override fun onFullNameChange(fullName: String) {
        _customerFormUiState.update { it.copy(fullName = fullName) }
    }

    override fun onEmailChange(email: String) {
        _customerFormUiState.update { it.copy(email = email) }
    }

    override fun onPhoneChange(phone: String) {
        _customerFormUiState.update { it.copy(phone = phone) }
    }

    override fun onAddressChange(address: String) {
        _customerFormUiState.update { it.copy(address = address) }
    }

    override fun onCustomerTypeChange(customerType: CustomerType) {
        _customerFormUiState.update { it.copy(selectedCustomerType = customerType) }
    }

    override fun onSubmitClick() {
        if (_customerFormUiState.value.isEditing) {
            updateCustomer()
        } else {
            createCustomer()
        }
    }

    private fun createCustomer() {
        val uiState = _customerFormUiState.value
        if (uiState.fullName.isEmpty() || uiState.selectedCustomerType == null) {
            _resourceUiState.update {
                it.copy(error = "Please fill in all required fields (Full Name and Customer Type)")
            }
            return
        }

        val newCustomer = Customer(
            id = 0,
            customerTypeId = uiState.selectedCustomerType.id,
            customerTypeName = uiState.selectedCustomerType.name,
            fullName = uiState.fullName,
            email = uiState.email.ifEmpty { null },
            phone = uiState.phone.ifEmpty { null },
            address = uiState.address.ifEmpty { null },
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            deletedAt = null
        )

        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Creating new customer...")
                saveCustomerUseCase.invoke(newCustomer)
                selectedCustomer = null
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to create new customer: ${e.message}")
            }
        }
    }

    private fun updateCustomer() {
        val uiState = _customerFormUiState.value
        if (uiState.fullName.isEmpty() || uiState.selectedCustomerType == null) {
            _resourceUiState.update {
                it.copy(error = "Please fill in all required fields (Full Name and Customer Type)")
            }
            return
        }

        val updatedCustomer = selectedCustomer?.copy(
            customerTypeId = uiState.selectedCustomerType.id,
            customerTypeName = uiState.selectedCustomerType.name,
            fullName = uiState.fullName,
            email = uiState.email.ifEmpty { null },
            phone = uiState.phone.ifEmpty { null },
            address = uiState.address.ifEmpty { null },
            updatedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Updating customer...")
                saveCustomerUseCase.invoke(updatedCustomer!!)
                selectedCustomer = null
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to update customer: ${e.message}")
            }
        }
    }

    private fun loadDataToCards() {
        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Retrieving customers from the server...")
                syncCustomersUseCase.sync()
                getLocalCustomersUseCase().collect { customers ->
                    _customerUiState.update {
                        it.copy(customers = customers)
                    }
                    if (_resourceUiState.value.isLoading) {
                        _resourceUiState.endLoading()
                    }
                }
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to retrieve customers: ${e.message}")
            }
        }
    }

    private fun loadCustomerTypes() {
        viewModelScope.launch {
            try {
                syncCustomerTypesUseCase.sync()
                getLocalCustomerTypesUseCase().collect { customerTypes ->
                    _customerFormUiState.update {
                        it.copy(customerTypes = customerTypes)
                    }
                }
            } catch (e: Exception) {
                // Silently fail or log
            }
        }
    }
}