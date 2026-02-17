package com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.domain.model.RiceVariety
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.state.VarietyFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.state.VarietyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewRiceVarietyFormViewModel() : ViewModel(), IRiceVarietyViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _varietyUiState = MutableStateFlow(VarietyUiState())
    override val varietyUiState: StateFlow<VarietyUiState> = _varietyUiState.asStateFlow()

    private val _varietyFormUiState = MutableStateFlow(
        VarietyFormUiState(
            name = "Hom Mali 105",
            description = "Premium grade white jasmine rice, known for its aroma and soft texture.",
            isEditing = true
        )
    )
    override val varietyFormUiState: StateFlow<VarietyFormUiState> = _varietyFormUiState.asStateFlow()

    override val selectedVariety: RiceVariety = RiceVariety(
        id = 1,
        name = "Hom Mali 105",
        description = "Premium grade white jasmine rice, known for its aroma and soft texture.",
        imageUrl = null,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        deletedAt = null
    )

    private val _isFirstLaunch = MutableStateFlow(false)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun onEditVarietyItemClick(selectedVariety: RiceVariety) {}

    override fun onDeleteVarietyItemClick(selectedVariety: RiceVariety) {}

    override fun onAddNewVarietyClick() {}

    override fun onNameChange(name: String) {
        _varietyFormUiState.value = _varietyFormUiState.value.copy(name = name)
    }

    override fun onDescriptionChange(description: String) {
        _varietyFormUiState.value = _varietyFormUiState.value.copy(description = description)
    }

    override fun onSubmitClick() {
        // No-op for preview
    }

    override fun firstLaunch() {
        _isFirstLaunch.value = false
    }
}