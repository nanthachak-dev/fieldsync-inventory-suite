package com.example.fieldsync_inventory_app.domain.use_case.rice_variety

import com.example.fieldsync_inventory_app.domain.model.RiceVariety
import com.example.fieldsync_inventory_app.domain.repository.rice_variety.RiceVarietyRepository
import javax.inject.Inject

// Use Case (Optional but good practice)
class CreateOrUpdateRiceVarietyUseCase @Inject constructor(
    private val repository: RiceVarietyRepository
) {
    suspend fun invoke(riceVariety: RiceVariety){
        repository.saveVariety(riceVariety)
    }
}