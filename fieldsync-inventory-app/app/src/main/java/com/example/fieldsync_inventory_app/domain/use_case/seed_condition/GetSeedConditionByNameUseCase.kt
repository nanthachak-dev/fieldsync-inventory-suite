package com.example.fieldsync_inventory_app.domain.use_case.seed_condition

import com.example.fieldsync_inventory_app.domain.repository.seed_condition.SeedConditionRepository
import com.example.fieldsync_inventory_app.domain.model.SeedCondition
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSeedConditionByNameUseCase @Inject constructor(
    private val repository: SeedConditionRepository
) {
    operator fun invoke(name: String): Flow<SeedCondition?> {
        return repository.getLocalSeedConditionByName(name)
    }
}