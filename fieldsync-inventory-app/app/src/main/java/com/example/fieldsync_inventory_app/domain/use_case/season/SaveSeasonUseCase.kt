package com.example.fieldsync_inventory_app.domain.use_case.season

import com.example.fieldsync_inventory_app.domain.repository.season.SeasonRepository
import com.example.fieldsync_inventory_app.domain.model.Season
import javax.inject.Inject

class SaveSeasonUseCase @Inject constructor(
    private val repository: SeasonRepository
) {
    suspend operator fun invoke(season: Season) {
        repository.saveSeason(season)
    }
}