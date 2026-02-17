package com.example.fieldsync_inventory_app.domain.use_case.rice_generation

import com.example.fieldsync_inventory_app.domain.repository.rice_generation.RiceGenerationRepository
import com.example.fieldsync_inventory_app.domain.model.RiceGeneration
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRiceGenerationByNameUseCase @Inject constructor(
    private val repository: RiceGenerationRepository
) {
    operator fun invoke(name: String): Flow<RiceGeneration?> {
        return repository.getLocalRiceGenerationByName(name)
    }
}