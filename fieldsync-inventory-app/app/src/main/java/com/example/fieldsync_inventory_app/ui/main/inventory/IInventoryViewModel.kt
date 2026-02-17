package com.example.fieldsync_inventory_app.ui.main.inventory

import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.StateFlow

interface IInventoryViewModel {
    val resourceUiState: StateFlow<ResourceUiState>
    val stockSummaryState: StateFlow<ResourceUiState>
    val isFirstLaunch: StateFlow<Boolean>
    fun firstLaunch()
    fun refreshScreen()
}