package com.example.fieldsync_inventory_app.domain.use_case.rice_generation

import com.example.fieldsync_inventory_app.domain.repository.rice_generation.RiceGenerationRepository
import javax.inject.Inject

class DeleteRiceGenerationUseCase @Inject constructor(
    private val repository: RiceGenerationRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteRiceGeneration(id)
    }
}