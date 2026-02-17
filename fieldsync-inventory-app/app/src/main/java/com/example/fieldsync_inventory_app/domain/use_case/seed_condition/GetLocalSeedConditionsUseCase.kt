package com.example.fieldsync_inventory_app.domain.use_case.seed_condition

import com.example.fieldsync_inventory_app.domain.repository.seed_condition.SeedConditionRepository
import javax.inject.Inject

class GetLocalSeedConditionsUseCase @Inject constructor(
    private val repository: SeedConditionRepository
) {
    operator fun invoke() = repository.getLocalSeedConditions()
}
