package com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety

import com.example.fieldsync_inventory_app.domain.model.RiceVariety
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.state.VarietyFormUiState
import com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.state.VarietyUiState
import kotlinx.coroutines.flow.StateFlow

interface IRiceVarietyViewModel {
    // Properties
    val resourceUiState: StateFlow<ResourceUiState>
    val varietyUiState: StateFlow<VarietyUiState>
    val varietyFormUiState: StateFlow<VarietyFormUiState>
    val selectedVariety: RiceVariety?
    val isFirstLaunch: StateFlow<Boolean>

    // Variety screen functions
    fun onEditVarietyItemClick(selectedVariety: RiceVariety)
    fun onDeleteVarietyItemClick(selectedVariety: RiceVariety)
    fun onAddNewVarietyClick()

    // Variety form functions
    fun onNameChange(name: String)
    fun onDescriptionChange(description: String)
    fun onSubmitClick()

    // General functions
    fun firstLaunch()
}