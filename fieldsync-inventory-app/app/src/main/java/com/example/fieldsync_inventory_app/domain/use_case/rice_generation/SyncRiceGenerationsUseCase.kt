package com.example.fieldsync_inventory_app.domain.use_case.rice_generation

import com.example.fieldsync_inventory_app.domain.repository.rice_generation.RiceGenerationRepository
import javax.inject.Inject

// Use Case (Optional but good practice)
class SyncRiceGenerationsUseCase @Inject constructor(
    private val repository: RiceGenerationRepository
) {
     suspend fun sync(){
        repository.syncRiceGenerations()
    }
}