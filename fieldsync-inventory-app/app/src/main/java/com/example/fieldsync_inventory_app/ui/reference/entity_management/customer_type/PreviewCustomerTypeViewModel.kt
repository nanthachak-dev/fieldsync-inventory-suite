package com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.domain.model.CustomerType
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type.state.CustomerTypeFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type.state.CustomerTypeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewCustomerTypeViewModel() : ViewModel(), ICustomerTypeViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _customerTypeUiState = MutableStateFlow(
        CustomerTypeUiState(
            customerTypes = listOf(
                CustomerType(
                    id = 1,
                    name = "Wholesale",
                    description = "Large scale buyers",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    deletedAt = null
                ),
                CustomerType(
                    id = 2,
                    name = "Retail",
                    description = "Individual consumers",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    deletedAt = null
                ),
                CustomerType(
                    id = 3,
                    name = "Government",
                    description = "State agencies",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    deletedAt = null
                )
            )
        )
    )
    override val customerTypeUiState: StateFlow<CustomerTypeUiState> = _customerTypeUiState.asStateFlow()

    private val _customerTypeFormUiState = MutableStateFlow(CustomerTypeFormUiState())
    override val customerTypeFormUiState: StateFlow<CustomerTypeFormUiState> = _customerTypeFormUiState.asStateFlow()

    override var selectedCustomerType: CustomerType? = null

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun onEditCustomerTypeItemClick(selectedCustomerType: CustomerType) {
        this.selectedCustomerType = selectedCustomerType
    }

    override fun onDeleteCustomerTypeItemClick(selectedCustomerType: CustomerType) {}

    override fun onAddNewCustomerTypeClick() {
        this.selectedCustomerType = null
    }

    override fun onNameChange(name: String) {
        _customerTypeFormUiState.value = _customerTypeFormUiState.value.copy(name = name)
    }

    override fun onDescriptionChange(description: String) {
        _customerTypeFormUiState.value = _customerTypeFormUiState.value.copy(description = description)
    }

    override fun onSubmitClick() {}

    override fun firstLaunch() {
        _isFirstLaunch.value = false
    }
}
