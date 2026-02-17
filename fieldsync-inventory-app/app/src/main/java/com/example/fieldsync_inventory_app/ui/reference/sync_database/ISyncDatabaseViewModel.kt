package com.example.fieldsync_inventory_app.ui.reference.sync_database

import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.StateFlow

interface ISyncDatabaseViewModel {
    val resourceUiState: StateFlow<ResourceUiState>
    fun onSyncReferenceDataClicked()
    fun onSyncSeedBatchDataClicked()

    fun onSyncTransactionClicked()
    fun onSyncPersonDataClicked()
}