package com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.domain.model.Supplier
import com.example.fieldsync_inventory_app.domain.model.SupplierType
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier.state.SupplierFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier.state.SupplierUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewSupplierFormViewModel() : ViewModel(), ISupplierViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _supplierUiState = MutableStateFlow(SupplierUiState())
    override val supplierUiState: StateFlow<SupplierUiState> = _supplierUiState.asStateFlow()

    private val _supplierFormUiState = MutableStateFlow(
        SupplierFormUiState(
            fullName = "Fast Seeds Co.",
            email = "contact@fastseeds.com",
            phone = "202-555-0143",
            address = "Vientiane Capital",
            description = "Key supplier for seasonal varieties",
            selectedSupplierType = SupplierType(2, "Company", "Registered business entities"),
            supplierTypes = listOf(
                SupplierType(1, "Individual", "Direct individual sellers"),
                SupplierType(2, "Company", "Registered business entities")
            ),
            isEditing = true
        )
    )
    override val supplierFormUiState: StateFlow<SupplierFormUiState> = _supplierFormUiState.asStateFlow()

    override val selectedSupplier: Supplier = Supplier(
        id = 1,
        fullName = "Fast Seeds Co.",
        email = "contact@fastseeds.com",
        phone = "202-555-0143",
        address = "Vientiane Capital",
        description = "Key supplier for seasonal varieties",
        supplierTypeId = 2,
        supplierTypeName = "Company"
    )

    private val _isFirstLaunch = MutableStateFlow(false)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun onEditSupplierItemClick(selectedSupplier: Supplier) {}
    override fun onDeleteSupplierItemClick(selectedSupplier: Supplier) {}
    override fun onAddNewSupplierClick() {}
    override fun onFullNameChange(fullName: String) {}
    override fun onEmailChange(email: String) {}
    override fun onPhoneChange(phone: String) {}
    override fun onAddressChange(address: String) {}
    override fun onDescriptionChange(description: String) {}
    override fun onSupplierTypeChange(supplierType: SupplierType) {}
    override fun onSubmitClick() {}
    override fun firstLaunch() {
        _isFirstLaunch.value = false
    }
}
