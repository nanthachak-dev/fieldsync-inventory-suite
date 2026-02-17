package com.example.fieldsync_inventory_app.ui.main.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.use_case.database.ClearLocalDataUseCase
import com.example.fieldsync_inventory_app.domain.use_case.database_sync.PersonSyncUseCase
import com.example.fieldsync_inventory_app.domain.use_case.database_sync.ReferenceSyncUseCase
import com.example.fieldsync_inventory_app.domain.use_case.last_sync.SyncChangesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.seed_batch.SyncSeedBatchesUseCase
import com.example.fieldsync_inventory_app.domain.repository.data_store.AppSettingsRepository
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details.GetRiceVarietyStockUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details.GetTopSellingVarietiesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_transaction_summary.GetTotalSaleUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_transaction_summary.GetTotalStockUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_transaction_summary.GetTotalTransactionUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.dashboard.model.BarChartData
import com.example.fieldsync_inventory_app.ui.main.dashboard.state.DashboardUiState
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import com.example.fieldsync_inventory_app.util.view_model.startLoading
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getTotalStockUseCase: GetTotalStockUseCase,
    private val getTotalTransactionUseCase: GetTotalTransactionUseCase,
    private val getTotalSaleUseCase: GetTotalSaleUseCase,
    private val getRiceVarietyStockUseCase: GetRiceVarietyStockUseCase,
    private val getTopSellingVarietiesUseCase: GetTopSellingVarietiesUseCase,
    private val syncChangesUseCase: SyncChangesUseCase,
    private val referenceSyncUseCase: ReferenceSyncUseCase,
    private val syncPersonUseCase: PersonSyncUseCase,
    private val syncSeedBatchesUseCase: SyncSeedBatchesUseCase,
    private val appSettingsRepository: AppSettingsRepository,
    private val clearLocalDataUseCase: ClearLocalDataUseCase
) : ViewModel(), IDashboardViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState
    private val _dashboardUiState = MutableStateFlow(DashboardUiState())
    override val dashboardUiState: StateFlow<DashboardUiState> = _dashboardUiState

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    // Using calendar to prevent local time zone issue if newly created sale record is at current time
    private val _salesDateRange = MutableStateFlow(
        run {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val to = calendar.timeInMillis
            val from = to - (30L * 24 * 60 * 60 * 1000)
            Pair(from, to)
        }
    )

    private var isSuccessSyncAll = true

    init {
        // Observe changes in sales date range and re-calculate metrics
        _salesDateRange.onEach {
            calculateDashboardMetrics()
        }.launchIn(viewModelScope)
    }

    override fun firstLaunch() {
        viewModelScope.launch {
            // Check if this is the first time the app is launched after installation or data clearing.
            val isFirstLaunchValue = appSettingsRepository.isFirstLaunch()

            // If it's the first launch, perform a one-time full data synchronization.
            if (isFirstLaunchValue) {
                Log.d("DashboardViewModel", "First launch detected, performing initial sync...")

                // Clear all local data to ensure a fresh start
                Log.d("DashboardViewModel", "Clearing local data...")
                clearLocalDataUseCase()

                // Trigger the comprehensive sync process.
                syncAll()
            } else {
                synChanges()
            }
            _isFirstLaunch.value = false
        }
    }

    override fun refreshScreen() {
        viewModelScope.launch {
            val isFirstLaunchValue = appSettingsRepository.isFirstLaunch()
            if (isFirstLaunchValue) {
                syncAll()
            } else {
                synChanges()
            }
        }
    }

    override fun updateSalesRange(from: Long, to: Long) {
        _salesDateRange.value = Pair(from, to)
    }

    private fun synChanges() {
        viewModelScope.launch {
            _resourceUiState.startLoading("Syncing changes latest data with the server")
            try {
                syncChangesUseCase()
                _resourceUiState.endLoading(null)
            } catch (e: Exception) {
                _resourceUiState.endLoading("Failed syncing latest data with the server: ${e.message}")
            }
            calculateDashboardMetrics()
        }
    }

    /**
     * Synchronizes all necessary data with the remote server.
     * This function follows a two-stage process:
     * 1. Fetches essential data (references, customers, seed batches) from the server in parallel.
     * 2. If the initial sync is successful, it then pushes local changes to the server.
     * This ensures that the local database is up-to-date before sending any local modifications,
     * preventing potential data conflicts.
     */
    private fun syncAll() {
        Log.d("DashboardViewModel.syncAll", "Sync all...")
        viewModelScope.launch {
            // Set loading state to true and clear any previous errors.
            _resourceUiState.startLoading("Syncing all data with the server for the first time the app is installed")

            // Define the initial synchronization tasks that can be run concurrently.
            // These tasks fetch data from the remote server.
            val syncJobs = listOf(
                "References" to suspend { referenceSyncUseCase() },
                "Customers" to suspend { syncPersonUseCase() },
                "Seed Batches" to suspend { syncSeedBatchesUseCase.sync() }
            )

            // Execute all sync jobs in parallel and wait for them to complete.
            // Each job is wrapped in runCatching to handle potential exceptions gracefully.
            val results = coroutineScope {
                syncJobs.map { (name, job) ->
                    async { name to runCatching { job() } }
                }
            }.awaitAll()

            // Check for any failures in the initial sync process.
            val failures = results.filter { it.second.isFailure }
            if (failures.isNotEmpty()) {
                // If any job failed, collect all error messages.
                val errorMessages = failures.joinToString("; ") { (name, result) ->
                    val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                    Log.e(
                        "DashboardViewModel.syncAll",
                        "$name sync failed.",
                        result.exceptionOrNull()
                    )
                    "$name sync failed: $errorMessage"
                }
                // Update UI state with the combined error message and stop the process.
                _resourceUiState.endLoading(error = "Initial sync failed: $errorMessages")
                isSuccessSyncAll = false
                return@launch
            }

            // If all initial syncs passed, proceed to sync local changes.
            Log.d("DashboardViewModel.syncAll", "Initial syncs passed")
            runCatching { syncChangesUseCase() }
                .onSuccess {
                    // On successful sync of local changes, update the UI state to reflect success.
                    _resourceUiState.endLoading(null)
                    Log.d("DashboardViewModel.syncAll", "Sync all completed successfully.")
                    calculateDashboardMetrics()

                    // After the sync, update the preference to prevent it from running again on subsequent launches.
                    if (isSuccessSyncAll) {
                        appSettingsRepository.setFirstLaunch(false)
                    }
                }
                .onFailure { error ->
                    // If syncing local changes fails, update the UI state with the error.
                    Log.e("DashboardViewModel.syncAll", "Changes sync failed.", error)
                    _resourceUiState.endLoading(error = "Syncing changes failed: ${error.message}")
                    isSuccessSyncAll = false
                }
        }
    }

    private fun calculateDashboardMetrics() {
        Log.d("DashboardViewModel.calculateDashboardMetrics", "Calculating dashboard metrics...")
        viewModelScope.launch {
            try {
                // supervisorScope is to prevent the JobCancellationException crashes
                supervisorScope {
                    val dateRange = _salesDateRange.value
                    val startDateInst = Instant.ofEpochMilli(dateRange.first)
                    val endDateInst = Instant.ofEpochMilli(dateRange.second)
                    val startDateString = DateTimeFormatter.ISO_INSTANT.format(startDateInst)
                    val endDateString = DateTimeFormatter.ISO_INSTANT.format(endDateInst)

                    // Fetch data in parallel
                    val totalStockDiffered = async { getTotalStockUseCase() }
                    val totalTransactionDiffered = async { getTotalTransactionUseCase(startDateString, endDateString) }
                    val totalSaleDiffered = async { getTotalSaleUseCase(startDateString, endDateString) }
                    val riceVarietyStockDiffered = async { getRiceVarietyStockUseCase() } // Assuming no date filter for stock breakdown
                    val topSellingDiffered = async { getTopSellingVarietiesUseCase(startDateString, endDateString) }

                    val totalStock = totalStockDiffered.await()
                    val totalTransaction = totalTransactionDiffered.await()
                    val totalSale = totalSaleDiffered.await()
                    val riceVarietyStock = riceVarietyStockDiffered.await()
                    val topSelling = topSellingDiffered.await()

                    _dashboardUiState.value = _dashboardUiState.value.copy(
                        totalStockValue = totalStock.totalStock,
                        totalTransactions = totalTransaction.totalTransactions,
                        totalSoldOut = totalSale.totalSale,
                        stockData = riceVarietyStock.content.map {
                            BarChartData(it.riceVarietyName, it.totalQuantity.toFloat())
                        }.take(10),
                        topSaleData = topSelling.content.map {
                            BarChartData(it.riceVarietyName, it.totalSoldQuantity.toFloat())
                        }.take(10),
                        salesFromDate = dateRange.first,
                        salesToDate = dateRange.second,
                    )
                }
            } catch (e: Exception) {
                if (e is retrofit2.HttpException && e.code() == 401) {
                    Log.w("DashboardViewModel", "Dashboard metrics calculation unauthorized (401). Interceptor should handle logout.")
                } else {
                    Log.e("DashboardViewModel", "Error calculating dashboard metrics", e)
                }
            }
        }
    }
}
