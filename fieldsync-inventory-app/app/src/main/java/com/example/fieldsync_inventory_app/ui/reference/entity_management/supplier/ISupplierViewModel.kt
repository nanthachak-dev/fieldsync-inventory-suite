package com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier

import com.example.fieldsync_inventory_app.domain.model.Supplier
import com.example.fieldsync_inventory_app.domain.model.SupplierType
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier.state.SupplierFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier.state.SupplierUiState
import kotlinx.coroutines.flow.StateFlow

interface ISupplierViewModel {
    // Properties
    val resourceUiState: StateFlow<ResourceUiState>
    val supplierUiState: StateFlow<SupplierUiState>
    val supplierFormUiState: StateFlow<SupplierFormUiState>
    val selectedSupplier: Supplier?
    val isFirstLaunch: StateFlow<Boolean>

    // Supplier screen functions
    fun onEditSupplierItemClick(selectedSupplier: Supplier)
    fun onDeleteSupplierItemClick(selectedSupplier: Supplier)
    fun onAddNewSupplierClick()

    // Supplier form functions
    fun onFullNameChange(fullName: String)
    fun onEmailChange(email: String)
    fun onPhoneChange(phone: String)
    fun onAddressChange(address: String)
    fun onDescriptionChange(description: String)
    fun onSupplierTypeChange(supplierType: SupplierType)
    fun onSubmitClick()

    // General functions
    fun firstLaunch()
}
