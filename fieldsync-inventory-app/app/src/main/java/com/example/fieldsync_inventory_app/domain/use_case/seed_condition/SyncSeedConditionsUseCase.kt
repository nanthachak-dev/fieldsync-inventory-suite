package com.example.fieldsync_inventory_app.domain.use_case.seed_condition

import com.example.fieldsync_inventory_app.domain.repository.seed_condition.SeedConditionRepository
import javax.inject.Inject

// Use Case (Optional but good practice)
class SyncSeedConditionsUseCase @Inject constructor(
    private val repository: SeedConditionRepository
) {
     suspend fun sync(){
        repository.syncSeedConditions()
    }
}
