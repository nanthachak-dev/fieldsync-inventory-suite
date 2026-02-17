package com.example.fieldsync_inventory_app.domain.use_case.season

import com.example.fieldsync_inventory_app.domain.repository.season.SeasonRepository
import javax.inject.Inject

// Use Case (Optional but good practice)
class SyncSeasonsUseCase @Inject constructor(
    private val repository: SeasonRepository
) {
     suspend fun sync() {
        repository.syncSeasons()
    }
}