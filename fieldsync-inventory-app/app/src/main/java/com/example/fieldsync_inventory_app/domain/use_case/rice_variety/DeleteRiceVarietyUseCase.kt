package com.example.fieldsync_inventory_app.domain.use_case.rice_variety

import com.example.fieldsync_inventory_app.domain.repository.rice_variety.RiceVarietyRepository
import javax.inject.Inject

class DeleteRiceVarietyUseCase @Inject constructor(
    private val repository: RiceVarietyRepository
) {
    suspend operator fun invoke(id: Int) {
        return repository.deleteVariety(id)
    }
}