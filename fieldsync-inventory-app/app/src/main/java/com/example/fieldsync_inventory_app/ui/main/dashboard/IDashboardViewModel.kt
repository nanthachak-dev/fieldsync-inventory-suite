package com.example.fieldsync_inventory_app.ui.main.dashboard

import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.dashboard.state.DashboardUiState
import kotlinx.coroutines.flow.StateFlow

interface IDashboardViewModel {
    val resourceUiState: StateFlow<ResourceUiState>
    val dashboardUiState: StateFlow<DashboardUiState>
    fun refreshScreen()
    val isFirstLaunch: StateFlow<Boolean>
    fun firstLaunch()
    fun updateSalesRange(from: Long, to: Long)
}