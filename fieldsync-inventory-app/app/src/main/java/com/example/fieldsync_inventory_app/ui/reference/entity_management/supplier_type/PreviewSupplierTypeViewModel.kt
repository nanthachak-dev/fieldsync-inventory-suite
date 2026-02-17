package com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.domain.model.SupplierType
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type.state.SupplierTypeFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type.state.SupplierTypeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewSupplierTypeViewModel() : ViewModel(), ISupplierTypeViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _supplierTypeUiState = MutableStateFlow(
        SupplierTypeUiState(
            supplierTypes = listOf(
                SupplierType(1, "Individual", "Direct individual sellers"),
                SupplierType(2, "Company", "Registered business entities"),
                SupplierType(3, "Organization", "NGOs and other groups")
            )
        )
    )
    override val supplierTypeUiState: StateFlow<SupplierTypeUiState> = _supplierTypeUiState.asStateFlow()

    private val _supplierTypeFormUiState = MutableStateFlow(SupplierTypeFormUiState())
    override val supplierTypeFormUiState: StateFlow<SupplierTypeFormUiState> = _supplierTypeFormUiState.asStateFlow()

    override var selectedSupplierType: SupplierType? = null

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun onEditSupplierTypeItemClick(selectedSupplierType: SupplierType) {
        this.selectedSupplierType = selectedSupplierType
    }

    override fun onDeleteSupplierTypeItemClick(selectedSupplierType: SupplierType) {}

    override fun onAddNewSupplierTypeClick() {
        this.selectedSupplierType = null
    }

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
