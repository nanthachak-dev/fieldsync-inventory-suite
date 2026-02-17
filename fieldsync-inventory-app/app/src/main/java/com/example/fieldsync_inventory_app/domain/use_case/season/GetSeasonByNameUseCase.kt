package com.example.fieldsync_inventory_app.domain.use_case.season

import com.example.fieldsync_inventory_app.domain.repository.season.SeasonRepository
import com.example.fieldsync_inventory_app.domain.model.Season
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSeasonByNameUseCase @Inject constructor(
    private val repository: SeasonRepository
) {
    operator fun invoke(name: String): Flow<Season?> {
        return repository.getLocalSeasonByName(name)
    }
}