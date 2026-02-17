package com.example.fieldsync_inventory_app.domain.use_case.rice_generation

import com.example.fieldsync_inventory_app.domain.repository.rice_generation.RiceGenerationRepository
import javax.inject.Inject

class GetLocalRiceGenerationsUseCase @Inject constructor(
    private val repository: RiceGenerationRepository
) {
    operator fun invoke() = repository.getLocalRiceGenerations()
}