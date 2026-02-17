package com.example.fieldsync_inventory_app.ui.reference.sync_database

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.use_case.database_sync.PersonSyncUseCase
import com.example.fieldsync_inventory_app.domain.use_case.database_sync.ReferenceSyncUseCase
import com.example.fieldsync_inventory_app.domain.use_case.seed_batch.SyncSeedBatchesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details.SyncStockMovementDetailsUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import com.example.fieldsync_inventory_app.util.view_model.startLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncDatabaseViewModel @Inject constructor(
    val syncReferenceDataUseCase: ReferenceSyncUseCase,
    val syncPersonDataUseCase: PersonSyncUseCase,
    val syncSeedBatchesUseCase: SyncSeedBatchesUseCase,
    val syncStockTransactionDetailsUseCase: SyncStockMovementDetailsUseCase
) : ViewModel(), ISyncDatabaseViewModel {

    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState

    override fun onSyncReferenceDataClicked() {
        _resourceUiState.startLoading()

        viewModelScope.launch {
            val errorMessages = syncReferenceDataUseCase()
            _resourceUiState.endLoading(errorMessages)
        }
    }

    override fun onSyncPersonDataClicked() {
        _resourceUiState.startLoading()

        viewModelScope.launch {
            val errorMessages = syncPersonDataUseCase()
            _resourceUiState.endLoading(errorMessages)
        }
    }
    
    override fun onSyncSeedBatchDataClicked() {
        Log.d("SyncDatabaseViewModel", "Starting seed batch data synchronization...")

        _resourceUiState.startLoading()

        viewModelScope.launch {
            try {
                syncSeedBatchesUseCase.sync()
                Log.d(
                    "SyncDatabaseViewModel.onSyncSeedBatchDataClicked",
                    "Seed batch data synchronized successfully."
                )
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                // Handle the UI feedback (e.g., show a Snack bar saying "Sync Failed: Offline")
                Log.e(
                    "SyncDatabaseViewModel.onSyncSeedBatchDataClicked",
                    "Seed batch synchronization failed.",
                    e
                )
                _resourceUiState.endLoading("Failed to synchronize seed batch data: ${e.message}")
            }
        }
    }

    override fun onSyncTransactionClicked() {
        Log.d(
            "SyncDatabaseViewModel.onSyncTransactionClicked",
            "Starting transaction details synchronization..."
        )

        _resourceUiState.startLoading()

        viewModelScope.launch {
            try {
                syncStockTransactionDetailsUseCase.sync()
                Log.d(
                    "SyncDatabaseViewModel.onSyncTransactionClicked",
                    "Transaction details synchronized successfully."
                )
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                // Handle the UI feedback (e.g., show a Snack bar saying "Sync Failed: Offline")
                Log.e(
                    "SyncDatabaseViewModel.onSyncTransactionClicked",
                    "Transaction details synchronization failed.",
                    e
                )
                _resourceUiState.endLoading("Failed to synchronize transaction details: ${e.message}")
            }
        }
    }
}
