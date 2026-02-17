package com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.domain.model.RiceVariety
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.state.VarietyFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.state.VarietyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewRiceVarietyViewModel() : ViewModel(), IRiceVarietyViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _varietyUiState = MutableStateFlow(
        VarietyUiState(
            varieties = listOf(
                RiceVariety(
                    id = 1,
                    name = "Hom Mali 105",
                    description = "Premium grade white jasmine rice, known for its aroma and soft texture.",
                    imageUrl = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    deletedAt = null
                ),
                RiceVariety(
                    id = 2,
                    name = "RD6",
                    description = "High-quality glutinous (sticky) rice, popular for its fragrance.",
                    imageUrl = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    deletedAt = null
                ),
                RiceVariety(
                    id = 3,
                    name = "Riceberry",
                    description = "Cross-bred nutrient-dense deep purple whole grain rice.",
                    imageUrl = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    deletedAt = null
                ),
                RiceVariety(
                    id = 4,
                    name = "Pathum Thani 1",
                    description = "Fragrant rice variety resistant to pests and diseases.",
                    imageUrl = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    deletedAt = null
                )
            )
        )
    )
    override val varietyUiState: StateFlow<VarietyUiState> = _varietyUiState.asStateFlow()

    private val _varietyFormUiState = MutableStateFlow(VarietyFormUiState())
    override val varietyFormUiState: StateFlow<VarietyFormUiState> = _varietyFormUiState.asStateFlow()

    private var _selectedVariety: RiceVariety? = null
    override val selectedVariety: RiceVariety?
        get() = _selectedVariety

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    override fun onEditVarietyItemClick(selectedVariety: RiceVariety) {
        _selectedVariety = selectedVariety
    }

    override fun onDeleteVarietyItemClick(selectedVariety: RiceVariety) {
        // No-op for preview
    }

    override fun onAddNewVarietyClick() {
        _selectedVariety = null
    }

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