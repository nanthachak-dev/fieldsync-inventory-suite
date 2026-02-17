package com.example.fieldsync_inventory_app.domain.use_case.rice_variety

import com.example.fieldsync_inventory_app.domain.repository.rice_variety.RiceVarietyRepository
import com.example.fieldsync_inventory_app.domain.model.RiceVariety
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRiceVarietyByNameUseCase @Inject constructor(
    private val repository: RiceVarietyRepository
) {
    operator fun invoke(name: String): Flow<RiceVariety> {
        return repository.getLocalVarietyByName(name)
    }
}