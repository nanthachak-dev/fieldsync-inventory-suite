package com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.model.SupplierType
import com.example.fieldsync_inventory_app.domain.use_case.supplier_type.DeleteSupplierTypeUseCase
import com.example.fieldsync_inventory_app.domain.use_case.supplier_type.GetLocalSupplierTypesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.supplier_type.SaveSupplierTypeUseCase
import com.example.fieldsync_inventory_app.domain.use_case.supplier_type.SyncSupplierTypesUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type.state.SupplierTypeFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type.state.SupplierTypeUiState
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import com.example.fieldsync_inventory_app.util.view_model.startLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierTypeViewModel @Inject constructor(
    private val getLocalSupplierTypesUseCase: GetLocalSupplierTypesUseCase,
    private val syncSupplierTypesUseCase: SyncSupplierTypesUseCase,
    private val saveSupplierTypeUseCase: SaveSupplierTypeUseCase,
    private val deleteSupplierTypeUseCase: DeleteSupplierTypeUseCase
) : ViewModel(), ISupplierTypeViewModel {

    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _supplierTypeUiState = MutableStateFlow(SupplierTypeUiState())
    override val supplierTypeUiState: StateFlow<SupplierTypeUiState> = _supplierTypeUiState.asStateFlow()

    private val _supplierTypeFormUiState = MutableStateFlow(SupplierTypeFormUiState())
    override val supplierTypeFormUiState: StateFlow<SupplierTypeFormUiState> =
        _supplierTypeFormUiState.asStateFlow()

    override var selectedSupplierType: SupplierType? = null

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun firstLaunch() {
        loadDataToCards()
        _isFirstLaunch.update { false }
    }

    override fun onEditSupplierTypeItemClick(selectedSupplierType: SupplierType) {
        _resourceUiState.update { it.copy(isSuccess = false) }
        this.selectedSupplierType = selectedSupplierType
        _supplierTypeFormUiState.update {
            it.copy(
                name = selectedSupplierType.name,
                description = selectedSupplierType.description,
                isEditing = true
            )
        }
    }

    override fun onDeleteSupplierTypeItemClick(selectedSupplierType: SupplierType) {
        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Deleting supplier type...")
                deleteSupplierTypeUseCase.invoke(selectedSupplierType.id)
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to delete supplier type: ${e.message}")
            }
        }
    }

    override fun onAddNewSupplierTypeClick() {
        _resourceUiState.update { it.copy(isSuccess = false) }
        _supplierTypeFormUiState.update {
            it.copy(
                name = "",
                description = "",
                isEditing = false
            )
        }
    }

    override fun onNameChange(name: String) {
        _supplierTypeFormUiState.update {
            it.copy(name = name)
        }
    }

    override fun onDescriptionChange(description: String) {
        _supplierTypeFormUiState.update {
            it.copy(description = description)
        }
    }

    override fun onSubmitClick() {
        if (_supplierTypeFormUiState.value.isEditing) {
            updateSupplierType()
        } else {
            createSupplierType()
        }
    }

    private fun createSupplierType() {
        if (_supplierTypeFormUiState.value.name.isEmpty()) {
            _resourceUiState.update {
                it.copy(error = "Please fill in Name field")
            }
            return
        }

        val newSupplierType = SupplierType(
            id = 0,
            name = _supplierTypeFormUiState.value.name,
            description = _supplierTypeFormUiState.value.description
        )

        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Creating new supplier type...")
                saveSupplierTypeUseCase.invoke(newSupplierType)
                selectedSupplierType = null
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to create new supplier type: ${e.message}")
            }
        }
    }

    private fun updateSupplierType() {
        if (_supplierTypeFormUiState.value.name.isEmpty()) {
            _resourceUiState.update {
                it.copy(error = "Please fill in Name field")
            }
            return
        }

        val updatedSupplierType = selectedSupplierType?.copy(
            name = _supplierTypeFormUiState.value.name,
            description = _supplierTypeFormUiState.value.description
        )

        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Updating supplier type...")
                saveSupplierTypeUseCase.invoke(updatedSupplierType!!)
                selectedSupplierType = null
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to update supplier type: ${e.message}")
            }
        }
    }

    private fun loadDataToCards() {
        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Retrieving supplier types from the server...")
                syncSupplierTypesUseCase.sync()
                getLocalSupplierTypesUseCase().collect { supplierTypes ->
                    _supplierTypeUiState.update {
                        it.copy(supplierTypes = supplierTypes)
                    }
                    if (_resourceUiState.value.isLoading) {
                        _resourceUiState.endLoading()
                    }
                }
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to retrieve supplier types: ${e.message}")
            }
        }
    }
}