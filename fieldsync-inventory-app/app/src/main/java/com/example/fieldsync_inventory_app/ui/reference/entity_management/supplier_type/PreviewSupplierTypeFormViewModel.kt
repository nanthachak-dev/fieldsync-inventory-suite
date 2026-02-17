package com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.domain.model.SupplierType
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type.state.SupplierTypeFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type.state.SupplierTypeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewSupplierTypeFormViewModel() : ViewModel(), ISupplierTypeViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _supplierTypeUiState = MutableStateFlow(SupplierTypeUiState())
    override val supplierTypeUiState: StateFlow<SupplierTypeUiState> = _supplierTypeUiState.asStateFlow()

    private val _supplierTypeFormUiState = MutableStateFlow(
        SupplierTypeFormUiState(
            name = "Company",
            description = "Registered business entities",
            isEditing = true
        )
    )
    override val supplierTypeFormUiState: StateFlow<SupplierTypeFormUiState> = _supplierTypeFormUiState.asStateFlow()

    override val selectedSupplierType: SupplierType = SupplierType(2, "Company", "Registered business entities")

    private val _isFirstLaunch = MutableStateFlow(false)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun onEditSupplierTypeItemClick(selectedSupplierType: SupplierType) {}
    override fun onDeleteSupplierTypeItemClick(selectedSupplierType: SupplierType) {}
    override fun onAddNewSupplierTypeClick() {}
    override fun onNameChange(name: String) {
        _supplierTypeFormUiState.value = _supplierTypeFormUiState.value.copy(name = name)
    }
    override fun onDescriptionChange(description: String) {
        _supplierTypeFormUiState.value = _supplierTypeFormUiState.value.copy(description = description)
    }
    override fun onSubmitClick() {}
    override fun firstLaunch() {
        _isFirstLaunch.value = false
    }
}
