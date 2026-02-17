package com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.model.Supplier
import com.example.fieldsync_inventory_app.domain.model.SupplierType
import com.example.fieldsync_inventory_app.domain.use_case.supplier.DeleteSupplierUseCase
import com.example.fieldsync_inventory_app.domain.use_case.supplier.GetLocalSuppliersUseCase
import com.example.fieldsync_inventory_app.domain.use_case.supplier.SaveSupplierUseCase
import com.example.fieldsync_inventory_app.domain.use_case.supplier.SyncSuppliersUseCase
import com.example.fieldsync_inventory_app.domain.use_case.supplier_type.GetLocalSupplierTypesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.supplier_type.SyncSupplierTypesUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier.state.SupplierFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier.state.SupplierUiState
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import com.example.fieldsync_inventory_app.util.view_model.startLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierViewModel @Inject constructor(
    private val getLocalSuppliersUseCase: GetLocalSuppliersUseCase,
    private val syncSuppliersUseCase: SyncSuppliersUseCase,
    private val saveSupplierUseCase: SaveSupplierUseCase,
    private val deleteSupplierUseCase: DeleteSupplierUseCase,
    private val getLocalSupplierTypesUseCase: GetLocalSupplierTypesUseCase,
    private val syncSupplierTypesUseCase: SyncSupplierTypesUseCase
) : ViewModel(), ISupplierViewModel {

    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _supplierUiState = MutableStateFlow(SupplierUiState())
    override val supplierUiState: StateFlow<SupplierUiState> = _supplierUiState.asStateFlow()

    private val _supplierFormUiState = MutableStateFlow(SupplierFormUiState())
    override val supplierFormUiState: StateFlow<SupplierFormUiState> =
        _supplierFormUiState.asStateFlow()

    override var selectedSupplier: Supplier? = null

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun firstLaunch() {
        loadDataToCards()
        loadSupplierTypes()
        _isFirstLaunch.update { false }
    }

    override fun onEditSupplierItemClick(selectedSupplier: Supplier) {
        _resourceUiState.update { it.copy(isSuccess = false) }
        this.selectedSupplier = selectedSupplier
        _supplierFormUiState.update {
            it.copy(
                fullName = selectedSupplier.fullName,
                email = selectedSupplier.email ?: "",
                phone = selectedSupplier.phone ?: "",
                address = selectedSupplier.address ?: "",
                description = selectedSupplier.description ?: "",
                selectedSupplierType = _supplierFormUiState.value.supplierTypes.find { type -> type.id == selectedSupplier.supplierTypeId },
                isEditing = true
            )
        }
    }

    override fun onDeleteSupplierItemClick(selectedSupplier: Supplier) {
        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Deleting supplier...")
                deleteSupplierUseCase.invoke(selectedSupplier.id)
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to delete supplier: ${e.message}")
            }
        }
    }

    override fun onAddNewSupplierClick() {
        _resourceUiState.update { it.copy(isSuccess = false) }
        _supplierFormUiState.update {
            it.copy(
                fullName = "",
                email = "",
                phone = "",
                address = "",
                description = "",
                selectedSupplierType = null,
                isEditing = false
            )
        }
    }

    override fun onFullNameChange(fullName: String) {
        _supplierFormUiState.update { it.copy(fullName = fullName) }
    }

    override fun onEmailChange(email: String) {
        _supplierFormUiState.update { it.copy(email = email) }
    }

    override fun onPhoneChange(phone: String) {
        _supplierFormUiState.update { it.copy(phone = phone) }
    }

    override fun onAddressChange(address: String) {
        _supplierFormUiState.update { it.copy(address = address) }
    }

    override fun onDescriptionChange(description: String) {
        _supplierFormUiState.update { it.copy(description = description) }
    }

    override fun onSupplierTypeChange(supplierType: SupplierType) {
        _supplierFormUiState.update { it.copy(selectedSupplierType = supplierType) }
    }

    override fun onSubmitClick() {
        if (_supplierFormUiState.value.isEditing) {
            updateSupplier()
        } else {
            createSupplier()
        }
    }

    private fun createSupplier() {
        val uiState = _supplierFormUiState.value
        if (uiState.fullName.isEmpty() || uiState.selectedSupplierType == null) {
            _resourceUiState.update {
                it.copy(error = "Please fill in all required fields (Full Name and Supplier Type)")
            }
            return
        }

        val newSupplier = Supplier(
            id = 0,
            fullName = uiState.fullName,
            email = uiState.email.ifEmpty { null },
            phone = uiState.phone.ifEmpty { null },
            address = uiState.address.ifEmpty { null },
            description = uiState.description.ifEmpty { null },
            supplierTypeId = uiState.selectedSupplierType.id,
            supplierTypeName = uiState.selectedSupplierType.name
        )

        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Creating new supplier...")
                saveSupplierUseCase.invoke(newSupplier)
                selectedSupplier = null
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to create new supplier: ${e.message}")
            }
        }
    }

    private fun updateSupplier() {
        val uiState = _supplierFormUiState.value
        if (uiState.fullName.isEmpty() || uiState.selectedSupplierType == null) {
            _resourceUiState.update {
                it.copy(error = "Please fill in all required fields (Full Name and Supplier Type)")
            }
            return
        }

        val updatedSupplier = selectedSupplier?.copy(
            fullName = uiState.fullName,
            email = uiState.email.ifEmpty { null },
            phone = uiState.phone.ifEmpty { null },
            address = uiState.address.ifEmpty { null },
            description = uiState.description.ifEmpty { null },
            supplierTypeId = uiState.selectedSupplierType.id,
            supplierTypeName = uiState.selectedSupplierType.name
        )

        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Updating supplier...")
                saveSupplierUseCase.invoke(updatedSupplier!!)
                selectedSupplier = null
                loadDataToCards()
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to update supplier: ${e.message}")
            }
        }
    }

    private fun loadDataToCards() {
        viewModelScope.launch {
            try {
                _resourceUiState.startLoading("Retrieving suppliers from the server...")
                syncSuppliersUseCase.sync()
                getLocalSuppliersUseCase().collect { suppliers ->
                    _supplierUiState.update {
                        it.copy(suppliers = suppliers)
                    }
                    if (_resourceUiState.value.isLoading) {
                        _resourceUiState.endLoading()
                    }
                }
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed to retrieve suppliers: ${e.message}")
            }
        }
    }

    private fun loadSupplierTypes() {
        viewModelScope.launch {
            try {
                syncSupplierTypesUseCase.sync()
                getLocalSupplierTypesUseCase().collect { supplierTypes ->
                    _supplierFormUiState.update {
                        it.copy(supplierTypes = supplierTypes)
                    }
                }
            } catch (e: Exception) {
                // Silently fail or log
            }
        }
    }
}