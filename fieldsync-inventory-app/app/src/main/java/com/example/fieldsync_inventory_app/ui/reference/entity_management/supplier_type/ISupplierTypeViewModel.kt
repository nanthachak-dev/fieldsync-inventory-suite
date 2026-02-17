package com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type

import com.example.fieldsync_inventory_app.domain.model.SupplierType
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type.state.SupplierTypeFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type.state.SupplierTypeUiState
import kotlinx.coroutines.flow.StateFlow

interface ISupplierTypeViewModel {
    // Properties
    val resourceUiState: StateFlow<ResourceUiState>
    val supplierTypeUiState: StateFlow<SupplierTypeUiState>
    val supplierTypeFormUiState: StateFlow<SupplierTypeFormUiState>
    val selectedSupplierType: SupplierType?
    val isFirstLaunch: StateFlow<Boolean>

    // SupplierType screen functions
    fun onEditSupplierTypeItemClick(selectedSupplierType: SupplierType)
    fun onDeleteSupplierTypeItemClick(selectedSupplierType: SupplierType)
    fun onAddNewSupplierTypeClick()

    // SupplierType form functions
    fun onNameChange(name: String)
    fun onDescriptionChange(description: String)
    fun onSubmitClick()

    // General functions
    fun firstLaunch()
}
