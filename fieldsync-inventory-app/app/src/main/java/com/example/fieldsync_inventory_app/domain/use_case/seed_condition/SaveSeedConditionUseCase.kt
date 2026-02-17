package com.example.fieldsync_inventory_app.domain.use_case.seed_condition

import com.example.fieldsync_inventory_app.domain.repository.seed_condition.SeedConditionRepository
import com.example.fieldsync_inventory_app.domain.model.SeedCondition
import javax.inject.Inject

class SaveSeedConditionUseCase @Inject constructor(
    private val repository: SeedConditionRepository
) {
    suspend operator fun invoke(seedCondition: SeedCondition) {
        repository.saveSeedCondition(seedCondition)
    }
}
