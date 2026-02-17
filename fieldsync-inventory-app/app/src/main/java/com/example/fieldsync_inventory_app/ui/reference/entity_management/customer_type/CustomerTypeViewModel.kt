package com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.model.CustomerType
import com.example.fieldsync_inventory_app.domain.use_case.customer_type.DeleteCustomerTypeUseCase
import com.example.fieldsync_inventory_app.domain.use_case.customer_type.GetLocalCustomerTypesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.customer_type.SaveCustomerTypeUseCase
import com.example.fieldsync_inventory_app.domain.use_case.customer_type.SyncCustomerTypesUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type.state.CustomerTypeFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type.state.CustomerTypeUiState
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import com.example.fieldsync_inventory_app.util.view_model.startLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerTypeViewModel @Inject constructor(
    private val getLocalCustomerTypesUseCase: GetLocalCustomerTypesUseCase,
    private val syncCustomerTypesUseCase: SyncCustomerTypesUseCase,
    private val saveCustomerTypeUseCase: SaveCustomerTypeUseCase,
    private val deleteCustomerTypeUseCase: DeleteCustomerTypeUseCase
) : ViewModel(), ICustomerTypeViewModel {

    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _customerTypeUiState = MutableStateFlow(CustomerTypeUiState())
    override val customerTypeUiState: StateFlow<CustomerTypeUiState> = _customerTypeUiState.asStateFlow()

    private val _customerTypeFormUiState = MutableStateFlow(CustomerTypeFormUiState())
    override val customerTypeFormUiState: StateFlow<CustomerTypeFormUiState> =
        _customerTypeFormUiState.asStateFlow()

    override var selectedCustomerType: CustomerType? = null

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun firstLaunch() {
        loadDataToCards()
        _isFirstLaunch.update { false }
    }

    override fun onEditCustomerTypeItemClick(selectedCustomerType: CustomerType) {
        _resourceUiState.update { it.copy(isSuccess = false) }
        this.selectedCustomerType = selectedCustomerType
        _customerTypeFormUiState.update {
            it.copy(
                name = selectedCustomerType.name,
                description = selectedCustomerType.description ?: "",
                isEditing = true
            )
        }
    }

    override fun onDeleteCustomerTypeItemClick(selectedCustomerType: CustomerType) {
        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Deleting customer type...")
                deleteCustomerTypeUseCase.invoke(selectedCustomerType.id)
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to delete customer type: ${e.message}")
            }
        }
    }

    override fun onAddNewCustomerTypeClick() {
        _resourceUiState.update { it.copy(isSuccess = false) }
        _customerTypeFormUiState.update {
            it.copy(
                name = "",
                description = "",
                isEditing = false
            )
        }
    }

    override fun onNameChange(name: String) {
        _customerTypeFormUiState.update {
            it.copy(name = name)
        }
    }

    override fun onDescriptionChange(description: String) {
        _customerTypeFormUiState.update {
            it.copy(description = description)
        }
    }

    override fun onSubmitClick() {
        if (_customerTypeFormUiState.value.isEditing) {
            updateCustomerType()
        } else {
            createCustomerType()
        }
    }

    private fun createCustomerType() {
        if (_customerTypeFormUiState.value.name.isEmpty()) {
            _resourceUiState.update {
                it.copy(error = "Please fill in Name field")
            }
            return
        }

        val newCustomerType = CustomerType(
            id = 0,
            name = _customerTypeFormUiState.value.name,
            description = _customerTypeFormUiState.value.description,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            deletedAt = null
        )

        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Creating new customer type...")
                saveCustomerTypeUseCase.invoke(newCustomerType)
                selectedCustomerType = null
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to create new customer type: ${e.message}")
            }
        }
    }

    private fun updateCustomerType() {
        if (_customerTypeFormUiState.value.name.isEmpty()) {
            _resourceUiState.update {
                it.copy(error = "Please fill in Name field")
            }
            return
        }

        val updatedCustomerType = selectedCustomerType?.copy(
            name = _customerTypeFormUiState.value.name,
            description = _customerTypeFormUiState.value.description,
            updatedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Updating customer type...")
                saveCustomerTypeUseCase.invoke(updatedCustomerType!!)
                selectedCustomerType = null
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to update customer type: ${e.message}")
            }
        }
    }

    private fun loadDataToCards() {
        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Retrieving customer types from the server...")
                syncCustomerTypesUseCase.sync()
                getLocalCustomerTypesUseCase().collect { customerTypes ->
                    _customerTypeUiState.update {
                        it.copy(customerTypes = customerTypes)
                    }
                    if (_resourceUiState.value.isLoading) {
                        _resourceUiState.endLoading()
                    }
                }
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to retrieve customer types: ${e.message}")
            }
        }
    }
}