package com.example.fieldsync_inventory_app.domain.use_case.database

import com.example.fieldsync_inventory_app.data.local.database.RcrcSeedLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClearLocalDataUseCase @Inject constructor(
    private val database: RcrcSeedLocalDatabase
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        database.clearAllTables()
    }
}
