package com.example.fieldsync_inventory_app.domain.use_case.rice_generation

import com.example.fieldsync_inventory_app.domain.repository.rice_generation.RiceGenerationRepository
import com.example.fieldsync_inventory_app.domain.model.RiceGeneration
import javax.inject.Inject

class SaveRiceGenerationUseCase @Inject constructor(
    private val repository: RiceGenerationRepository
) {
    suspend operator fun invoke(riceGeneration: RiceGeneration) {
        repository.saveRiceGeneration(riceGeneration)
    }
}