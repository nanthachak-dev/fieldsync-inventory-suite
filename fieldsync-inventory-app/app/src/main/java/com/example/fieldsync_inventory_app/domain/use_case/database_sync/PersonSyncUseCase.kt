package com.example.fieldsync_inventory_app.domain.use_case.database_sync

import android.util.Log
import com.example.fieldsync_inventory_app.domain.use_case.customer.SyncCustomersUseCase
import com.example.fieldsync_inventory_app.domain.use_case.supplier.SyncSuppliersUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PersonSyncUseCase @Inject constructor(
    val syncCustomersUseCase: SyncCustomersUseCase,
    val syncSupplierUseCase: SyncSuppliersUseCase
) {
    suspend operator fun invoke(): String? {
        var errorMessages: String? = null

        val syncJobs = listOf(
            "Customers" to suspend { syncCustomersUseCase.sync() },
            "Suppliers" to suspend { syncSupplierUseCase.sync() }
        )

        Log.d(
            "PersonSyncUseCase",
            "Starting person data synchronization..."
        )
        val results = coroutineScope {
            syncJobs.map { (name, job) ->
                async { name to runCatching { job() } }
            }
        }.awaitAll()

        val failures = results.filter { it.second.isFailure }
        if (failures.isNotEmpty()) {
            errorMessages = failures.joinToString("; ") { (name, result) ->
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                Log.e(
                    "SyncDatabaseViewModel.onSyncPersonDataClicked",
                    "`$name` synchronization failed.",
                    result.exceptionOrNull()
                )
                "`$name` synchronization failed: $errorMessage"
            }
        } else {
            Log.d(
                "SyncDatabaseViewModel.onSyncPersonDataClicked",
                "All person data synchronized successfully."
            )
        }

        return errorMessages
    }
}