package com.example.fieldsync_inventory_app.ui.reference.sync_database

import androidx.lifecycle.ViewModel
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewSyncDatabaseViewModel : ViewModel(), ISyncDatabaseViewModel {
    override val resourceUiState: StateFlow<ResourceUiState> =
        MutableStateFlow(ResourceUiState())

    override fun onSyncReferenceDataClicked() {
        TODO("Not yet implemented")
    }

    override fun onSyncSeedBatchDataClicked() {
        TODO("Not yet implemented")
    }

    override fun onSyncTransactionClicked() {
        TODO("Not yet implemented")
    }

    override fun onSyncPersonDataClicked() {
        TODO("Not yet implemented")
    }
}