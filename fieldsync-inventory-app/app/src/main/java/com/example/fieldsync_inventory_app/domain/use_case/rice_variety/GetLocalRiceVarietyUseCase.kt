package com.example.fieldsync_inventory_app.domain.use_case.rice_variety

import com.example.fieldsync_inventory_app.domain.repository.rice_variety.RiceVarietyRepository
import javax.inject.Inject

class GetLocalRiceVarietyUseCase @Inject constructor(
    private val repository: RiceVarietyRepository
){
    operator fun invoke(name: String) = repository.getLocalVarietyByName(name)
}