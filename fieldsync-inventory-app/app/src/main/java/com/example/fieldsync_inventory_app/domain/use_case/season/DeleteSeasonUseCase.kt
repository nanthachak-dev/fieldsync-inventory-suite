package com.example.fieldsync_inventory_app.domain.use_case.season

import com.example.fieldsync_inventory_app.domain.repository.season.SeasonRepository
import javax.inject.Inject

class DeleteSeasonUseCase @Inject constructor(
    private val repository: SeasonRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteSeason(id)
    }
}