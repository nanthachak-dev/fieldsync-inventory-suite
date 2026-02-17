package com.example.fieldsync_inventory_app.domain.use_case.last_sync

import com.example.fieldsync_inventory_app.domain.repository.last_sync.LastSyncRepository
import com.example.fieldsync_inventory_app.domain.model.LastSync
import javax.inject.Inject

class SaveAllLastSyncUseCase @Inject constructor(
    private val lastSyncRepository: LastSyncRepository
) {
    suspend operator fun invoke(lastSyncs: List<LastSync>) {
        lastSyncRepository.insertAll(lastSyncs)
    }
}