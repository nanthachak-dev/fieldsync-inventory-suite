package com.example.fieldsync_inventory_app.domain.use_case.database_sync

import android.util.Log
import com.example.fieldsync_inventory_app.domain.use_case.customer_type.SyncCustomerTypesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.rice_generation.SyncRiceGenerationsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.rice_variety.SyncRiceVarietiesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.season.SyncSeasonsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.seed_condition.SyncSeedConditionsUseCase
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_type.SyncStockMovementTypesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.supplier_type.SyncSupplierTypesUseCase
import com.example.fieldsync_inventory_app.domain.use_case.transaction_type.SyncStockTransactionTypesUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


class ReferenceSyncUseCase @Inject constructor(
    val syncRiceVarietiesUseCase: SyncRiceVarietiesUseCase,
    val syncSeasonsUseCase: SyncSeasonsUseCase,
    val syncRiceGenerationsUseCase: SyncRiceGenerationsUseCase,
    val syncSeedConditionsUseCase: SyncSeedConditionsUseCase,
    val syncStockMovementTypesUseCase: SyncStockMovementTypesUseCase,
    val syncStockTransactionTypesUseCase: SyncStockTransactionTypesUseCase,
    val syncCustomerTypesUseCase: SyncCustomerTypesUseCase,
    val syncSupplierTypesUseCase: SyncSupplierTypesUseCase
) {
    suspend operator fun invoke(): String? {
        val syncJobs = listOf(
            "Rice Varieties" to suspend { syncRiceVarietiesUseCase.sync() },
            "Seasons" to suspend { syncSeasonsUseCase.sync() },
            "Rice Generations" to suspend { syncRiceGenerationsUseCase.sync() },
            "Seed Conditions" to suspend { syncSeedConditionsUseCase.sync() },
            "Stock Movement Types" to suspend { syncStockMovementTypesUseCase.sync() },
            "Stock Transaction Types" to suspend { syncStockTransactionTypesUseCase.sync() },
            "Customer Types" to suspend { syncCustomerTypesUseCase.sync() },
            "Supplier Types" to suspend { syncSupplierTypesUseCase.sync() }
        )

        Log.d(
            "ReferenceSyncUseCase",
            "Starting reference data synchronization..."
        )
        val results = coroutineScope {
            syncJobs.map { (name, job) ->
                async { name to runCatching { job() } }
            }
        }.awaitAll()
        var errorMessages: String? = null
        val failures = results.filter { it.second.isFailure }
        if (failures.isNotEmpty()) {
            errorMessages = failures.joinToString("; ") { (name, result) ->
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                Log.e("ReferenceSyncUseCase", "`$name` synchronization failed.", result.exceptionOrNull())
                "`$name` synchronization failed: $errorMessage"
            }

        } else {
            Log.d("ReferenceSyncUseCase", "All reference data synchronized successfully.")
        }

        return errorMessages
    }
}