package com.example.fieldsync_inventory_app.domain.use_case.rice_variety

import com.example.fieldsync_inventory_app.domain.repository.rice_variety.RiceVarietyRepository
import javax.inject.Inject

// Use Case (Optional but good practice)
class SyncRiceVarietiesUseCase @Inject constructor(
    private val repository: RiceVarietyRepository
) {
    suspend fun sync(){
        // Fetch data from the server and save to local database
        repository.syncVarieties()
    }
}